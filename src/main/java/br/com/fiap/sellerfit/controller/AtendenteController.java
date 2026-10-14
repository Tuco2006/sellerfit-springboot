package br.com.fiap.sellerfit.controller;

import br.com.fiap.sellerfit.entities.Atendente;
import br.com.fiap.sellerfit.services.AtendenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/atendentes")
public class AtendenteController {

    private final AtendenteService atendenteService = new AtendenteService();

    // Selecionar
    @GetMapping
    public ResponseEntity<List<Atendente>> selecionarRs() throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(atendenteService.selecionar());
    }

    // Buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<Atendente> buscarPorIdRs(@PathVariable int id) throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(atendenteService.buscarPorId(id));
    }

    // Inserir
    @PostMapping
    public ResponseEntity<Atendente> inserirRs(@RequestBody Atendente atendente) throws SQLException, ClassNotFoundException {
        atendenteService.inserir(atendente);
        URI location = URI.create("/api/atendentes/" + atendente.getId());
        return ResponseEntity.created(location).body(atendente);
    }

    // Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<Atendente> atualizarRs(@PathVariable int id, @RequestBody Atendente atendente) throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(atendenteService.atualizar(id, atendente));
    }

    // Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRs(@PathVariable int id) throws SQLException, ClassNotFoundException {
        atendenteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
