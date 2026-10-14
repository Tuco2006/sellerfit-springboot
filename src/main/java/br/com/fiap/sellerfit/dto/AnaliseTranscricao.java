package br.com.fiap.sellerfit.dto;

import java.util.List;

public class AnaliseTranscricao {

    private int idReuniao;
    private List<String> dores;
    private String urgencia;
    private String resumoPerfil;
    private List<String> tracosRecomendados;
    private String segmentoDetectado;
    private String sentimentoGeral;
    private String origemAnalise;
    private String sinalNegocio;
    private String justificativaSinal;
    private String zonaNps;
    private List<String> mencoesTotvs;
    private List<String> mencoesConcorrentes;

    public AnaliseTranscricao() {
    }

    public int getIdReuniao() {
        return idReuniao;
    }

    public void setIdReuniao(int idReuniao) {
        this.idReuniao = idReuniao;
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

    public String getOrigemAnalise() {
        return origemAnalise;
    }

    public void setOrigemAnalise(String origemAnalise) {
        this.origemAnalise = origemAnalise;
    }

    public String getSinalNegocio() {
        return sinalNegocio;
    }

    public void setSinalNegocio(String sinalNegocio) {
        this.sinalNegocio = sinalNegocio;
    }

    public String getJustificativaSinal() {
        return justificativaSinal;
    }

    public void setJustificativaSinal(String justificativaSinal) {
        this.justificativaSinal = justificativaSinal;
    }

    public String getZonaNps() {
        return zonaNps;
    }

    public void setZonaNps(String zonaNps) {
        this.zonaNps = zonaNps;
    }

    public List<String> getMencoesTotvs() {
        return mencoesTotvs;
    }

    public void setMencoesTotvs(List<String> mencoesTotvs) {
        this.mencoesTotvs = mencoesTotvs;
    }

    public List<String> getMencoesConcorrentes() {
        return mencoesConcorrentes;
    }

    public void setMencoesConcorrentes(List<String> mencoesConcorrentes) {
        this.mencoesConcorrentes = mencoesConcorrentes;
    }
}
