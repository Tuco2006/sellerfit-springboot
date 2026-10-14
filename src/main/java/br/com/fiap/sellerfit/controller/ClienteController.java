package br.com.fiap.sellerfit.controller;

import br.com.fiap.sellerfit.entities.Cliente;
import br.com.fiap.sellerfit.entities.Reuniao;
import br.com.fiap.sellerfit.services.ClienteService;
import br.com.fiap.sellerfit.services.ReuniaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService = new ClienteService();
    private final ReuniaoService reuniaoService = new ReuniaoService();

    // Selecionar
    @GetMapping
    public ResponseEntity<List<Cliente>> selecionarRs() throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(clienteService.selecionar());
    }

    // Buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorIdRs(@PathVariable int id) throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    // Historico de reunioes do cliente
    @GetMapping("/{id}/reunioes")
    public ResponseEntity<List<Reuniao>> selecionarReunioesRs(@PathVariable int id) throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(reuniaoService.selecionarPorCliente(id));
    }

    // Inserir
    @PostMapping
    public ResponseEntity<Cliente> inserirRs(@RequestBody Cliente cliente) throws SQLException, ClassNotFoundException {
        clienteService.inserir(cliente);
        URI location = URI.create("/api/clientes/" + cliente.getId());
        return ResponseEntity.created(location).body(cliente);
    }

    // Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarRs(@PathVariable int id, @RequestBody Cliente cliente) throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(clienteService.atualizar(id, cliente));
    }

    // Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRs(@PathVariable int id) throws SQLException, ClassNotFoundException {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
