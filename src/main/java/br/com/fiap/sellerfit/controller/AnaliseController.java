package br.com.fiap.sellerfit.controller;

import br.com.fiap.sellerfit.dto.EntradaAnalise;
import br.com.fiap.sellerfit.dto.ResultadoAnalise;
import br.com.fiap.sellerfit.services.AnaliseService;
import br.com.fiap.sellerfit.services.OpenAiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AnaliseController {

    private final AnaliseService analiseService = new AnaliseService();
    private final OpenAiService openAiService = new OpenAiService();

    // Analisar a transcricao da reuniao e sugerir os atendentes
    @PostMapping("/analises")
    public ResponseEntity<ResultadoAnalise> analisarRs(@RequestBody EntradaAnalise entrada) throws SQLException, ClassNotFoundException {
        ResultadoAnalise resultado = analiseService.analisar(entrada);
        URI location = URI.create("/api/analises/" + resultado.getAnalise().getIdReuniao());
        return ResponseEntity.created(location).body(resultado);
    }

    // Consultar a analise de uma reuniao
    @GetMapping("/analises/{idReuniao}")
    public ResponseEntity<ResultadoAnalise> buscarAnaliseRs(@PathVariable int idReuniao) throws SQLException, ClassNotFoundException {
        return ResponseEntity.ok(analiseService.buscarPorReuniao(idReuniao));
    }

    // Verificar se a API esta no ar
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthRs() {
        return ResponseEntity.ok(Map.of("status", "ok", "ia", openAiService.configurada() ? "openai" : "motor-local"));
    }
}
