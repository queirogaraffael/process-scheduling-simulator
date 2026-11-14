package org.unifacisa.simulacao;

import org.unifacisa.escalonador.Escalonador;
import org.unifacisa.escalonador.Politica;
import org.unifacisa.modelo.GeradorDeProcessos;
import org.unifacisa.modelo.Processo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Simulacao {

    private final long delayMs;
    private final boolean exibirCiclos;

    public Simulacao() {
        this(0L, true);
    }

    public Simulacao(long delayMs, boolean exibirCiclos) {
        this.delayMs = delayMs;
        this.exibirCiclos = exibirCiclos;
    }

    public Resultado executar(List<Processo> processosBase, Politica politica) {
        List<Processo> copias = GeradorDeProcessos.clonarLista(processosBase);
        Escalonador escalonador = new Escalonador(politica);
        Cpu cpu = new Cpu(escalonador, delayMs, exibirCiclos);
        return cpu.executar(copias);
    }

    public Resultado executar(List<Processo> processosBase, Politica politica, int quantum) {
        List<Processo> copias = GeradorDeProcessos.clonarLista(processosBase);
        Escalonador escalonador = new Escalonador(politica, quantum);
        Cpu cpu = new Cpu(escalonador, delayMs, exibirCiclos);
        return cpu.executar(copias);
    }

    public List<Resultado> executarBateriaCompleta(List<Processo> processosBase) {
        List<Resultado> resultados = new ArrayList<>(6);
        resultados.add(executar(processosBase, Politica.FCFS));
        resultados.add(executar(processosBase, Politica.SJF));
        resultados.add(executar(processosBase, Politica.PRIORIDADE));
        resultados.add(executar(processosBase, Politica.ROUND_ROBIN, 2));
        resultados.add(executar(processosBase, Politica.ROUND_ROBIN, 5));
        resultados.add(executar(processosBase, Politica.ROUND_ROBIN, 10));
        return resultados;
    }

    public static String formatarTabelaComparativa(List<Resultado> resultados) {
        StringBuilder sb = new StringBuilder();
        String linha = "+---------------------------------+----------------------+-----------------+------------------------------------------------------------+\n";
        sb.append("\n");
        sb.append("=========================================================================================================================================\n");
        sb.append("                                                      TABELA COMPARATIVA DE RESULTADOS                                                   \n");
        sb.append("=========================================================================================================================================\n");
        sb.append(linha);
        sb.append(String.format("| %-31s | %-20s | %-15s | %-58s |%n",
                "Algoritmo", "Tempo Médio Espera", "Trocas Processo", "Ordem de Finalização"));
        sb.append(linha);
        for (Resultado r : resultados) {
            sb.append(String.format(Locale.US, "| %-31s | %14.2f ticks | %15d | %-58s |%n",
                    r.getNomeAlgoritmo(),
                    r.getTempoMedioEspera(),
                    r.getTrocasDeProcesso(),
                    r.formatarOrdemFinalizacao()));
        }
        sb.append(linha);
        sb.append("\nDetalhamento da Ordem de Execução (Despachos na CPU):\n");
        for (Resultado r : resultados) {
            sb.append(String.format("  - %-31s : %s%n", r.getNomeAlgoritmo(), r.formatarOrdemExecucao()));
        }
        return sb.toString();
    }
}
