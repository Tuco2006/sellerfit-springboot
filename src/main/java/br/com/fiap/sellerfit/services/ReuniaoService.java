package br.com.fiap.sellerfit.services;

import br.com.fiap.sellerfit.dao.ClienteDAO;
import br.com.fiap.sellerfit.dao.ReuniaoDAO;
import br.com.fiap.sellerfit.entities.Reuniao;
import br.com.fiap.sellerfit.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.sellerfit.excecoes.RegraNegocioException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ReuniaoService {

    private static final List<String> STATUS = List.of("ABERTA", "ANALISADA", "FINALIZADA");
    private static final List<String> URGENCIAS = List.of("BAIXA", "MÉDIA", "ALTA");
    private static final List<String> SINAIS = List.of("ALERTA_CHURN", "OPORTUNIDADE_UPSELL", "NEUTRO");

    // Selecionar
    public List<Reuniao> selecionar() throws SQLException, ClassNotFoundException {
        try (ReuniaoDAO reuniaoDAO = new ReuniaoDAO()) {
            return reuniaoDAO.selecionar();
        }
    }

    // Selecionar as reunioes de um cliente
    public List<Reuniao> selecionarPorCliente(int idCliente) throws SQLException, ClassNotFoundException {
        validarCliente(idCliente);

        try (ReuniaoDAO reuniaoDAO = new ReuniaoDAO()) {
            return reuniaoDAO.selecionarPorCliente(idCliente);
        }
    }

    // Buscar por id
    public Reuniao buscarPorId(int id) throws SQLException, ClassNotFoundException {
        try (ReuniaoDAO reuniaoDAO = new ReuniaoDAO()) {
            Reuniao reuniao = reuniaoDAO.buscarPorId(id);

            if (reuniao == null) {
                throw new RecursoNaoEncontradoException("Reunião " + id + " não encontrada");
            }
            return reuniao;
        }
    }

    // Inserir
    public Reuniao inserir(Reuniao reuniao) throws SQLException, ClassNotFoundException {
        validar(reuniao);

        try (ReuniaoDAO reuniaoDAO = new ReuniaoDAO()) {
            reuniaoDAO.inserir(reuniao);
            return reuniao;
        }
    }

    // Atualizar
    public Reuniao atualizar(int id, Reuniao reuniao) throws SQLException, ClassNotFoundException {
        validar(reuniao);
        reuniao.setId(id);

        try (ReuniaoDAO reuniaoDAO = new ReuniaoDAO()) {
            if (reuniaoDAO.atualizar(reuniao) == 0) {
                throw new RecursoNaoEncontradoException("Reunião " + id + " não encontrada");
            }
            return reuniao;
        }
    }

    // Deletar
    public void deletar(int id) throws SQLException, ClassNotFoundException {
        try (ReuniaoDAO reuniaoDAO = new ReuniaoDAO()) {
            if (reuniaoDAO.deletar(id) == 0) {
                throw new RecursoNaoEncontradoException("Reunião " + id + " não encontrada");
            }
        }
    }

    // Regra de negocios
    private void validar(Reuniao reuniao) throws SQLException, ClassNotFoundException {
        if (reuniao == null) {
            throw new RegraNegocioException("Dados da reunião não informados");
        }
        if (reuniao.getTranscricao() == null || reuniao.getTranscricao().trim().length() < 20) {
            throw new RegraNegocioException("A transcrição precisa ter pelo menos 20 caracteres");
        }
        if (reuniao.getNotaNps() != null && (reuniao.getNotaNps() < 0 || reuniao.getNotaNps() > 10)) {
            throw new RegraNegocioException("A nota NPS deve estar entre 0 e 10");
        }
        if (reuniao.getDataReuniao() == null) {
            reuniao.setDataReuniao(LocalDate.now());
        }
        if (reuniao.getDataReuniao().isAfter(LocalDate.now())) {
            throw new RegraNegocioException("A data da reunião não pode ser no futuro");
        }
        if (reuniao.getStatus() == null) {
            reuniao.setStatus("ABERTA");
        }
        if (!STATUS.contains(reuniao.getStatus())) {
            throw new RegraNegocioException("Status inválido. Use ABERTA, ANALISADA ou FINALIZADA");
        }
        if (reuniao.getUrgencia() != null && !URGENCIAS.contains(reuniao.getUrgencia())) {
            throw new RegraNegocioException("Urgência inválida. Use BAIXA, MÉDIA ou ALTA");
        }
        if (reuniao.getSinalNegocio() != null && !SINAIS.contains(reuniao.getSinalNegocio())) {
            throw new RegraNegocioException("Sinal de negócio inválido. Use ALERTA_CHURN, OPORTUNIDADE_UPSELL ou NEUTRO");
        }
        validarCliente(reuniao.getIdCliente());
    }

    private void validarCliente(int idCliente) throws SQLException, ClassNotFoundException {
        try (ClienteDAO clienteDAO = new ClienteDAO()) {
            if (clienteDAO.buscarPorId(idCliente) == null) {
                throw new RegraNegocioException("Cliente " + idCliente + " não cadastrado");
            }
        }
    }
}
