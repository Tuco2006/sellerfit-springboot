package br.com.fiap.sellerfit.services;

import br.com.fiap.sellerfit.dao.ClienteDAO;
import br.com.fiap.sellerfit.entities.Cliente;
import br.com.fiap.sellerfit.excecoes.RecursoNaoEncontradoException;
import br.com.fiap.sellerfit.excecoes.RegraNegocioException;

import java.sql.SQLException;
import java.util.List;

public class ClienteService {

    // Selecionar
    public List<Cliente> selecionar() throws SQLException, ClassNotFoundException {
        try (ClienteDAO clienteDAO = new ClienteDAO()) {
            return clienteDAO.selecionar();
        }
    }

    // Buscar por id
    public Cliente buscarPorId(int id) throws SQLException, ClassNotFoundException {
        try (ClienteDAO clienteDAO = new ClienteDAO()) {
            Cliente cliente = clienteDAO.buscarPorId(id);

            if (cliente == null) {
                throw new RecursoNaoEncontradoException("Cliente " + id + " não encontrado");
            }
            return cliente;
        }
    }

    // Inserir
    public Cliente inserir(Cliente cliente) throws SQLException, ClassNotFoundException {
        validar(cliente);

        try (ClienteDAO clienteDAO = new ClienteDAO()) {
            clienteDAO.inserir(cliente);
            return cliente;
        }
    }

    // Atualizar
    public Cliente atualizar(int id, Cliente cliente) throws SQLException, ClassNotFoundException {
        validar(cliente);
        cliente.setId(id);

        try (ClienteDAO clienteDAO = new ClienteDAO()) {
            if (clienteDAO.atualizar(cliente) == 0) {
                throw new RecursoNaoEncontradoException("Cliente " + id + " não encontrado");
            }
            return cliente;
        }
    }

    // Deletar
    public void deletar(int id) throws SQLException, ClassNotFoundException {
        try (ClienteDAO clienteDAO = new ClienteDAO()) {
            if (clienteDAO.deletar(id) == 0) {
                throw new RecursoNaoEncontradoException("Cliente " + id + " não encontrado");
            }
        }
    }

    // Regra de negocios
    private void validar(Cliente cliente) {
        if (cliente == null) {
            throw new RegraNegocioException("Dados do cliente não informados");
        }
        if (vazio(cliente.getNome()) || cliente.getNome().trim().length() < 3) {
            throw new RegraNegocioException("O nome do cliente deve ter pelo menos 3 caracteres");
        }
        if (cliente.getNome().length() > 100) {
            throw new RegraNegocioException("O nome do cliente deve ter no máximo 100 caracteres");
        }
        if (vazio(cliente.getEmpresa())) {
            throw new RegraNegocioException("Informe a empresa do cliente");
        }
        if (cliente.getEmpresa().trim().length() > 100) {
            throw new RegraNegocioException("O nome da empresa deve ter no máximo 100 caracteres");
        }
        if (vazio(cliente.getSegmento())) {
            throw new RegraNegocioException("Informe o segmento do cliente");
        }
        if (cliente.getSegmento().trim().length() > 50) {
            throw new RegraNegocioException("O segmento deve ter no máximo 50 caracteres");
        }
        if (cliente.getOrcamento() < 0) {
            throw new RegraNegocioException("O orçamento não pode ser negativo");
        }

        cliente.setNome(cliente.getNome().trim());
        cliente.setEmpresa(cliente.getEmpresa().trim());
        cliente.setSegmento(cliente.getSegmento().trim().toLowerCase());
    }

    private boolean vazio(String valor) {
        return valor == null || valor.isBlank();
    }
}
