package br.com.fiap.sellerfit.dao;

import br.com.fiap.sellerfit.conexoes.ConexaoFactory;
import br.com.fiap.sellerfit.entities.Match;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MatchDAO implements AutoCloseable {

    public Connection minhaConexao;

    private static final String COLUNAS = "ID_MATCH, ID_REUNIAO, ID_ATENDENTE, SCORE_COMPATIBILIDADE, MOTIVO, STATUS";

    public MatchDAO() throws SQLException, ClassNotFoundException {
        super();
        this.minhaConexao = new ConexaoFactory().conexao();
    }

    // Insert
    public int inserir(Match match) throws SQLException {
        PreparedStatement stmt = minhaConexao.prepareStatement
                ("Insert into T_FIAP_MATCH_SF (ID_REUNIAO, ID_ATENDENTE, SCORE_COMPATIBILIDADE, MOTIVO, STATUS) values (?, ?, ?, ?, ?)", new String[]{"ID_MATCH"});
        stmt.setInt(1, match.getIdReuniao());
        stmt.setInt(2, match.getIdAtendente());
        stmt.setDouble(3, match.getScoreCompatibilidade());
        stmt.setString(4, match.getMotivo());
        stmt.setString(5, match.getStatus());

        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            match.setId(rs.getInt(1));
        }
        rs.close();
        stmt.close();

        return match.getId();
    }

    // Delete
    public int deletar(int id) throws SQLException {
        PreparedStatement stmt = minhaConexao.prepareStatement
                ("Delete from T_FIAP_MATCH_SF where ID_MATCH = ?");
        stmt.setInt(1, id);

        int linhas = stmt.executeUpdate();
        stmt.close();

        return linhas;
    }

    // Update
    public int atualizar(Match match) throws SQLException {
        PreparedStatement stmt = minhaConexao.prepareStatement
                ("Update T_FIAP_MATCH_SF set ID_REUNIAO = ?, ID_ATENDENTE = ?, SCORE_COMPATIBILIDADE = ?, MOTIVO = ?, STATUS = ? where ID_MATCH = ?");
        stmt.setInt(1, match.getIdReuniao());
        stmt.setInt(2, match.getIdAtendente());
        stmt.setDouble(3, match.getScoreCompatibilidade());
        stmt.setString(4, match.getMotivo());
        stmt.setString(5, match.getStatus());
        stmt.setInt(6, match.getId());

        int linhas = stmt.executeUpdate();
        stmt.close();

        return linhas;
    }

    // Select
    public List<Match> selecionar() throws SQLException {
        List<Match> listaMatches = new ArrayList<Match>();
        PreparedStatement stmt = minhaConexao.prepareStatement
                ("select " + COLUNAS + " from T_FIAP_MATCH_SF order by ID_MATCH");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            listaMatches.add(montarMatch(rs));
        }
        rs.close();
        stmt.close();

        return listaMatches;
    }

    // Select dos matches de uma reuniao, do maior score pro menor
    public List<Match> selecionarPorReuniao(int idReuniao) throws SQLException {
        List<Match> listaMatches = new ArrayList<Match>();
        PreparedStatement stmt = minhaConexao.prepareStatement
                ("select " + COLUNAS + " from T_FIAP_MATCH_SF where ID_REUNIAO = ? order by SCORE_COMPATIBILIDADE desc");
        stmt.setInt(1, idReuniao);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            listaMatches.add(montarMatch(rs));
        }
        rs.close();
        stmt.close();

        return listaMatches;
    }

    // Buscar por id
    public Match buscarPorId(int id) throws SQLException {
        Match match = null;

        PreparedStatement stmt = minhaConexao.prepareStatement
                ("select " + COLUNAS + " from T_FIAP_MATCH_SF where ID_MATCH = ?");
        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            match = montarMatch(rs);
        }
        rs.close();
        stmt.close();

        return match;
    }

    private Match montarMatch(ResultSet rs) throws SQLException {
        Match match = new Match();
        match.setId(rs.getInt(1));
        match.setIdReuniao(rs.getInt(2));
        match.setIdAtendente(rs.getInt(3));
        match.setScoreCompatibilidade(rs.getDouble(4));
        match.setMotivo(rs.getString(5));
        match.setStatus(rs.getString(6));
        return match;
    }

    @Override
    public void close() throws SQLException {
        if (minhaConexao != null && !minhaConexao.isClosed()) {
            minhaConexao.close();
        }
    }
}
