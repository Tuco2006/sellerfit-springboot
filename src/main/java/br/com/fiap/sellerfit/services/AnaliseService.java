package br.com.fiap.sellerfit.services;

import br.com.fiap.sellerfit.api.AnaliseIA;
import br.com.fiap.sellerfit.dao.AtendenteDAO;
import br.com.fiap.sellerfit.dao.ClienteDAO;
import br.com.fiap.sellerfit.dao.DorDAO;
import br.com.fiap.sellerfit.dao.MatchDAO;
import br.com.fiap.sellerfit.dao.ReuniaoDAO;
import br.com.fiap.sellerfit.dto.AnaliseTranscricao;
import br.com.fiap.sellerfit.dto.EntradaAnalise;
import br.com.fiap.sellerfit.dto.MatchAtendente;
import br.com.fiap.sellerfit.dto.RecomendacaoIA;
import br.com.fiap.sellerfit.dto.ResultadoAnalise;
import br.com.fiap.sellerfit.entities.Atendente;
import br.com.fiap.sellerfit.entities.Atendimento;
import br.com.fiap.sellerfit.entities.Cliente;
import br.com.fiap.sellerfit.entities.Dor;
import br.com.fiap.sellerfit.entities.Match;
import br.com.fiap.sellerfit.entities.Reuniao;
import br.com.fiap.sellerfit.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.sellerfit.excecoes.RegraNegocioException;

