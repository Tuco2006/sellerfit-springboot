package br.com.fiap.sellerfit.dao;

import br.com.fiap.sellerfit.conexoes.ConexaoFactory;
import br.com.fiap.sellerfit.entities.Reuniao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ReuniaoDAO implements AutoCloseable {

    public Connection minhaConexao;

    private static final String COLUNAS = "ID_REUNIAO, ID_CLIENTE, DATA_REUNIAO, TRANSCRICAO, NOTA_NPS, URGENCIA, SINAL_NEGOCIO, STATUS";

    public ReuniaoDAO() throws SQLException, ClassNotFoundException {
        super();
        this.minhaConexao = new ConexaoFactory().conexao();
    }

    // Insert
    public int inserir(Reuniao reuniao) throws SQLException {
        PreparedStatement stmt = minhaConexao.prepareStatement
                ("Insert into T_FIAP_REUNIAO_SF (ID_CLIENTE, DATA_REUNIAO, TRANSCRICAO, NOTA_NPS, URGENCIA, SINAL_NEGOCIO, STATUS) values (?, ?, ?, ?, ?, ?, ?)", new String[]{"ID_REUNIAO"});
        preencherCampos(stmt, reuniao);

        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            reuniao.setId(rs.getInt(1));
        }
        rs.close();
        stmt.close();

        return reuniao.getId();
    }

    // Delete
    public int deletar(int id) throws SQLException {
        PreparedStatement stmt = minhaConexao.prepareStatement
                ("Delete from T_FIAP_REUNIAO_SF where ID_REUNIAO = ?");
        stmt.setInt(1, id);

        int linhas = stmt.executeUpdate();
        stmt.close();

        return linhas;
    }

    // Update
    public int atualizar(Reuniao reuniao) throws SQLException {
        PreparedStatement stmt = minhaConexao.prepareStatement
                ("Update T_FIAP_REUNIAO_SF set ID_CLIENTE = ?, DATA_REUNIAO = ?, TRANSCRICAO = ?, NOTA_NPS = ?, URGENCIA = ?, SINAL_NEGOCIO = ?, STATUS = ? where ID_REUNIAO = ?");
        preencherCampos(stmt, reuniao);
        stmt.setInt(8, reuniao.getId());

        int linhas = stmt.executeUpdate();
        stmt.close();

        return linhas;
    }

    // Select
    public List<Reuniao> selecionar() throws SQLException {
        List<Reuniao> listaReunioes = new ArrayList<Reuniao>();
        PreparedStatement stmt = minhaConexao.prepareStatement
                ("select " + COLUNAS + " from T_FIAP_REUNIAO_SF order by ID_REUNIAO");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            listaReunioes.add(montarReuniao(rs));
        }
        rs.close();
        stmt.close();

        return listaReunioes;
    }

    // Select das reunioes de um cliente
    public List<Reuniao> selecionarPorCliente(int idCliente) throws SQLException {
        List<Reuniao> listaReunioes = new ArrayList<Reuniao>();
        PreparedStatement stmt = minhaConexao.prepareStatement
                ("select " + COLUNAS + " from T_FIAP_REUNIAO_SF where ID_CLIENTE = ? order by DATA_REUNIAO desc");
        stmt.setInt(1, idCliente);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            listaReunioes.add(montarReuniao(rs));
        }
        rs.close();
        stmt.close();

        return listaReunioes;
    }

    // Buscar por id
    public Reuniao buscarPorId(int id) throws SQLException {
        Reuniao reuniao = null;

        PreparedStatement stmt = minhaConexao.prepareStatement
                ("select " + COLUNAS + " from T_FIAP_REUNIAO_SF where ID_REUNIAO = ?");
        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            reuniao = montarReuniao(rs);
        }
        rs.close();
        stmt.close();

        return reuniao;
    }

    private void preencherCampos(PreparedStatement stmt, Reuniao reuniao) throws SQLException {
        stmt.setInt(1, reuniao.getIdCliente());
        stmt.setDate(2, Date.valueOf(reuniao.getDataReuniao()));
        stmt.setString(3, reuniao.getTranscricao());
        if (reuniao.getNotaNps() != null) {
            stmt.setInt(4, reuniao.getNotaNps());
        } else {
            stmt.setNull(4, Types.INTEGER);
        }
        stmt.setString(5, reuniao.getUrgencia());
        stmt.setString(6, reuniao.getSinalNegocio());
        stmt.setString(7, reuniao.getStatus());
    }

    private Reuniao montarReuniao(ResultSet rs) throws SQLException {
        Reuniao reuniao = new Reuniao();
        reuniao.setId(rs.getInt(1));
        reuniao.setIdCliente(rs.getInt(2));
        reuniao.setDataReuniao(rs.getDate(3).toLocalDate());
        reuniao.setTranscricao(rs.getString(4));
        int nota = rs.getInt(5);
        reuniao.setNotaNps(rs.wasNull() ? null : nota);
        reuniao.setUrgencia(rs.getString(6));
        reuniao.setSinalNegocio(rs.getString(7));
        reuniao.setStatus(rs.getString(8));
        return reuniao;
    }

    @Override
    public void close() throws SQLException {
        if (minhaConexao != null && !minhaConexao.isClosed()) {
            minhaConexao.close();
        }
    }
}
