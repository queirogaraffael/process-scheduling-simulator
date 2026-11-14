package org.unifacisa.escalonador;

import org.unifacisa.modelo.Processo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Escalonador {

    private static final Comparator<Processo> COMPARADOR_SJF =
            Comparator.comparingInt(Processo::getInstrucoesRestantes)
                    .thenComparingInt(Processo::getChegada)
                    .thenComparingInt(Processo::getId);

    private static final Comparator<Processo> COMPARADOR_PRIORIDADE =
            Comparator.comparingInt(Processo::getPrioridade).reversed()
                    .thenComparingInt(Processo::getChegada)
                    .thenComparingInt(Processo::getId);

    private final Politica politica;
    private final int quantum;
    private final LinkedList<Processo> filaProntos = new LinkedList<>();

    public Escalonador(Politica politica) {
        this(politica, Integer.MAX_VALUE);
        if (politica == Politica.ROUND_ROBIN) {
            throw new IllegalArgumentException("Round Robin exige informar um valor de quantum positivo.");
        }
    }

    public Escalonador(Politica politica, int quantum) {
        Objects.requireNonNull(politica, "A política de escalonamento não pode ser nula.");
        if (quantum <= 0) {
            throw new IllegalArgumentException("O quantum deve ser positivo: " + quantum);
        }
        this.politica = politica;
        this.quantum = politica == Politica.ROUND_ROBIN ? quantum : Integer.MAX_VALUE;
    }

    public void adicionar(Processo processo) {
        Objects.requireNonNull(processo, "O processo não pode ser nulo.");
        filaProntos.addLast(processo);
    }

    public void devolver(Processo processo) {
        Objects.requireNonNull(processo, "O processo não pode ser nulo.");
        if (processo.finalizado()) {
            throw new IllegalArgumentException("Não é possível devolver um processo já finalizado à fila.");
        }
        filaProntos.addLast(processo);
    }

    public Processo proximo() {
        if (filaProntos.isEmpty()) {
            return null;
        }
        return switch (politica) {
            case FCFS, ROUND_ROBIN -> filaProntos.removeFirst();
            case SJF -> removerMelhor(COMPARADOR_SJF);
            case PRIORIDADE -> removerMelhor(COMPARADOR_PRIORIDADE);
        };
    }

    private Processo removerMelhor(Comparator<Processo> comparador) {
        Processo escolhido = filaProntos.getFirst();
        for (Processo candidato : filaProntos) {
            if (comparador.compare(candidato, escolhido) < 0) {
                escolhido = candidato;
            }
        }
        filaProntos.remove(escolhido);
        return escolhido;
    }

    public int quantum() {
        return quantum;
    }

    public List<Processo> aguardando() {
        return Collections.unmodifiableList(new ArrayList<>(filaProntos));
    }

    public boolean temProcessos() {
        return !filaProntos.isEmpty();
    }

    public Politica getPolitica() {
        return politica;
    }

    public String descricao() {
        if (politica == Politica.ROUND_ROBIN) {
            return "Round Robin (Quantum " + quantum + ")";
        }
        return politica.getNomeExibicao();
    }
}

