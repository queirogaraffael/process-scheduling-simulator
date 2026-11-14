package org.unifacisa;

import org.unifacisa.modelo.GeradorDeProcessos;
import org.unifacisa.modelo.Processo;
import org.unifacisa.simulacao.Resultado;
import org.unifacisa.simulacao.Simulacao;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public class Main {

    public static final long SEED_PADRAO = 42L;
    public static final int QUANTIDADE_PROCESSOS_PADRAO = 10;
    public static final long DELAY_PADRAO_MS = 20L;

    public static void main(String[] args) {
        long delayMs = DELAY_PADRAO_MS;
        long seed = SEED_PADRAO;
        int quantidadeProcessos = QUANTIDADE_PROCESSOS_PADRAO;
        boolean exibirCiclos = true;

        for (String arg : args) {
            if (arg.startsWith("--delay=")) {
                delayMs = Long.parseLong(arg.substring("--delay=".length()));
            } else if (arg.startsWith("--seed=")) {
                seed = Long.parseLong(arg.substring("--seed=".length()));
            } else if (arg.startsWith("--processos=")) {
                quantidadeProcessos = Integer.parseInt(arg.substring("--processos=".length()));
            } else if (arg.equals("--quiet")) {
                exibirCiclos = false;
            }
        }

        GeradorDeProcessos gerador = new GeradorDeProcessos(seed);
        List<Processo> processosBase = gerador.gerar(quantidadeProcessos);

        System.out.println("================================================================================");
        System.out.println("          SIMULADOR DE ALGORITMOS DE ESCALONAMENTO DE PROCESSOS                 ");
        System.out.println("================================================================================");
        System.out.printf("Configuração: %d processos | Seed: %d | Delay por ciclo: %d ms%n%n",
                quantidadeProcessos, seed, delayMs);

        System.out.println("Sequência de Processos Gerados (mesma entrada para todos os algoritmos):");
        System.out.println("+------+------------------+------------+--------------------+");
        System.out.println("| ID   | Instruções       | Prioridade | Instante Chegada   |");
        System.out.println("+------+------------------+------------+--------------------+");
        for (Processo p : processosBase) {
            System.out.printf("| P%-3d | %16d | %10d | %12d ticks |%n",
                    p.getId(), p.getInstrucoesTotais(), p.getPrioridade(), p.getChegada());
        }
        System.out.println("+------+------------------+------------+--------------------+");

        Simulacao simulacao = new Simulacao(delayMs, exibirCiclos);
        List<Resultado> resultados = simulacao.executarBateriaCompleta(processosBase);

        System.out.println(Simulacao.formatarTabelaComparativa(resultados));

        gravarRelatorioDados(processosBase, resultados, seed);
    }

    private static void gravarRelatorioDados(List<Processo> processosBase, List<Resultado> resultados, long seed) {
        StringBuilder md = new StringBuilder();
        md.append("# Dados Extraídos da Simulação (Seed ").append(seed).append(")\n\n");
        md.append("## 1. Sequência de Processos Utilizada nos Testes\n\n");
        md.append("| Processo | Quantidade de Instruções | Prioridade (1-5) | Instante de Chegada (tick) |\n");
        md.append("| :--- | :---: | :---: | :---: |\n");
        int somaInstrucoes = 0;
        for (Processo p : processosBase) {
            somaInstrucoes += p.getInstrucoesTotais();
            md.append(String.format("| **P%d** | %d | %d | %d |\n",
                    p.getId(), p.getInstrucoesTotais(), p.getPrioridade(), p.getChegada()));
        }
        md.append(String.format("\n**Total de instruções da carga:** %d instruções.\n\n", somaInstrucoes));

        md.append("## 2. Tabela Comparativa dos Algoritmos\n\n");
        md.append("| Algoritmo | Ordem de Finalização | Tempo Médio de Espera (ticks) | Tempo Médio de Retorno (ticks) | Trocas de Processos | Ordem de Execução (Despachos na CPU) |\n");
        md.append("| :--- | :--- | :---: | :---: | :---: | :--- |\n");
        for (Resultado r : resultados) {
            md.append(String.format(Locale.US, "| **%s** | `%s` | **%.2f** | %.2f | **%d** | `%s` |\n",
                    r.getNomeAlgoritmo(),
                    r.formatarOrdemFinalizacao(),
                    r.getTempoMedioEspera(),
                    r.getTempoMedioRetorno(),
                    r.getTrocasDeProcesso(),
                    r.formatarOrdemExecucao()));
        }

        md.append("\n## 3. Detalhamento Individual por Processo em Cada Algoritmo\n\n");
        for (Resultado r : resultados) {
            md.append("### ").append(r.getNomeAlgoritmo()).append("\n\n");
            md.append("| Processo | Chegada | Instruções | Prioridade | Início | Término | Tempo de Espera | Tempo de Retorno |\n");
            md.append("| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: |\n");
            for (Processo p : r.getProcessosFinalizados()) {
                md.append(String.format("| P%d | %d | %d | %d | %d | %d | %d | %d |\n",
                        p.getId(), p.getChegada(), p.getInstrucoesTotais(), p.getPrioridade(),
                        p.getInicio(), p.getTermino(), p.tempoEspera(), p.tempoRetorno()));
            }
            md.append("\n**Diagrama de Gantt:** `").append(r.getDiagramaGantt()).append("`\n\n");
        }

        try {
            Files.writeString(Path.of("RELATORIO_DADOS.md"), md.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Aviso: não foi possível gravar RELATORIO_DADOS.md: " + e.getMessage());
        }
    }
}
