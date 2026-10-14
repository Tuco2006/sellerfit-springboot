package br.com.fiap.sellerfit.services;

import br.com.fiap.sellerfit.dao.DorDAO;
import br.com.fiap.sellerfit.dao.ReuniaoDAO;
import br.com.fiap.sellerfit.entities.Dor;
import br.com.fiap.sellerfit.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.sellerfit.excecoes.RegraNegocioException;

import java.sql.SQLException;
import java.util.List;

public class DorService {

    private static final List<String> NIVEIS = List.of("BAIXA", "MÉDIA", "ALTA");

    // Selecionar
    public List<Dor> selecionar() throws SQLException, ClassNotFoundException {
        try (DorDAO dorDAO = new DorDAO()) {
            return dorDAO.selecionar();
        }
    }

    // Selecionar as dores de uma reuniao
    public List<Dor> selecionarPorReuniao(int idReuniao) throws SQLException, ClassNotFoundException {
        validarReuniao(idReuniao);

        try (DorDAO dorDAO = new DorDAO()) {
            return dorDAO.selecionarPorReuniao(idReuniao);
        }
    }

    // Buscar por id
    public Dor buscarPorId(int id) throws SQLException, ClassNotFoundException {
        try (DorDAO dorDAO = new DorDAO()) {
            Dor dor = dorDAO.buscarPorId(id);

            if (dor == null) {
                throw new RecursoNaoEncontradoException("Dor " + id + " não encontrada");
            }
            return dor;
        }
    }

    // Inserir
    public Dor inserir(Dor dor) throws SQLException, ClassNotFoundException {
        validar(dor);

        try (DorDAO dorDAO = new DorDAO()) {
            dorDAO.inserir(dor);
            return dor;
        }
    }

    // Atualizar
    public Dor atualizar(int id, Dor dor) throws SQLException, ClassNotFoundException {
        validar(dor);
        dor.setId(id);

        try (DorDAO dorDAO = new DorDAO()) {
            if (dorDAO.atualizar(dor) == 0) {
                throw new RecursoNaoEncontradoException("Dor " + id + " não encontrada");
            }
            return dor;
        }
    }

    // Deletar
    public void deletar(int id) throws SQLException, ClassNotFoundException {
        try (DorDAO dorDAO = new DorDAO()) {
            if (dorDAO.deletar(id) == 0) {
                throw new RecursoNaoEncontradoException("Dor " + id + " não encontrada");
            }
        }
    }

    // Regra de negocios
    private void validar(Dor dor) throws SQLException, ClassNotFoundException {
        if (dor == null) {
            throw new RegraNegocioException("Dados da dor não informados");
        }
        if (dor.getDescricao() == null || dor.getDescricao().trim().length() < 3) {
            throw new RegraNegocioException("Descreva a dor com pelo menos 3 caracteres");
        }
        if (dor.getDescricao().trim().length() > 200) {
            throw new RegraNegocioException("A descrição da dor deve ter no máximo 200 caracteres");
        }
        dor.setDescricao(dor.getDescricao().trim());
        if (dor.getNivelUrgencia() == null || !NIVEIS.contains(dor.getNivelUrgencia().toUpperCase())) {
            throw new RegraNegocioException("Nível de urgência inválido. Use BAIXA, MÉDIA ou ALTA");
        }
        dor.setNivelUrgencia(dor.getNivelUrgencia().toUpperCase());
        validarReuniao(dor.getIdReuniao());
    }

    private void validarReuniao(int idReuniao) throws SQLException, ClassNotFoundException {
        try (ReuniaoDAO reuniaoDAO = new ReuniaoDAO()) {
            if (reuniaoDAO.buscarPorId(idReuniao) == null) {
                throw new RegraNegocioException("Reunião " + idReuniao + " não cadastrada");
            }
        }
    }
}
