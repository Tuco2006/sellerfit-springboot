package br.com.fiap.sellerfit.main;

import br.com.fiap.sellerfit.dto.MatchAtendente;
import br.com.fiap.sellerfit.entities.Atendente;
import br.com.fiap.sellerfit.entities.Atendimento;
import br.com.fiap.sellerfit.entities.Cliente;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class TesteAtendimento {

    // String
    static String texto(String j) {
        return JOptionPane.showInputDialog(j);
    }

    public static void main(String[] args) {

        Cliente cliente = new Cliente();
        cliente.setNome(texto("Nome do cliente"));
        cliente.setEmpresa(texto("Empresa do cliente"));
        cliente.setSegmento(texto("Segmento (ex: varejo, indústria, saúde)"));

        String transcricao = texto("Cole aqui a transcrição da reunião");

        Atendimento atendimento = new Atendimento(cliente, transcricao, null);

        // atendentes de exemplo, sem precisar do banco
        List<Atendente> atendentes = new ArrayList<Atendente>();
        atendentes.add(new Atendente(1, "João Pereira", null, "Especialista ERP Industrial", "#2563eb", "JP",
                List.of("técnico", "direto", "objetivo", "analítico"), List.of("indústria", "logística"), List.of("retenção"),
                "", 6, 8.7, 214, "Disponível"));
        atendentes.add(new Atendente(2, "Mariana Alves", null, "Consultora de Varejo", "#db2777", "MA",
                List.of("comunicativa", "empática", "ágil", "didática"), List.of("varejo", "serviços"), List.of("cross-sell"),
                "", 5, 9.4, 301, "Disponível"));
        atendentes.add(new Atendente(3, "Fernanda Costa", null, "Consultora Estratégica de Contas", "#0891b2", "FC",
                List.of("estratégico", "comunicativa", "negociadora", "confiante"), List.of("indústria", "varejo", "financeiro"), List.of("cross-sell"),
                "", 11, 9.0, 178, "Ausente"));

        List<MatchAtendente> matches = atendimento.sugerirMelhoresAtendentes(atendentes, 3);

        System.out.println(atendimento);
        System.out.println("\nAtendentes sugeridos:");
        for (MatchAtendente m : matches) {
            System.out.println(m.getAtendente().getNome() + " - score " + m.getScore() + " - " + m.getMotivo());
        }
    }
}
