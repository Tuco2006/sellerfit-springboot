package br.com.fiap.sellerfit.dao;

import br.com.fiap.sellerfit.conexoes.ConexaoFactory;
import br.com.fiap.sellerfit.entities.Dor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DorDAO implements AutoCloseable {

    public Connection minhaConexao;

    public DorDAO() throws SQLException, ClassNotFoundException {
        super();
        this.minhaConexao = new ConexaoFactory().conexao();
    }

    // Insert
    public int inserir(Dor dor) throws SQLException {
        PreparedStatement stmt = minhaConexao.prepareStatement
                ("Insert into T_FIAP_DOR_SF (ID_REUNIAO, DESCRICAO, NIVEL_URGENCIA) values (?, ?, ?)", new String[]{"ID_DOR"});
        stmt.setInt(1, dor.getIdReuniao());
        stmt.setString(2, dor.getDescricao());
        stmt.setString(3, dor.getNivelUrgencia());

        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            dor.setId(rs.getInt(1));
        }
        rs.close();
        stmt.close();

        return dor.getId();
    }

    // Delete
    public int deletar(int id) throws SQLException {
        PreparedStatement stmt = minhaConexao.prepareStatement
                ("Delete from T_FIAP_DOR_SF where ID_DOR = ?");
        stmt.setInt(1, id);

        int linhas = stmt.executeUpdate();
        stmt.close();

        return linhas;
    }

    // Update
    public int atualizar(Dor dor) throws SQLException {
        PreparedStatement stmt = minhaConexao.prepareStatement
                ("Update T_FIAP_DOR_SF set ID_REUNIAO = ?, DESCRICAO = ?, NIVEL_URGENCIA = ? where ID_DOR = ?");
        stmt.setInt(1, dor.getIdReuniao());
        stmt.setString(2, dor.getDescricao());
        stmt.setString(3, dor.getNivelUrgencia());
        stmt.setInt(4, dor.getId());

        int linhas = stmt.executeUpdate();
        stmt.close();

        return linhas;
    }

    // Select
    public List<Dor> selecionar() throws SQLException {
        List<Dor> listaDores = new ArrayList<Dor>();
        PreparedStatement stmt = minhaConexao.prepareStatement
                ("select ID_DOR, ID_REUNIAO, DESCRICAO, NIVEL_URGENCIA from T_FIAP_DOR_SF order by ID_DOR");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            listaDores.add(montarDor(rs));
        }
        rs.close();
        stmt.close();

        return listaDores;
    }

    // Select das dores de uma reuniao
    public List<Dor> selecionarPorReuniao(int idReuniao) throws SQLException {
        List<Dor> listaDores = new ArrayList<Dor>();
        PreparedStatement stmt = minhaConexao.prepareStatement
                ("select ID_DOR, ID_REUNIAO, DESCRICAO, NIVEL_URGENCIA from T_FIAP_DOR_SF where ID_REUNIAO = ? order by ID_DOR");
        stmt.setInt(1, idReuniao);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            listaDores.add(montarDor(rs));
        }
        rs.close();
        stmt.close();

        return listaDores;
    }

    // Buscar por id
    public Dor buscarPorId(int id) throws SQLException {
        Dor dor = null;

        PreparedStatement stmt = minhaConexao.prepareStatement
                ("select ID_DOR, ID_REUNIAO, DESCRICAO, NIVEL_URGENCIA from T_FIAP_DOR_SF where ID_DOR = ?");
        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            dor = montarDor(rs);
        }
        rs.close();
        stmt.close();

        return dor;
    }

    private Dor montarDor(ResultSet rs) throws SQLException {
        Dor dor = new Dor();
        dor.setId(rs.getInt(1));
        dor.setIdReuniao(rs.getInt(2));
        dor.setDescricao(rs.getString(3));
        dor.setNivelUrgencia(rs.getString(4));
        return dor;
    }

    @Override
    public void close() throws SQLException {
        if (minhaConexao != null && !minhaConexao.isClosed()) {
            minhaConexao.close();
        }
    }
}
