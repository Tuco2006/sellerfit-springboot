package br.com.fiap.sellerfit.controller;

import br.com.fiap.sellerfit.entities.Match;
import br.com.fiap.sellerfit.services.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService = new MatchService();

    // Selecionar
    @GetMapping
    public ResponseEntity<List<Match>> selecionarRs() throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(matchService.selecionar());
    }

    // Buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<Match> buscarPorIdRs(@PathVariable int id) throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(matchService.buscarPorId(id));
    }

    // Inserir
    @PostMapping
    public ResponseEntity<Match> inserirRs(@RequestBody Match match) throws SQLException, ClassNotFoundException {
        matchService.inserir(match);
        URI location = URI.create("/api/matches/" + match.getId());
        return ResponseEntity.created(location).body(match);
    }

    // Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<Match> atualizarRs(@PathVariable int id, @RequestBody Match match) throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(matchService.atualizar(id, match));
    }

    // Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRs(@PathVariable int id) throws SQLException, ClassNotFoundException {
        matchService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
