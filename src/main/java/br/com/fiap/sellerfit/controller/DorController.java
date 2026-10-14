package br.com.fiap.sellerfit.controller;

import br.com.fiap.sellerfit.entities.Dor;
import br.com.fiap.sellerfit.services.DorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/dores")
public class DorController {

    private final DorService dorService = new DorService();

    // Selecionar
    @GetMapping
    public ResponseEntity<List<Dor>> selecionarRs() throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(dorService.selecionar());
    }

    // Buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<Dor> buscarPorIdRs(@PathVariable int id) throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(dorService.buscarPorId(id));
    }

    // Inserir
    @PostMapping
    public ResponseEntity<Dor> inserirRs(@RequestBody Dor dor) throws SQLException, ClassNotFoundException {
        dorService.inserir(dor);
        URI location = URI.create("/api/dores/" + dor.getId());
        return ResponseEntity.created(location).body(dor);
    }

    // Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<Dor> atualizarRs(@PathVariable int id, @RequestBody Dor dor) throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(dorService.atualizar(id, dor));
    }

    // Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRs(@PathVariable int id) throws SQLException, ClassNotFoundException {
        dorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
