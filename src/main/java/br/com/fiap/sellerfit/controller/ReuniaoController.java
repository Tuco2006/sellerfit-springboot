package br.com.fiap.sellerfit.controller;

import br.com.fiap.sellerfit.entities.Dor;
import br.com.fiap.sellerfit.entities.Match;
import br.com.fiap.sellerfit.entities.Reuniao;
import br.com.fiap.sellerfit.services.DorService;
import br.com.fiap.sellerfit.services.MatchService;
import br.com.fiap.sellerfit.services.ReuniaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/reunioes")
public class ReuniaoController {

    private final ReuniaoService reuniaoService = new ReuniaoService();
    private final DorService dorService = new DorService();
    private final MatchService matchService = new MatchService();

    // Selecionar
    @GetMapping
    public ResponseEntity<List<Reuniao>> selecionarRs() throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(reuniaoService.selecionar());
    }

    // Buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<Reuniao> buscarPorIdRs(@PathVariable int id) throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(reuniaoService.buscarPorId(id));
    }

    // Dores identificadas na reuniao
    @GetMapping("/{id}/dores")
    public ResponseEntity<List<Dor>> selecionarDoresRs(@PathVariable int id) throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(dorService.selecionarPorReuniao(id));
    }

    // Matches gerados para a reuniao
    @GetMapping("/{id}/matches")
    public ResponseEntity<List<Match>> selecionarMatchesRs(@PathVariable int id) throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(matchService.selecionarPorReuniao(id));
    }

    // Inserir
    @PostMapping
    public ResponseEntity<Reuniao> inserirRs(@RequestBody Reuniao reuniao) throws SQLException, ClassNotFoundException {
        reuniaoService.inserir(reuniao);
        URI location = URI.create("/api/reunioes/" + reuniao.getId());
        return ResponseEntity.created(location).body(reuniao);
    }

    // Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<Reuniao> atualizarRs(@PathVariable int id, @RequestBody Reuniao reuniao) throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(reuniaoService.atualizar(id, reuniao));
    }

    // Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRs(@PathVariable int id) throws SQLException, ClassNotFoundException {
        reuniaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
