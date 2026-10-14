package br.com.fiap.sellerfit.services;

import br.com.fiap.sellerfit.dao.AtendenteDAO;
import br.com.fiap.sellerfit.dao.MatchDAO;
import br.com.fiap.sellerfit.dao.ReuniaoDAO;
import br.com.fiap.sellerfit.entities.Match;
import br.com.fiap.sellerfit.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.sellerfit.excecoes.RegraNegocioException;

import java.sql.SQLException;
import java.util.List;

public class MatchService {

    private static final List<String> STATUS = List.of("SUGERIDO", "ACEITO", "RECUSADO");

    // Selecionar
    public List<Match> selecionar() throws SQLException, ClassNotFoundException {
        try (MatchDAO matchDAO = new MatchDAO()) {
            return matchDAO.selecionar();
        }
    }

    // Selecionar os matches de uma reuniao
    public List<Match> selecionarPorReuniao(int idReuniao) throws SQLException, ClassNotFoundException {
        validarReuniao(idReuniao);

        try (MatchDAO matchDAO = new MatchDAO()) {
            return matchDAO.selecionarPorReuniao(idReuniao);
        }
    }

    // Buscar por id
    public Match buscarPorId(int id) throws SQLException, ClassNotFoundException {
        try (MatchDAO matchDAO = new MatchDAO()) {
            Match match = matchDAO.buscarPorId(id);

            if (match == null) {
                throw new RecursoNaoEncontradoException("Match " + id + " não encontrado");
            }
            return match;
        }
    }

    // Inserir
    public Match inserir(Match match) throws SQLException, ClassNotFoundException {
        validar(match);

        try (MatchDAO matchDAO = new MatchDAO()) {
            matchDAO.inserir(match);
            return match;
        }
    }

    // Atualizar
    public Match atualizar(int id, Match match) throws SQLException, ClassNotFoundException {
        validar(match);
        match.setId(id);

        try (MatchDAO matchDAO = new MatchDAO()) {
            if (matchDAO.atualizar(match) == 0) {
                throw new RecursoNaoEncontradoException("Match " + id + " não encontrado");
            }
            return match;
        }
    }

    // Deletar
    public void deletar(int id) throws SQLException, ClassNotFoundException {
        try (MatchDAO matchDAO = new MatchDAO()) {
            if (matchDAO.deletar(id) == 0) {
                throw new RecursoNaoEncontradoException("Match " + id + " não encontrado");
            }
        }
    }

    // Regra de negocios
    private void validar(Match match) throws SQLException, ClassNotFoundException {
        if (match == null) {
            throw new RegraNegocioException("Dados do match não informados");
        }
        if (match.getScoreCompatibilidade() < 0 || match.getScoreCompatibilidade() > 100) {
            throw new RegraNegocioException("O score de compatibilidade deve estar entre 0 e 100");
        }
        if (match.getMotivo() != null && match.getMotivo().length() > 400) {
            throw new RegraNegocioException("O motivo do match deve ter no máximo 400 caracteres");
        }
        if (match.getStatus() == null) {
            match.setStatus("SUGERIDO");
        }
        if (!STATUS.contains(match.getStatus())) {
            throw new RegraNegocioException("Status inválido. Use SUGERIDO, ACEITO ou RECUSADO");
        }
        validarReuniao(match.getIdReuniao());

        try (AtendenteDAO atendenteDAO = new AtendenteDAO()) {
            if (atendenteDAO.buscarPorId(match.getIdAtendente()) == null) {
                throw new RegraNegocioException("Atendente " + match.getIdAtendente() + " não cadastrado");
            }
        }
    }

    private void validarReuniao(int idReuniao) throws SQLException, ClassNotFoundException {
        try (ReuniaoDAO reuniaoDAO = new ReuniaoDAO()) {
            if (reuniaoDAO.buscarPorId(idReuniao) == null) {
                throw new RegraNegocioException("Reunião " + idReuniao + " não cadastrada");
            }
        }
    }
}
