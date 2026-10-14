package br.com.fiap.sellerfit.entities;

import java.time.LocalDate;

public class Reuniao {

    private int id;
    private int idCliente;
    private LocalDate dataReuniao;
    private String transcricao;
    private Integer notaNps;
    private String urgencia;
    private String sinalNegocio;
    private String status;

    public Reuniao() {
        super();
    }

    public Reuniao(int id, int idCliente, LocalDate dataReuniao, String transcricao, Integer notaNps, String urgencia, String sinalNegocio, String status) {
        super();
        this.id = id;
        this.idCliente = idCliente;
        this.dataReuniao = dataReuniao;
        this.transcricao = transcricao;
        this.notaNps = notaNps;
        this.urgencia = urgencia;
        this.sinalNegocio = sinalNegocio;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public LocalDate getDataReuniao() {
        return dataReuniao;
    }

    public void setDataReuniao(LocalDate dataReuniao) {
        this.dataReuniao = dataReuniao;
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

    public String getUrgencia() {
        return urgencia;
    }

    public void setUrgencia(String urgencia) {
        this.urgencia = urgencia;
    }

    public String getSinalNegocio() {
        return sinalNegocio;
    }

    public void setSinalNegocio(String sinalNegocio) {
        this.sinalNegocio = sinalNegocio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reuniao " +
                "\nid=" + id +
                "\nidCliente=" + idCliente +
                "\ndataReuniao=" + dataReuniao +
                "\nnotaNps=" + notaNps +
                "\nurgencia='" + urgencia + '\'' +
                "\nsinalNegocio='" + sinalNegocio + '\'' +
                "\nstatus='" + status + '\'';
    }
}
