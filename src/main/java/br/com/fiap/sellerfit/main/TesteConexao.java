package br.com.fiap.sellerfit.main;

import br.com.fiap.sellerfit.conexoes.ConexaoFactory;
import br.com.fiap.sellerfit.excecoes.ExcecoesConexao;

import java.sql.Connection;
import java.sql.SQLException;

public class TesteConexao {

    public static void main(String[] args) throws ExcecoesConexao, SQLException, ClassNotFoundException {

        try {
            Connection cn = new ConexaoFactory().conexao();
            System.out.print("Conectado com o Banco de Dados");
            cn.close();
        }catch (Exception e){
            throw new ExcecoesConexao(e);
        }
    }
}
