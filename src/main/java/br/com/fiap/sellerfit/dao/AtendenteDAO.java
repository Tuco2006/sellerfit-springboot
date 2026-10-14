package br.com.fiap.sellerfit.dao;

import br.com.fiap.sellerfit.conexoes.ConexaoFactory;
import br.com.fiap.sellerfit.entities.Atendente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AtendenteDAO implements AutoCloseable {

    public Connection minhaConexao;

    private static final String COLUNAS = "ID_ATENDENTE, NOME, EMAIL, CARGO, COR_AVATAR, INICIAIS, TRACOS, SEGMENTOS, FOCO, BIO, ANOS_EXPERIENCIA, NOTA_MEDIA, CASES_RESOLVIDOS, DISPONIBILIDADE";

    public AtendenteDAO() throws SQLException, ClassNotFoundException {
        super();
        this.minhaConexao = new ConexaoFactory().conexao();
    }

    // Insert
    public int inserir(Atendente atendente) throws SQLException {
        PreparedStatement stmt = minhaConexao.prepareStatement
                ("Insert into T_FIAP_ATENDENTE_SF (NOME, EMAIL, CARGO, COR_AVATAR, INICIAIS, TRACOS, SEGMENTOS, FOCO, BIO, ANOS_EXPERIENCIA, NOTA_MEDIA, CASES_RESOLVIDOS, DISPONIBILIDADE) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new String[]{"ID_ATENDENTE"});
        preencherCampos(stmt, atendente);

        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            atendente.setId(rs.getInt(1));
        }
        rs.close();
        stmt.close();

        return atendente.getId();
    }

    // Delete
    public int deletar(int id) throws SQLException {
        PreparedStatement stmt = minhaConexao.prepareStatement
                ("Delete from T_FIAP_ATENDENTE_SF where ID_ATENDENTE = ?");
        stmt.setInt(1, id);

        int linhas = stmt.executeUpdate();
        stmt.close();

        return linhas;
    }

    // Update
    public int atualizar(Atendente atendente) throws SQLException {
        PreparedStatement stmt = minhaConexao.prepareStatement
                ("Update T_FIAP_ATENDENTE_SF set NOME = ?, EMAIL = ?, CARGO = ?, COR_AVATAR = ?, INICIAIS = ?, TRACOS = ?, SEGMENTOS = ?, FOCO = ?, BIO = ?, ANOS_EXPERIENCIA = ?, NOTA_MEDIA = ?, CASES_RESOLVIDOS = ?, DISPONIBILIDADE = ? where ID_ATENDENTE = ?");
        preencherCampos(stmt, atendente);
        stmt.setInt(14, atendente.getId());

        int linhas = stmt.executeUpdate();
        stmt.close();

        return linhas;
    }

    // Select
    public List<Atendente> selecionar() throws SQLException {
        List<Atendente> listaAtendentes = new ArrayList<Atendente>();
        PreparedStatement stmt = minhaConexao.prepareStatement
                ("select " + COLUNAS + " from T_FIAP_ATENDENTE_SF order by ID_ATENDENTE");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            listaAtendentes.add(montarAtendente(rs));
        }
        rs.close();
        stmt.close();

        return listaAtendentes;
    }

    // Buscar por id
    public Atendente buscarPorId(int id) throws SQLException {
        Atendente atendente = null;

        PreparedStatement stmt = minhaConexao.prepareStatement
                ("select " + COLUNAS + " from T_FIAP_ATENDENTE_SF where ID_ATENDENTE = ?");
        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            atendente = montarAtendente(rs);
        }
        rs.close();
        stmt.close();

        return atendente;
    }

    private void preencherCampos(PreparedStatement stmt, Atendente atendente) throws SQLException {
        stmt.setString(1, atendente.getNome());
        stmt.setString(2, atendente.getEmail());
        stmt.setString(3, atendente.getCargo());
        stmt.setString(4, atendente.getAvatarCor());
        stmt.setString(5, atendente.getIniciais());
        stmt.setString(6, String.join(",", atendente.getTracos()));
        stmt.setString(7, String.join(",", atendente.getSegmentos()));
        stmt.setString(8, String.join(",", atendente.getFoco()));
        stmt.setString(9, atendente.getBio());
        stmt.setInt(10, atendente.getAnosExperiencia());
        stmt.setDouble(11, atendente.getNotaMedia());
        stmt.setInt(12, atendente.getCasesResolvidos());
        stmt.setString(13, atendente.getDisponibilidade());
    }

    private Atendente montarAtendente(ResultSet rs) throws SQLException {
        Atendente atendente = new Atendente();
        atendente.setId(rs.getInt(1));
        atendente.setNome(rs.getString(2));
        atendente.setEmail(rs.getString(3));
        atendente.setCargo(rs.getString(4));
        atendente.setAvatarCor(rs.getString(5));
        atendente.setIniciais(rs.getString(6));
        atendente.setTracos(paraLista(rs.getString(7)));
        atendente.setSegmentos(paraLista(rs.getString(8)));
        atendente.setFoco(paraLista(rs.getString(9)));
        atendente.setBio(rs.getString(10));
        atendente.setAnosExperiencia(rs.getInt(11));
        atendente.setNotaMedia(rs.getDouble(12));
        atendente.setCasesResolvidos(rs.getInt(13));
        atendente.setDisponibilidade(rs.getString(14));
        return atendente;
    }

    // no banco as listas ficam separadas por virgula
    private List<String> paraLista(String valor) {
        List<String> lista = new ArrayList<String>();
        if (valor == null || valor.isBlank()) {
            return lista;
        }
        for (String item : valor.split(",")) {
            lista.add(item.trim());
        }
        return lista;
    }

    @Override
    public void close() throws SQLException {
        if (minhaConexao != null && !minhaConexao.isClosed()) {
            minhaConexao.close();
        }
    }
}
