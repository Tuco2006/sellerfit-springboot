package br.com.fiap.sellerfit.dto;

public class EntradaAnalise {

    private String clienteNome;
    private String empresa;
    private String segmento;
    private String transcricao;
    private Integer notaNps;

    public EntradaAnalise() {
    }

    public EntradaAnalise(String clienteNome, String empresa, String segmento, String transcricao, Integer notaNps) {
        this.clienteNome = clienteNome;
        this.empresa = empresa;
        this.segmento = segmento;
        this.transcricao = transcricao;
        this.notaNps = notaNps;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getSegmento() {
        return segmento;
    }

    public void setSegmento(String segmento) {
        this.segmento = segmento;
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
}
