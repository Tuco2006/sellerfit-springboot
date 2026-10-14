package br.com.fiap.sellerfit.api;

import java.util.List;

public class AnaliseIA {

    private List<String> dores;
    private String urgencia;
    private String resumoPerfil;
    private List<String> tracosRecomendados;
    private String segmentoDetectado;
    private String sentimentoGeral;
    private String atendenteRecomendadoId;
    private String justificativaAtendente;

    public AnaliseIA() {
    }

    public List<String> getDores() {
        return dores;
    }

    public void setDores(List<String> dores) {
        this.dores = dores;
    }

    public String getUrgencia() {
        return urgencia;
    }

    public void setUrgencia(String urgencia) {
        this.urgencia = urgencia;
    }

    public String getResumoPerfil() {
        return resumoPerfil;
    }

    public void setResumoPerfil(String resumoPerfil) {
        this.resumoPerfil = resumoPerfil;
    }

    public List<String> getTracosRecomendados() {
        return tracosRecomendados;
    }

    public void setTracosRecomendados(List<String> tracosRecomendados) {
        this.tracosRecomendados = tracosRecomendados;
    }

    public String getSegmentoDetectado() {
        return segmentoDetectado;
    }

    public void setSegmentoDetectado(String segmentoDetectado) {
        this.segmentoDetectado = segmentoDetectado;
    }

    public String getSentimentoGeral() {
        return sentimentoGeral;
    }

    public void setSentimentoGeral(String sentimentoGeral) {
        this.sentimentoGeral = sentimentoGeral;
    }

    public String getAtendenteRecomendadoId() {
        return atendenteRecomendadoId;
    }

    public void setAtendenteRecomendadoId(String atendenteRecomendadoId) {
        this.atendenteRecomendadoId = atendenteRecomendadoId;
    }

    public String getJustificativaAtendente() {
        return justificativaAtendente;
    }

    public void setJustificativaAtendente(String justificativaAtendente) {
        this.justificativaAtendente = justificativaAtendente;
    }

    @Override
    public String toString() {
        return "Análise da IA " +
                "\ndores=" + dores +
                "\nurgencia='" + urgencia + '\'' +
                "\nresumoPerfil='" + resumoPerfil + '\'' +
                "\ntracosRecomendados=" + tracosRecomendados +
                "\nsegmentoDetectado='" + segmentoDetectado + '\'' +
                "\nsentimentoGeral='" + sentimentoGeral + '\'' +
                "\natendenteRecomendadoId='" + atendenteRecomendadoId + '\'' +
                "\njustificativaAtendente='" + justificativaAtendente + '\'';
    }
}
