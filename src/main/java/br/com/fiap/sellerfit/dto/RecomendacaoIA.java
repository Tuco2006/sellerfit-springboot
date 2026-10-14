package br.com.fiap.sellerfit.dto;

import br.com.fiap.sellerfit.entities.Atendente;

public class RecomendacaoIA {

    private Atendente atendente;
    private String justificativa;

    public RecomendacaoIA() {
    }

    public RecomendacaoIA(Atendente atendente, String justificativa) {
        this.atendente = atendente;
        this.justificativa = justificativa;
    }

    public Atendente getAtendente() {
        return atendente;
    }

    public void setAtendente(Atendente atendente) {
        this.atendente = atendente;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }
}
