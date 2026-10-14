package br.com.fiap.sellerfit.dto;

import br.com.fiap.sellerfit.entities.Atendente;

public class MatchAtendente {

    private Atendente atendente;
    private int score;
    private String motivo;

    public MatchAtendente() {
    }

    public MatchAtendente(Atendente atendente, int score, String motivo) {
        this.atendente = atendente;
        this.score = score;
        this.motivo = motivo;
    }

    public Atendente getAtendente() {
        return atendente;
    }

    public void setAtendente(Atendente atendente) {
        this.atendente = atendente;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
