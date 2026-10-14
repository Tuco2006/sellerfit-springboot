package br.com.fiap.sellerfit.services;

import br.com.fiap.sellerfit.api.AnaliseIA;
import br.com.fiap.sellerfit.api.RespostaOpenAi;
import br.com.fiap.sellerfit.conexoes.Configuracao;
import br.com.fiap.sellerfit.dto.EntradaAnalise;
import br.com.fiap.sellerfit.entities.Atendente;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;
import java.util.TreeSet;

public class OpenAiService {

    private final static String URL = "https://api.openai.com/v1/chat/completions";

    private final static String CHAVE = Configuracao.ler("OPENAI_API_KEY", "openai.api.key");
    private final static String MODELO_CONFIGURADO = Configuracao.ler("OPENAI_MODEL", "openai.model");
    private final static String MODELO = MODELO_CONFIGURADO != null && !MODELO_CONFIGURADO.isBlank() ? MODELO_CONFIGURADO.trim() : "gpt-4o-mini";

    private final static int TEMPO_LIMITE = 60000;

    private final static String INSTRUCAO_SISTEMA = "Você é um analista de CRM sênior, especialista em identificar dores de clientes " +
            "em reuniões comerciais e em recomendar qual atendente do time deve continuar cada atendimento. " +
            "Responda sempre em português do Brasil e sempre em JSON válido.";

    public boolean configurada() {
        return CHAVE != null && !CHAVE.isBlank();
    }

    public AnaliseIA analisarTranscricao(EntradaAnalise entrada, List<Atendente> atendentes) throws IOException {
        if (!configurada()) {
            throw new IOException("Chave da OpenAI não configurada");
        }

        Gson gson = new Gson();

        HttpPost request = new HttpPost(URL);
        request.setHeader("Authorization", "Bearer " + CHAVE.trim());
        request.setEntity(new StringEntity(gson.toJson(montarCorpo(entrada, atendentes)), ContentType.APPLICATION_JSON));

        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(TEMPO_LIMITE)
                .setSocketTimeout(TEMPO_LIMITE)
                .build();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
             CloseableHttpResponse response = httpClient.execute(request)) {

            HttpEntity entity = response.getEntity();
            String result = entity != null ? EntityUtils.toString(entity, "UTF-8") : "";

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new IOException("Erro na API da OpenAI: " + response.getStatusLine().getStatusCode() + " " + result);
            }

            RespostaOpenAi resposta = gson.fromJson(result, RespostaOpenAi.class);

            if (resposta == null || resposta.getConteudo() == null) {
                throw new IOException("Resposta vazia da OpenAI");
            }

            return gson.fromJson(resposta.getConteudo(), AnaliseIA.class);
        }
    }

    private JsonObject montarCorpo(EntradaAnalise entrada, List<Atendente> atendentes) {
        JsonObject sistema = new JsonObject();
        sistema.addProperty("role", "system");
        sistema.addProperty("content", INSTRUCAO_SISTEMA);

        JsonObject usuario = new JsonObject();
        usuario.addProperty("role", "user");
        usuario.addProperty("content", montarPrompt(entrada, atendentes));

        JsonArray mensagens = new JsonArray();
        mensagens.add(sistema);
        mensagens.add(usuario);

        JsonObject formato = new JsonObject();
        formato.addProperty("type", "json_object");

        JsonObject corpo = new JsonObject();
        corpo.addProperty("model", MODELO);
        corpo.addProperty("temperature", 0.4);
        corpo.add("response_format", formato);
        corpo.add("messages", mensagens);
        return corpo;
    }

    private String montarPrompt(EntradaAnalise entrada, List<Atendente> atendentes) {
        TreeSet<String> vocabularioTracos = new TreeSet<String>();
        StringBuilder catalogo = new StringBuilder();

        for (Atendente a : atendentes) {
            vocabularioTracos.addAll(a.getTracos());
            catalogo.append("- id:").append(a.getId())
                    .append(" | nome:").append(a.getNome())
                    .append(" | cargo:").append(a.getCargo())
                    .append(" | tracos:").append(String.join(", ", a.getTracos()))
                    .append(" | segmentos:").append(String.join(", ", a.getSegmentos()))
                    .append(" | foco:").append(String.join(", ", a.getFoco()))
                    .append(" | nota:").append(a.getNotaMedia())
                    .append(" | disponibilidade:").append(a.getDisponibilidade())
                    .append(" | bio:").append(a.getBio() != null ? a.getBio() : "")
                    .append("\n");
        }

        return "Você é o motor de IA do SellerFit, um sistema usado por vendedores da TOTVS logo após uma reunião comercial.\n\n" +
                "Analise a transcrição abaixo e devolva SOMENTE um JSON válido, seguindo exatamente este formato:\n\n" +
                "{\n" +
                "  \"dores\": [\"dor 1 identificada\", \"dor 2 identificada\"],\n" +
                "  \"urgencia\": \"BAIXA\" | \"MÉDIA\" | \"ALTA\",\n" +
                "  \"resumoPerfil\": \"resumo curto (2-3 frases) do perfil e prioridades do cliente\",\n" +
                "  \"tracosRecomendados\": [\"traco1\", \"traco2\"],\n" +
                "  \"segmentoDetectado\": \"segmento de mercado do cliente, uma palavra\",\n" +
                "  \"sentimentoGeral\": \"positivo\" | \"neutro\" | \"negativo\" | \"frustrado\" | \"animado\",\n" +
                "  \"atendenteRecomendadoId\": \"id de UM atendente da lista abaixo\",\n" +
                "  \"justificativaAtendente\": \"2-3 frases explicando a escolha\"\n" +
                "}\n\n" +
                "Regras:\n" +
                "- \"dores\" deve trazer as dores ditas explicitamente e também as que o cliente deu a entender nas entrelinhas. De 2 a 6 itens curtos, com no máximo 150 caracteres cada.\n" +
                "- \"urgencia\" mostra o quanto essa dor está afetando o negócio do cliente agora.\n" +
                "- \"tracosRecomendados\" deve ter de 2 a 4 palavras escolhidas OBRIGATORIAMENTE desta lista (escritas exatamente igual): " +
                String.join(", ", vocabularioTracos) + ".\n" +
                "- \"segmentoDetectado\" deve se basear no segmento informado (" + entrada.getSegmento() + ") e no que aparecer na transcrição.\n" +
                "- \"atendenteRecomendadoId\" TEM que ser exatamente um dos ids do catálogo abaixo. Escolha considerando o jeito de falar do cliente, " +
                "a dor específica dele, a disponibilidade e a especialidade descrita na bio de cada atendente, não só o segmento.\n" +
                "- Na \"justificativaAtendente\" cite algo concreto que o cliente disse ou precisa, cruzado com algo do perfil ou da bio do atendente escolhido.\n\n" +
                "Catálogo de atendentes:\n" + catalogo + "\n" +
                "Dados da reunião:\n" +
                "Cliente: " + entrada.getClienteNome() + "\n" +
                "Empresa: " + entrada.getEmpresa() + "\n" +
                "Segmento informado: " + entrada.getSegmento() + "\n" +
                (entrada.getNotaNps() != null ? "Nota NPS: " + entrada.getNotaNps() + "\n" : "") +
                "\nTranscrição da reunião:\n\"\"\"\n" + entrada.getTranscricao() + "\n\"\"\"";
    }
}
