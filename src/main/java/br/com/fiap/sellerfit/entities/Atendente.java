package br.com.fiap.sellerfit.entities;

import java.util.ArrayList;
import java.util.List;

public class Atendente {

    private int id;
    private String nome;
    private String email;
    private String cargo;
    private String avatarCor;
    private String iniciais;
    private List<String> tracos = new ArrayList<String>();
    private List<String> segmentos = new ArrayList<String>();
    private List<String> foco = new ArrayList<String>();
    private String bio;
    private int anosExperiencia;
    private double notaMedia;
    private int casesResolvidos;
    private String disponibilidade;

    public Atendente() {
        super();
    }

    public Atendente(int id, String nome, String email, String cargo, String avatarCor, String iniciais, List<String> tracos, List<String> segmentos, List<String> foco, String bio, int anosExperiencia, double notaMedia, int casesResolvidos, String disponibilidade) {
        super();
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cargo = cargo;
        this.avatarCor = avatarCor;
        this.iniciais = iniciais;
        this.tracos = tracos;
        this.segmentos = segmentos;
        this.foco = foco;
        this.bio = bio;
        this.anosExperiencia = anosExperiencia;
        this.notaMedia = notaMedia;
        this.casesResolvidos = casesResolvidos;
        this.disponibilidade = disponibilidade;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getAvatarCor() {
        return avatarCor;
    }

    public void setAvatarCor(String avatarCor) {
        this.avatarCor = avatarCor;
    }

    public String getIniciais() {
        return iniciais;
    }

    public void setIniciais(String iniciais) {
        this.iniciais = iniciais;
    }

    public List<String> getTracos() {
        return tracos;
    }

    public void setTracos(List<String> tracos) {
        this.tracos = tracos;
    }

    public List<String> getSegmentos() {
        return segmentos;
    }

    public void setSegmentos(List<String> segmentos) {
        this.segmentos = segmentos;
    }

    public List<String> getFoco() {
        return foco;
    }

    public void setFoco(List<String> foco) {
        this.foco = foco;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getAnosExperiencia() {
        return anosExperiencia;
    }

    public void setAnosExperiencia(int anosExperiencia) {
        this.anosExperiencia = anosExperiencia;
    }

    public double getNotaMedia() {
        return notaMedia;
    }

    public void setNotaMedia(double notaMedia) {
        this.notaMedia = notaMedia;
    }

    public int getCasesResolvidos() {
        return casesResolvidos;
    }

    public void setCasesResolvidos(int casesResolvidos) {
        this.casesResolvidos = casesResolvidos;
    }

    public String getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(String disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    @Override
    public String toString() {
        return "Atendente " +
                "\nid=" + id +
                "\nnome='" + nome + '\'' +
                "\ncargo='" + cargo + '\'' +
                "\ntracos=" + tracos +
                "\nsegmentos=" + segmentos +
                "\nfoco=" + foco +
                "\nnotaMedia=" + notaMedia +
                "\ndisponibilidade='" + disponibilidade + '\'';
    }
}
