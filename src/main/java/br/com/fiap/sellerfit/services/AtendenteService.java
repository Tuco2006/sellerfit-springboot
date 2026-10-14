package br.com.fiap.sellerfit.services;

import br.com.fiap.sellerfit.dao.AtendenteDAO;
import br.com.fiap.sellerfit.entities.Atendente;
import br.com.fiap.sellerfit.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.sellerfit.excecoes.RegraNegocioException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AtendenteService {

    private static final List<String> DISPONIBILIDADES = List.of("Disponível", "Em atendimento", "Ausente");
    private static final List<String> FOCOS = List.of("retenção", "cross-sell");

    // Selecionar
    public List<Atendente> selecionar() throws SQLException, ClassNotFoundException {
        try (AtendenteDAO atendenteDAO = new AtendenteDAO()) {
            return atendenteDAO.selecionar();
        }
    }

    // Buscar por id
    public Atendente buscarPorId(int id) throws SQLException, ClassNotFoundException {
        try (AtendenteDAO atendenteDAO = new AtendenteDAO()) {
            Atendente atendente = atendenteDAO.buscarPorId(id);

            if (atendente == null) {
                throw new RecursoNaoEncontradoException("Atendente " + id + " não encontrado");
            }
            return atendente;
        }
    }

    // Inserir
    public Atendente inserir(Atendente atendente) throws SQLException, ClassNotFoundException {
        validar(atendente);

        try (AtendenteDAO atendenteDAO = new AtendenteDAO()) {
            atendenteDAO.inserir(atendente);
            return atendente;
        }
    }

    // Atualizar
    public Atendente atualizar(int id, Atendente atendente) throws SQLException, ClassNotFoundException {
        validar(atendente);
        atendente.setId(id);

        try (AtendenteDAO atendenteDAO = new AtendenteDAO()) {
            if (atendenteDAO.atualizar(atendente) == 0) {
                throw new RecursoNaoEncontradoException("Atendente " + id + " não encontrado");
            }
            return atendente;
        }
    }

    // Deletar
    public void deletar(int id) throws SQLException, ClassNotFoundException {
        try (AtendenteDAO atendenteDAO = new AtendenteDAO()) {
            if (atendenteDAO.deletar(id) == 0) {
                throw new RecursoNaoEncontradoException("Atendente " + id + " não encontrado");
            }
        }
    }

    // Regra de negocios
    private void validar(Atendente atendente) {
        if (atendente == null) {
            throw new RegraNegocioException("Dados do atendente não informados");
        }
        if (vazio(atendente.getNome()) || atendente.getNome().trim().length() < 3) {
            throw new RegraNegocioException("O nome do atendente deve ter pelo menos 3 caracteres");
        }
        if (atendente.getNome().trim().length() > 100) {
            throw new RegraNegocioException("O nome do atendente deve ter no máximo 100 caracteres");
        }
        if (!vazio(atendente.getEmail()) && !atendente.getEmail().matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
            throw new RegraNegocioException("E-mail do atendente inválido");
        }
        if (!vazio(atendente.getEmail()) && atendente.getEmail().length() > 100) {
            throw new RegraNegocioException("O e-mail do atendente deve ter no máximo 100 caracteres");
        }
        if (vazio(atendente.getCargo())) {
            throw new RegraNegocioException("Informe o cargo do atendente");
        }
        if (atendente.getCargo().length() > 80) {
            throw new RegraNegocioException("O cargo do atendente deve ter no máximo 80 caracteres");
        }
        if (atendente.getBio() != null && atendente.getBio().length() > 400) {
            throw new RegraNegocioException("A bio do atendente deve ter no máximo 400 caracteres");
        }
        if (atendente.getTracos() == null || atendente.getTracos().isEmpty()) {
            throw new RegraNegocioException("Informe pelo menos um traço de comportamento do atendente");
        }
        if (atendente.getSegmentos() == null || atendente.getSegmentos().isEmpty()) {
            throw new RegraNegocioException("Informe pelo menos um segmento de atuação do atendente");
        }
        if (String.join(",", atendente.getTracos()).length() > 200 || String.join(",", atendente.getSegmentos()).length() > 200) {
            throw new RegraNegocioException("A lista de traços e a de segmentos devem ter no máximo 200 caracteres cada");
        }
        if (atendente.getFoco() == null) {
            atendente.setFoco(new ArrayList<String>());
        }
        for (String foco : atendente.getFoco()) {
            if (!FOCOS.contains(foco)) {
                throw new RegraNegocioException("Foco inválido: " + foco + ". Use retenção ou cross-sell");
            }
        }
        if (atendente.getNotaMedia() < 0 || atendente.getNotaMedia() > 10) {
            throw new RegraNegocioException("A nota média deve estar entre 0 e 10");
        }
        if (atendente.getAnosExperiencia() < 0 || atendente.getCasesResolvidos() < 0) {
            throw new RegraNegocioException("Anos de experiência e cases resolvidos não podem ser negativos");
        }
        if (atendente.getAnosExperiencia() > 99 || atendente.getCasesResolvidos() > 99999) {
            throw new RegraNegocioException("Anos de experiência deve ser até 99 e cases resolvidos até 99999");
        }
        if (vazio(atendente.getDisponibilidade())) {
            atendente.setDisponibilidade("Disponível");
        }
        if (!DISPONIBILIDADES.contains(atendente.getDisponibilidade())) {
            throw new RegraNegocioException("Disponibilidade inválida. Use Disponível, Em atendimento ou Ausente");
        }
        if (vazio(atendente.getAvatarCor())) {
            atendente.setAvatarCor("#2563eb");
        }
        if (!atendente.getAvatarCor().matches("^#[0-9a-fA-F]{6}$")) {
            throw new RegraNegocioException("A cor do avatar deve estar no formato #RRGGBB");
        }
        if (vazio(atendente.getIniciais())) {
            atendente.setIniciais(gerarIniciais(atendente.getNome()));
        }
    }

    // pega a primeira letra do primeiro e do ultimo nome
    private String gerarIniciais(String nome) {
        String[] partes = nome.trim().split("\\s+");
        String iniciais = partes[0].substring(0, 1);

        if (partes.length > 1) {
            iniciais += partes[partes.length - 1].substring(0, 1);
        }
        return iniciais.toUpperCase();
    }

    private boolean vazio(String valor) {
        return valor == null || valor.isBlank();
    }
}