import com.google.gson.JsonParseException;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AnaliseService {

    private static final int QTD_SUGESTOES = 3;
    private static final int TAMANHO_MAX_DOR = 200;

    private final OpenAiService openAiService = new OpenAiService();

    // recebe a transcricao, identifica as dores, faz o match e salva tudo no banco
    public ResultadoAnalise analisar(EntradaAnalise entrada) throws SQLException, ClassNotFoundException {
        validar(entrada);

        List<Atendente> atendentes = listarAtendentes();
        Cliente cliente = buscarOuCadastrarCliente(entrada);

        Atendimento atendimento = new Atendimento(cliente, entrada.getTranscricao(), entrada.getNotaNps());
        analisarComIA(atendimento, entrada, atendentes);
        atendimento.definirSinalNegocio();

        Reuniao reuniao = new Reuniao();
        reuniao.setIdCliente(cliente.getId());
        reuniao.setDataReuniao(LocalDate.now());
        reuniao.setTranscricao(entrada.getTranscricao().trim());
        reuniao.setNotaNps(entrada.getNotaNps());
        reuniao.setUrgencia(atendimento.definirNivelUrgencia());
        reuniao.setSinalNegocio(atendimento.getSinalNegocio());
        reuniao.setStatus("ANALISADA");

        try (ReuniaoDAO reuniaoDAO = new ReuniaoDAO()) {
            reuniaoDAO.inserir(reuniao);
        }

        try (DorDAO dorDAO = new DorDAO()) {
            for (String descricao : atendimento.identificarDores()) {
                dorDAO.inserir(new Dor(0, reuniao.getId(), limitar(descricao, TAMANHO_MAX_DOR), reuniao.getUrgencia()));
            }
        }

        List<MatchAtendente> matches = atendimento.sugerirMelhoresAtendentes(atendentes, QTD_SUGESTOES);

        try (MatchDAO matchDAO = new MatchDAO()) {
            for (MatchAtendente m : matches) {
                matchDAO.inserir(new Match(0, reuniao.getId(), m.getAtendente().getId(), m.getScore(), m.getMotivo(), "SUGERIDO"));
            }
        }

        return new ResultadoAnalise(montarAnalise(atendimento, reuniao.getId()), matches, montarRecomendacaoIA(atendimento, atendentes));
    }

    // refaz o resultado de uma reuniao que ja foi analisada
    public ResultadoAnalise buscarPorReuniao(int idReuniao) throws SQLException, ClassNotFoundException {
        Reuniao reuniao;
        Cliente cliente;
        List<String> doresSalvas = new ArrayList<String>();

        try (ReuniaoDAO reuniaoDAO = new ReuniaoDAO()) {
            reuniao = reuniaoDAO.buscarPorId(idReuniao);
        }
        if (reuniao == null) {
            throw new RecursoNaoEncontradoException("Reunião " + idReuniao + " não encontrada");
        }

        try (ClienteDAO clienteDAO = new ClienteDAO()) {
            cliente = clienteDAO.buscarPorId(reuniao.getIdCliente());
        }

        try (DorDAO dorDAO = new DorDAO()) {
            for (Dor dor : dorDAO.selecionarPorReuniao(idReuniao)) {
                doresSalvas.add(dor.getDescricao());
            }
        }

        Atendimento atendimento = new Atendimento(cliente, reuniao.getTranscricao(), reuniao.getNotaNps());
        atendimento.definirSinalNegocio();

        List<MatchAtendente> matches = new ArrayList<MatchAtendente>();
        try (MatchDAO matchDAO = new MatchDAO(); AtendenteDAO atendenteDAO = new AtendenteDAO()) {
            for (Match match : matchDAO.selecionarPorReuniao(idReuniao)) {
                Atendente atendente = atendenteDAO.buscarPorId(match.getIdAtendente());
                matches.add(new MatchAtendente(atendente, (int) match.getScoreCompatibilidade(), match.getMotivo()));
            }
        }

        AnaliseTranscricao analise = montarAnalise(atendimento, idReuniao);
        if (!doresSalvas.isEmpty()) {
            analise.setDores(doresSalvas);
        }
        if (reuniao.getUrgencia() != null) {
            analise.setUrgencia(reuniao.getUrgencia());
        }
        if (reuniao.getSinalNegocio() != null) {
            analise.setSinalNegocio(reuniao.getSinalNegocio());
        }

        return new ResultadoAnalise(analise, matches);
    }

    private void analisarComIA(Atendimento atendimento, EntradaAnalise entrada, List<Atendente> atendentes) {
        if (!openAiService.configurada()) {
            return;
        }

        try {
            AnaliseIA analiseIA = openAiService.analisarTranscricao(entrada, atendentes);
            if (analiseIA != null) {
                atendimento.aplicarAnaliseIA(analiseIA);
            }
        } catch (IOException | JsonParseException e) {
            System.out.println("Falha ao chamar a OpenAI, usando o motor local: " + e.getMessage());
        }
    }

    private RecomendacaoIA montarRecomendacaoIA(Atendimento atendimento, List<Atendente> atendentes) {
        AnaliseIA analiseIA = atendimento.getAnaliseIA();

        if (analiseIA == null || analiseIA.getAtendenteRecomendadoId() == null) {
            return null;
        }

        for (Atendente atendente : atendentes) {
            if (String.valueOf(atendente.getId()).equals(analiseIA.getAtendenteRecomendadoId().trim())) {
                String justificativa = analiseIA.getJustificativaAtendente();
                if (justificativa == null || justificativa.isBlank()) {
                    justificativa = "A IA recomendou esse atendente com base no perfil do cliente.";
                }
                return new RecomendacaoIA(atendente, justificativa);
            }
        }
        return null;
    }

    private AnaliseTranscricao montarAnalise(Atendimento atendimento, int idReuniao) {
        List<String> dores = atendimento.identificarDores();
        if (dores.isEmpty()) {
            dores.add("nenhuma dor explícita identificada");
        }

        AnaliseTranscricao analise = new AnaliseTranscricao();
        analise.setIdReuniao(idReuniao);
        analise.setDores(dores);
        analise.setUrgencia(atendimento.definirNivelUrgencia());
        analise.setResumoPerfil(atendimento.gerarPerfilCliente());
        analise.setTracosRecomendados(atendimento.recomendarTracos());
        analise.setSegmentoDetectado(definirSegmento(atendimento));
        analise.setSentimentoGeral(atendimento.definirSentimento());
        analise.setOrigemAnalise(atendimento.getOrigemAnalise());
        analise.setSinalNegocio(atendimento.getSinalNegocio());
        analise.setJustificativaSinal(atendimento.getJustificativaSinal());
        analise.setZonaNps(atendimento.classificarZonaNps());
        analise.setMencoesTotvs(atendimento.identificarMencoesTotvs());
        analise.setMencoesConcorrentes(atendimento.identificarMencoesConcorrentes());
        return analise;
    }

    private String definirSegmento(Atendimento atendimento) {
        AnaliseIA analiseIA = atendimento.getAnaliseIA();

        if (analiseIA != null && analiseIA.getSegmentoDetectado() != null && !analiseIA.getSegmentoDetectado().isBlank()) {
            return analiseIA.getSegmentoDetectado().toLowerCase();
        }
        return atendimento.getCliente().getSegmento();
    }

    private String limitar(String texto, int tamanho) {
        if (texto.length() <= tamanho) {
            return texto;
        }
        return texto.substring(0, tamanho - 3) + "...";
    }

    private Cliente buscarOuCadastrarCliente(EntradaAnalise entrada) throws SQLException, ClassNotFoundException {
        try (ClienteDAO clienteDAO = new ClienteDAO()) {
            Cliente cliente = clienteDAO.buscarPorNomeEmpresa(entrada.getClienteNome().trim(), entrada.getEmpresa().trim());

            if (cliente == null) {
                cliente = new Cliente(0, entrada.getClienteNome().trim(), entrada.getEmpresa().trim(), entrada.getSegmento().trim().toLowerCase(), 0);
                clienteDAO.inserir(cliente);
            }
            return cliente;
        }
    }

    private List<Atendente> listarAtendentes() throws SQLException, ClassNotFoundException {
        try (AtendenteDAO atendenteDAO = new AtendenteDAO()) {
            List<Atendente> atendentes = atendenteDAO.selecionar();

            if (atendentes.isEmpty()) {
                throw new RegraNegocioException("Nenhum atendente cadastrado para fazer o match");
            }
            return atendentes;
        }
    }

    // Regra de negocios
    private void validar(EntradaAnalise entrada) {
        if (entrada == null) {
            throw new RegraNegocioException("Dados da análise não informados");
        }
        if (entrada.getClienteNome() == null || entrada.getClienteNome().isBlank()) {
            throw new RegraNegocioException("Informe o nome do cliente");
        }
        if (entrada.getClienteNome().trim().length() > 100) {
            throw new RegraNegocioException("O nome do cliente deve ter no máximo 100 caracteres");
        }
        if (entrada.getEmpresa() == null || entrada.getEmpresa().isBlank()) {
            throw new RegraNegocioException("Informe a empresa");
        }
        if (entrada.getEmpresa().trim().length() > 100) {
            throw new RegraNegocioException("O nome da empresa deve ter no máximo 100 caracteres");
        }
        if (entrada.getSegmento() == null || entrada.getSegmento().isBlank()) {
            throw new RegraNegocioException("Informe o segmento");
        }
        if (entrada.getSegmento().trim().length() > 50) {
            throw new RegraNegocioException("O segmento deve ter no máximo 50 caracteres");
        }
        if (entrada.getTranscricao() == null || entrada.getTranscricao().trim().length() < 20) {
            throw new RegraNegocioException("A transcrição precisa ter pelo menos 20 caracteres");
        }
        if (entrada.getNotaNps() != null && (entrada.getNotaNps() < 0 || entrada.getNotaNps() > 10)) {
            throw new RegraNegocioException("A nota NPS deve estar entre 0 e 10");
        }
    }
}
