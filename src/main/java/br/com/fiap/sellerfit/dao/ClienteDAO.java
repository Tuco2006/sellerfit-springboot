package br.com.fiap.sellerfit.dao;

import br.com.fiap.sellerfit.conexoes.ConexaoFactory;
import br.com.fiap.sellerfit.entities.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements AutoCloseable {

    public Connection minhaConexao;

    public ClienteDAO() throws SQLException, ClassNotFoundException {
        super();
        this.minhaConexao = new ConexaoFactory().conexao();
    }

    // Insert
    public int inserir(Cliente cliente) throws SQLException {
        PreparedStatement stmt = minhaConexao.prepareStatement
                ("Insert into T_FIAP_CLIENTE_SF (NOME, EMPRESA, SEGMENTO, ORCAMENTO) values (?, ?, ?, ?)", new String[]{"ID_CLIENTE"});
        stmt.setString(1, cliente.getNome());
        stmt.setString(2, cliente.getEmpresa());
        stmt.setString(3, cliente.getSegmento());
        stmt.setDouble(4, cliente.getOrcamento());

        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            cliente.setId(rs.getInt(1));
        }
        rs.close();
        stmt.close();

        return cliente.getId();
    }

    // Delete
    public int deletar(int id) throws SQLException {
        PreparedStatement stmt = minhaConexao.prepareStatement
                ("Delete from T_FIAP_CLIENTE_SF where ID_CLIENTE = ?");
        stmt.setInt(1, id);

        int linhas = stmt.executeUpdate();
        stmt.close();

        return linhas;
    }

    // Update
    public int atualizar(Cliente cliente) throws SQLException {
        PreparedStatement stmt = minhaConexao.prepareStatement
                ("Update T_FIAP_CLIENTE_SF set NOME = ?, EMPRESA = ?, SEGMENTO = ?, ORCAMENTO = ? where ID_CLIENTE = ?");
        stmt.setString(1, cliente.getNome());
        stmt.setString(2, cliente.getEmpresa());
        stmt.setString(3, cliente.getSegmento());
        stmt.setDouble(4, cliente.getOrcamento());
        stmt.setInt(5, cliente.getId());

        int linhas = stmt.executeUpdate();
        stmt.close();

        return linhas;
    }

    // Select
    public List<Cliente> selecionar() throws SQLException {
        List<Cliente> listaClientes = new ArrayList<Cliente>();
        PreparedStatement stmt = minhaConexao.prepareStatement
                ("select ID_CLIENTE, NOME, EMPRESA, SEGMENTO, ORCAMENTO from T_FIAP_CLIENTE_SF order by ID_CLIENTE");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            listaClientes.add(montarCliente(rs));
        }
        rs.close();
        stmt.close();

        return listaClientes;
    }

    // Buscar por id
    public Cliente buscarPorId(int id) throws SQLException {
        Cliente cliente = null;

        PreparedStatement stmt = minhaConexao.prepareStatement
                ("select ID_CLIENTE, NOME, EMPRESA, SEGMENTO, ORCAMENTO from T_FIAP_CLIENTE_SF where ID_CLIENTE = ?");
        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            cliente = montarCliente(rs);
        }
        rs.close();
        stmt.close();

        return cliente;
    }

    // Buscar pelo nome e empresa (usado pra nao duplicar cliente na analise)
    public Cliente buscarPorNomeEmpresa(String nome, String empresa) throws SQLException {
        Cliente cliente = null;

        PreparedStatement stmt = minhaConexao.prepareStatement
                ("select ID_CLIENTE, NOME, EMPRESA, SEGMENTO, ORCAMENTO from T_FIAP_CLIENTE_SF where upper(NOME) = upper(?) and upper(EMPRESA) = upper(?)");
        stmt.setString(1, nome);
        stmt.setString(2, empresa);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            cliente = montarCliente(rs);
        }
        rs.close();
        stmt.close();

        return cliente;
    }

    private Cliente montarCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt(1));
        cliente.setNome(rs.getString(2));
        cliente.setEmpresa(rs.getString(3));
        cliente.setSegmento(rs.getString(4));
        cliente.setOrcamento(rs.getDouble(5));
        return cliente;
    }

    @Override
    public void close() throws SQLException {
        if (minhaConexao != null && !minhaConexao.isClosed()) {
            minhaConexao.close();
        }
    }
}
