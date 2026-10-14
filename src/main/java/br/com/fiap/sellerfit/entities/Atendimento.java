package br.com.fiap.sellerfit.entities;

import br.com.fiap.sellerfit.api.AnaliseIA;
import br.com.fiap.sellerfit.dto.MatchAtendente;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Atendimento {

    private Cliente cliente;
    private String transcricao;
    private Integer notaNps;
    private String sinalNegocio;
    private String justificativaSinal;

    private AnaliseIA analiseIA;

    // palavra encontrada na transcricao -> dor que ela representa
    private static final Map<String, String> PALAVRAS_DOR = new LinkedHashMap<String, String>();

    static {
        PALAVRAS_DOR.put("lento", "sistema lento no dia a dia");
        PALAVRAS_DOR.put("travando", "instabilidade / travamentos frequentes");
        PALAVRAS_DOR.put("confuso", "interface ou processo confuso");
        PALAVRAS_DOR.put("manual", "processos manuais que poderiam ser automatizados");
        PALAVRAS_DOR.put("caro", "percepção de custo alto");
        PALAVRAS_DOR.put("perde tempo", "perda de tempo em tarefas repetitivas");
        PALAVRAS_DOR.put("suporte", "insatisfação com o suporte atual");
        PALAVRAS_DOR.put("planilha", "dependência de planilhas paralelas");
        PALAVRAS_DOR.put("atraso", "atrasos em entregas ou processos");
        PALAVRAS_DOR.put("dificil", "dificuldade de uso do sistema atual");
        PALAVRAS_DOR.put("integra", "falta de integração entre sistemas");
        PALAVRAS_DOR.put("retrabalho", "retrabalho por falha de processo");
    }

    private static final String[] PALAVRAS_PROBLEMA = {
            "cancelar", "cancelamento", "rescindir", "rescisão", "encerrar", "desistir", "trocar de fornecedor",
            "insatisfeito", "insatisfação", "reclamação", "reclamar", "decepcionado", "frustrado", "ruim", "péssimo",
            "problema", "erro", "falha", "bug", "instabilidade", "indisponível", "travando", "lentidão", "lento",
            "não funciona", "dificuldade", "difícil", "complicado", "demora", "atraso", "caro", "custo alto",
            "aumento de preço", "reajuste", "não compensa"
    };

    private static final String[] PALAVRAS_OPORTUNIDADE = {
            "interesse", "interessado", "contratar", "comprar", "adquirir", "proposta", "orçamento", "cotação",
            "crescer", "crescimento", "expansão", "expandir", "ampliar", "adicionar", "upgrade", "novo módulo",
            "nova funcionalidade", "novo produto", "outro módulo", "mais módulos", "integração", "integrar",
            "implementar", "implantação", "investir", "investimento", "melhoria", "modernizar", "automatizar", "automação"
    };

    private static final String[] PRODUTOS_TOTVS = {
            "rm", "fiscal", "portal", "crm", "fluig", "wms", "pdv", "tms", "datasul", "logix", "winthor", "protheus"
    };

    private static final String[] CONCORRENTES = {
            "sap", "oracle", "linx", "alterdata", "sankhya", "salesforce"
    };

    public Atendimento() {
    }

    public Atendimento(Cliente cliente, String transcricao, Integer notaNps) {
        this.cliente = cliente;
        this.transcricao = transcricao;
        this.notaNps = notaNps;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getTranscricao() {
        return transcricao;
    }

    public void setTranscricao(String transcricao) {
        this.transcricao = transcricao;
    }

    public Integer getNotaNps() {
        return notaNps;
    }

    public void setNotaNps(Integer notaNps) {
        this.notaNps = notaNps;
    }

    public String getSinalNegocio() {
        return sinalNegocio;
    }

    public String getJustificativaSinal() {
        return justificativaSinal;
    }

    public AnaliseIA getAnaliseIA() {
        return analiseIA;
    }

    public void aplicarAnaliseIA(AnaliseIA analiseIA) {
        this.analiseIA = analiseIA;
    }

    public String getOrigemAnalise() {
        return analiseIA != null ? "openai" : "motor-local";
    }

    // varre a transcricao procurando as palavras de dor
    public List<String> identificarDores() {
        if (analiseIA != null && temItens(analiseIA.getDores())) {
            return new ArrayList<String>(analiseIA.getDores());
        }

        String texto = normalizar(transcricao);
        List<String> dores = new ArrayList<String>();

        for (Map.Entry<String, String> palavra : PALAVRAS_DOR.entrySet()) {
            if (texto.contains(palavra.getKey())) {
                dores.add(palavra.getValue());
            }
        }
        return dores;
    }

    // quanto mais dor identificada, maior a urgencia
    public String definirNivelUrgencia() {
        if (analiseIA != null && analiseIA.getUrgencia() != null) {
            return normalizarUrgencia(analiseIA.getUrgencia());
        }

        int qtdDores = identificarDores().size();

        if (qtdDores >= 3) {
            return "ALTA";
        }
        if (qtdDores >= 1) {
            return "MÉDIA";
        }
        return "BAIXA";
    }

    public String definirSentimento() {
        if (analiseIA != null && analiseIA.getSentimentoGeral() != null && !analiseIA.getSentimentoGeral().isBlank()) {
            return analiseIA.getSentimentoGeral().toLowerCase();
        }

        int qtdDores = identificarDores().size();

        if (qtdDores >= 3) {
            return "frustrado";
        }
        if (qtdDores > 0) {
            return "neutro";
        }
        return "positivo";
    }

    // tracos de comportamento que o atendente ideal deveria ter
    public List<String> recomendarTracos() {
        if (analiseIA != null && temItens(analiseIA.getTracosRecomendados())) {
            return new ArrayList<String>(analiseIA.getTracosRecomendados());
        }

        String texto = normalizar(transcricao);
        List<String> tracos = new ArrayList<String>();

        if (texto.contains("rapido") || texto.contains("urgente")) {
            adicionarSemRepetir(tracos, "ágil");
        }
        if (texto.contains("confuso") || texto.contains("entender")) {
            adicionarSemRepetir(tracos, "didático");
        }
        if (texto.contains("caro") || texto.contains("orcamento")) {
            adicionarSemRepetir(tracos, "negociadora");
        }
        if (texto.contains("tecnico") || texto.contains("sistema") || texto.contains("integra")) {
            adicionarSemRepetir(tracos, "técnico");
        }
        if (texto.contains("expandir") || texto.contains("investir") || texto.contains("contratar") || texto.contains("crescer")) {
            adicionarSemRepetir(tracos, "estratégico");
            adicionarSemRepetir(tracos, "negociadora");
        }
        if (texto.contains("automatizar") || texto.contains("modernizar") || texto.contains("upgrade")) {
            adicionarSemRepetir(tracos, "estratégico");
        }
        if (tracos.isEmpty()) {
            tracos.add("comunicativa");
            tracos.add("resolutivo");
        }
        return tracos;
    }

    public String classificarZonaNps() {
        if (notaNps == null) {
            return "Sem nota";
        }
        if (notaNps >= 9) {
            return "Promotor";
        }
        if (notaNps >= 7) {
            return "Passivo";
        }
        return "Detrator";
    }

    public List<String> identificarMencoesTotvs() {
        return procurarTermos(PRODUTOS_TOTVS);
    }

    public List<String> identificarMencoesConcorrentes() {
        return procurarTermos(CONCORRENTES);
    }

    // regra de negocio: detrator + concorrente/problema = churn, promotor + oportunidade = upsell
    public String definirSinalNegocio() {
        List<String> concorrentes = identificarMencoesConcorrentes();
        boolean mencionaConcorrente = !concorrentes.isEmpty();
        boolean possuiProblema = contemAlgumTermo(PALAVRAS_PROBLEMA);
        boolean possuiOportunidade = contemAlgumTermo(PALAVRAS_OPORTUNIDADE);
        String zonaNps = classificarZonaNps();

        if (mencionaConcorrente && possuiProblema) {
            sinalNegocio = "ALERTA_CHURN";
            justificativaSinal = "Cliente mencionou concorrente (" + String.join(", ", concorrentes) + ") junto com sinais de insatisfação no texto.";
        } else if (zonaNps.equals("Detrator") && possuiProblema) {
            sinalNegocio = "ALERTA_CHURN";
            justificativaSinal = "NPS na zona Detrator combinado com sinais de problema no texto.";
        } else if (zonaNps.equals("Promotor") && possuiOportunidade) {
            sinalNegocio = "OPORTUNIDADE_UPSELL";
            justificativaSinal = "NPS na zona Promotor combinado com sinais de interesse comercial no texto.";
        } else if (zonaNps.equals("Sem nota") && possuiOportunidade && !possuiProblema) {
            sinalNegocio = "OPORTUNIDADE_UPSELL";
            justificativaSinal = "Sem nota de NPS informada, mas o texto tem sinais claros de interesse comercial.";
        } else {
            sinalNegocio = "NEUTRO";
            justificativaSinal = "Não foram identificados sinais suficientes de risco de churn ou oportunidade de upsell.";
        }
        return sinalNegocio;
    }

    // monta o perfil do cliente a partir da reuniao
    public String gerarPerfilCliente() {
        if (analiseIA != null && analiseIA.getResumoPerfil() != null && !analiseIA.getResumoPerfil().isBlank()) {
            return analiseIA.getResumoPerfil();
        }

        return "Cliente da empresa " + cliente.getEmpresa() +
                ", segmento " + cliente.getSegmento() +
                ". Urgência " + definirNivelUrgencia().toLowerCase() +
                " com " + identificarDores().size() + " dor(es) identificada(s) na transcrição.";
    }

    // compara o perfil do atendente com o que foi falado na reuniao
    public int calcularScoreCompatibilidade(Atendente atendente) {
        List<String> tracosRecomendados = recomendarTracos();

        int tracosBatidos = tracosEmComum(atendente, tracosRecomendados).size();
        double scoreTracos = (double) tracosBatidos / tracosRecomendados.size() * 50;
        double scoreSegmento = atendeSegmento(atendente) ? 20 : 0;
        double scoreNota = atendente.getNotaMedia();
        double scoreFoco = atendeFoco(atendente) ? 20 : 0;

        int score = (int) Math.round(scoreTracos + scoreSegmento + scoreNota + scoreFoco);
        return Math.min(score, 100);
    }

    public String gerarMotivo(Atendente atendente) {
        List<String> partes = new ArrayList<String>();

        if (atendeFoco(atendente)) {
            partes.add(sinalNegocio.equals("ALERTA_CHURN") ? "especialista em retenção de clientes" : "especialista em cross-sell");
        }
        List<String> tracos = tracosEmComum(atendente, recomendarTracos());
        if (!tracos.isEmpty()) {
            partes.add("perfil " + String.join(", ", tracos));
        }
        if (atendeSegmento(atendente)) {
            partes.add("experiência no segmento do cliente");
        }
        partes.add("nota média " + String.format("%.1f", atendente.getNotaMedia()) + " em " + atendente.getCasesResolvidos() + " atendimentos");

        return "Indicado por " + String.join(" + ", partes) + ".";
    }

    // faz o match entre o cliente e os melhores atendentes da lista
    public List<MatchAtendente> sugerirMelhoresAtendentes(List<Atendente> atendentes, int limite) {
        if (sinalNegocio == null) {
            definirSinalNegocio();
        }

        List<MatchAtendente> matches = new ArrayList<MatchAtendente>();
        for (Atendente atendente : atendentes) {
            matches.add(new MatchAtendente(atendente, calcularScoreCompatibilidade(atendente), gerarMotivo(atendente)));
        }

        matches.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

        if (matches.size() > limite) {
            return new ArrayList<MatchAtendente>(matches.subList(0, limite));
        }
        return matches;
    }

    private boolean atendeFoco(Atendente atendente) {
        if (sinalNegocio == null || sinalNegocio.equals("NEUTRO")) {
            return false;
        }
        String focoEsperado = sinalNegocio.equals("ALERTA_CHURN") ? "retencao" : "cross-sell";

        for (String foco : atendente.getFoco()) {
            if (normalizar(foco).equals(focoEsperado)) {
                return true;
            }
        }
        return false;
    }

    private boolean atendeSegmento(Atendente atendente) {
        String segmentoCliente = raiz(cliente.getSegmento());

        for (String segmento : atendente.getSegmentos()) {
            if (raiz(segmento).equals(segmentoCliente)) {
                return true;
            }
        }
        return false;
    }

    private List<String> tracosEmComum(Atendente atendente, List<String> tracosRecomendados) {
        List<String> emComum = new ArrayList<String>();

        for (String traco : atendente.getTracos()) {
            for (String recomendado : tracosRecomendados) {
                if (raiz(traco).equals(raiz(recomendado))) {
                    emComum.add(traco);
                    break;
                }
            }
        }
        return emComum;
    }

    private List<String> procurarTermos(String[] termos) {
        List<String> encontrados = new ArrayList<String>();

        for (String termo : termos) {
            if (contemPalavra(termo)) {
                encontrados.add(termo);
            }
        }
        return encontrados;
    }

    private boolean contemAlgumTermo(String[] termos) {
        for (String termo : termos) {
            if (contemPalavra(termo)) {
                return true;
            }
        }
        return false;
    }

    // procura a palavra inteira, sem pegar pedaco de outra palavra
    private boolean contemPalavra(String termo) {
        Pattern padrao = Pattern.compile("(?<![\\p{L}\\d])" + Pattern.quote(normalizar(termo)) + "(?![\\p{L}\\d])");
        return padrao.matcher(normalizar(transcricao)).find();
    }

    private boolean temItens(List<String> lista) {
        return lista != null && !lista.isEmpty();
    }

    private String normalizarUrgencia(String urgencia) {
        String valor = normalizar(urgencia).toUpperCase().trim();

        if (valor.equals("ALTA")) {
            return "ALTA";
        }
        if (valor.equals("BAIXA")) {
            return "BAIXA";
        }
        return "MÉDIA";
    }

    private void adicionarSemRepetir(List<String> lista, String valor) {
        if (!lista.contains(valor)) {
            lista.add(valor);
        }
    }

    // ignora diferenca de genero tipo "direto" / "direta"
    private String raiz(String palavra) {
        String p = normalizar(palavra).trim();
        if (p.length() > 4 && (p.endsWith("a") || p.endsWith("o"))) {
            return p.substring(0, p.length() - 1);
        }
        return p;
    }

    // deixa minusculo e sem acento
    private String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        return Normalizer.normalize(texto.toLowerCase(), Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    }

    @Override
    public String toString() {
        return "ATENDIMENTO " +
                "\n" + gerarPerfilCliente() +
                "\nDores: " + identificarDores() +
                "\nSinal de negócio: " + (sinalNegocio != null ? sinalNegocio : "nao definido");
    }
}
