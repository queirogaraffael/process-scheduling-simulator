package org.unifacisa.escalonador;

public enum Politica {
    FCFS("FCFS (First Come, First Served)"),
    SJF("SJF (Shortest Job First)"),
    PRIORIDADE("Prioridade (Não Preemptivo)"),
    ROUND_ROBIN("Round Robin");

    private final String nomeExibicao;

    Politica(String nomeExibicao) {
        this.nomeExibicao = nomeExibicao;
    }

    public String getNomeExibicao() {
        return nomeExibicao;
    }
}

