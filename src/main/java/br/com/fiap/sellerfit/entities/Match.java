package br.com.fiap.sellerfit.entities;

public class Match {

    private int id;
    private int idReuniao;
    private int idAtendente;
    private double scoreCompatibilidade;
    private String motivo;
    private String status;

    public Match() {
        super();
    }

    public Match(int id, int idReuniao, int idAtendente, double scoreCompatibilidade, String motivo, String status) {
        super();
        this.id = id;
        this.idReuniao = idReuniao;
        this.idAtendente = idAtendente;
        this.scoreCompatibilidade = scoreCompatibilidade;
        this.motivo = motivo;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdReuniao() {
        return idReuniao;
    }

    public void setIdReuniao(int idReuniao) {
        this.idReuniao = idReuniao;
    }

    public int getIdAtendente() {
        return idAtendente;
    }

    public void setIdAtendente(int idAtendente) {
        this.idAtendente = idAtendente;
    }

    public double getScoreCompatibilidade() {
        return scoreCompatibilidade;
    }

    public void setScoreCompatibilidade(double scoreCompatibilidade) {
        this.scoreCompatibilidade = scoreCompatibilidade;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Match " +
                "\nid=" + id +
                "\nidReuniao=" + idReuniao +
                "\nidAtendente=" + idAtendente +
                "\nscoreCompatibilidade=" + scoreCompatibilidade +
                "\nmotivo='" + motivo + '\'' +
                "\nstatus='" + status + '\'';
    }
}
