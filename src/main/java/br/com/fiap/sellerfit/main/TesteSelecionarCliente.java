package br.com.fiap.sellerfit.main;

import br.com.fiap.sellerfit.entities.Cliente;
import br.com.fiap.sellerfit.services.ClienteService;

import java.sql.SQLException;
import java.util.List;

public class TesteSelecionarCliente {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        ClienteService clienteService = new ClienteService();

        List<Cliente> listaClientes = clienteService.selecionar();

        if (listaClientes != null) {
            // foreach
            for (Cliente c : listaClientes) {
                System.out.println(
                        c.getId() + " " +
                        c.getNome() + " " +
                        c.getEmpresa() + " " +
                        c.getSegmento() + " " +
                        c.getOrcamento()
                );
            }
        }
    }
}
