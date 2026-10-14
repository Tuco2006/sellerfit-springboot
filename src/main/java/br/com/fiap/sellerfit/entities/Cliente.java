package br.com.fiap.sellerfit.entities;

public class Cliente {

    private int id;
    private String nome;
    private String empresa;
    private String segmento;
    private double orcamento;

    public Cliente() {
        super();
    }

    public Cliente(int id, String nome, String empresa, String segmento, double orcamento) {
        super();
        this.id = id;
        this.nome = nome;
        this.empresa = empresa;
        this.segmento = segmento;
        this.orcamento = orcamento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public double getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(double orcamento) {
        this.orcamento = orcamento;
    }

    @Override
    public String toString() {
        return "Cliente " +
                "\nid=" + id +
                "\nnome='" + nome + '\'' +
                "\nempresa='" + empresa + '\'' +
                "\nsegmento='" + segmento + '\'' +
                "\norcamento=" + orcamento;
    }
}
