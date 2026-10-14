package br.com.fiap.sellerfit.dto;

import java.util.List;

public class ResultadoAnalise {

    private AnaliseTranscricao analise;
    private List<MatchAtendente> matches;
    private RecomendacaoIA recomendacaoIA;

    public ResultadoAnalise() {
    }

    public ResultadoAnalise(AnaliseTranscricao analise, List<MatchAtendente> matches) {
        this.analise = analise;
        this.matches = matches;
    }

    public ResultadoAnalise(AnaliseTranscricao analise, List<MatchAtendente> matches, RecomendacaoIA recomendacaoIA) {
        this.analise = analise;
        this.matches = matches;
        this.recomendacaoIA = recomendacaoIA;
    }

    public AnaliseTranscricao getAnalise() {
        return analise;
    }

    public void setAnalise(AnaliseTranscricao analise) {
        this.analise = analise;
    }

    public List<MatchAtendente> getMatches() {
        return matches;
    }

    public void setMatches(List<MatchAtendente> matches) {
        this.matches = matches;
    }

    public RecomendacaoIA getRecomendacaoIA() {
        return recomendacaoIA;
    }

    public void setRecomendacaoIA(RecomendacaoIA recomendacaoIA) {
        this.recomendacaoIA = recomendacaoIA;
    }
}
