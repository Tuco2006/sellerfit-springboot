package br.com.fiap.sellerfit.entities;

public class Dor {

    private int id;
    private int idReuniao;
    private String descricao;
    private String nivelUrgencia;

    public Dor() {
        super();
    }

    public Dor(int id, int idReuniao, String descricao, String nivelUrgencia) {
        super();
        this.id = id;
        this.idReuniao = idReuniao;
        this.descricao = descricao;
        this.nivelUrgencia = nivelUrgencia;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNivelUrgencia() {
        return nivelUrgencia;
    }

    public void setNivelUrgencia(String nivelUrgencia) {
        this.nivelUrgencia = nivelUrgencia;
    }

    @Override
    public String toString() {
        return "Dor " +
                "\nid=" + id +
                "\nidReuniao=" + idReuniao +
                "\ndescricao='" + descricao + '\'' +
                "\nnivelUrgencia='" + nivelUrgencia + '\'';
    }
}
