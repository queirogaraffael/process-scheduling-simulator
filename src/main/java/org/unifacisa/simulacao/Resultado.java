package org.unifacisa.simulacao;

import org.unifacisa.escalonador.Politica;
import org.unifacisa.modelo.Processo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Resultado {

    private final String nomeAlgoritmo;
    private final Politica politica;
    private final int quantum;
    private final List<Integer> ordemExecucao;
    private final List<Integer> ordemFinalizacao;
    private final double tempoMedioEspera;
    private final double tempoMedioRetorno;
    private final int trocasDeProcesso;
    private final int tempoTotal;
    private final int ciclosOciosos;
    private final List<Processo> processosFinalizados;
    private final String diagramaGantt;

    public Resultado(
            String nomeAlgoritmo,
            Politica politica,
            int quantum,
            List<Integer> ordemExecucao,
            List<Integer> ordemFinalizacao,
            double tempoMedioEspera,
            double tempoMedioRetorno,
            int trocasDeProcesso,
            int tempoTotal,
            int ciclosOciosos,
            List<Processo> processosFinalizados,
            String diagramaGantt) {
        this.nomeAlgoritmo = nomeAlgoritmo;
        this.politica = politica;
        this.quantum = quantum;
        this.ordemExecucao = Collections.unmodifiableList(ordemExecucao);
        this.ordemFinalizacao = Collections.unmodifiableList(ordemFinalizacao);
        this.tempoMedioEspera = tempoMedioEspera;
        this.tempoMedioRetorno = tempoMedioRetorno;
        this.trocasDeProcesso = trocasDeProcesso;
        this.tempoTotal = tempoTotal;
        this.ciclosOciosos = ciclosOciosos;
        this.processosFinalizados = Collections.unmodifiableList(processosFinalizados);
        this.diagramaGantt = diagramaGantt;
    }

    public String getNomeAlgoritmo() {
        return nomeAlgoritmo;
    }

    public Politica getPolitica() {
        return politica;
    }

    public int getQuantum() {
        return quantum;
    }

    public List<Integer> getOrdemExecucao() {
        return ordemExecucao;
    }

    public List<Integer> getOrdemFinalizacao() {
        return ordemFinalizacao;
    }

    public String formatarOrdemExecucao() {
        return ordemExecucao.stream()
                .map(id -> "P" + id)
                .collect(Collectors.joining(" -> "));
    }

    public String formatarOrdemFinalizacao() {
        return ordemFinalizacao.stream()
                .map(id -> "P" + id)
                .collect(Collectors.joining(" -> "));
    }

    public double getTempoMedioEspera() {
        return tempoMedioEspera;
    }

    public double getTempoMedioRetorno() {
        return tempoMedioRetorno;
    }

    public int getTrocasDeProcesso() {
        return trocasDeProcesso;
    }

    public int getTempoTotal() {
        return tempoTotal;
    }

    public int getCiclosOciosos() {
        return ciclosOciosos;
    }

    public List<Processo> getProcessosFinalizados() {
        return processosFinalizados;
    }

    public String getDiagramaGantt() {
        return diagramaGantt;
    }
}

