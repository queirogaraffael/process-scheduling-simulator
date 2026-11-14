package org.unifacisa.modelo;

public class Processo {

    private final int id;
    private final int instrucoesTotais;
    private int instrucoesRestantes;
    private final int prioridade;
    private final int chegada;
    private int inicio;
    private int termino;

    public Processo(int id, int instrucoesTotais, int prioridade) {
        this(id, instrucoesTotais, prioridade, 0);
    }

    public Processo(int id, int instrucoesTotais, int prioridade, int chegada) {
        if (id <= 0) {
            throw new IllegalArgumentException("O ID do processo deve ser positivo: " + id);
        }
        if (instrucoesTotais <= 0) {
            throw new IllegalArgumentException("A quantidade de instruções deve ser positiva: " + instrucoesTotais);
        }
        if (prioridade < 1 || prioridade > 5) {
            throw new IllegalArgumentException("A prioridade deve estar entre 1 e 5: " + prioridade);
        }
        if (chegada < 0) {
            throw new IllegalArgumentException("O instante de chegada não pode ser negativo: " + chegada);
        }
        this.id = id;
        this.instrucoesTotais = instrucoesTotais;
        this.instrucoesRestantes = instrucoesTotais;
        this.prioridade = prioridade;
        this.chegada = chegada;
        this.inicio = -1;
        this.termino = -1;
    }

    public void executarInstrucao() {
        if (instrucoesRestantes <= 0) {
            throw new IllegalStateException("O processo P" + id + " já está finalizado.");
        }
        instrucoesRestantes--;
    }

    public boolean finalizado() {
        return instrucoesRestantes == 0;
    }

    public Processo copia() {
        return new Processo(id, instrucoesTotais, prioridade, chegada);
    }

    public int tempoEspera() {
        if (termino < 0) {
            throw new IllegalStateException("O processo P" + id + " ainda não possui instante de término registrado.");
        }
        return termino - chegada - instrucoesTotais;
    }

    public int tempoRetorno() {
        if (termino < 0) {
            throw new IllegalStateException("O processo P" + id + " ainda não possui instante de término registrado.");
        }
        return termino - chegada;
    }

    public int getId() {
        return id;
    }

    public int getInstrucoesTotais() {
        return instrucoesTotais;
    }

    public int getInstrucoesRestantes() {
        return instrucoesRestantes;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public int getChegada() {
        return chegada;
    }

    public int getInicio() {
        return inicio;
    }

    public void registrarInicioSeNecessario(int tempoAtual) {
        if (this.inicio < 0) {
            this.inicio = tempoAtual;
        }
    }

    public int getTermino() {
        return termino;
    }

    public void setTermino(int termino) {
        this.termino = termino;
    }

    @Override
    public String toString() {
        return String.format("P%d[instr=%d/%d, prio=%d, cheg=%d]",
                id, instrucoesRestantes, instrucoesTotais, prioridade, chegada);
    }
}

