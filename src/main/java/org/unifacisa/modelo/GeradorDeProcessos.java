package org.unifacisa.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeradorDeProcessos {

    public static final int MIN_INSTRUCOES = 10;
    public static final int MAX_INSTRUCOES = 50;
    public static final int MIN_PRIORIDADE = 1;
    public static final int MAX_PRIORIDADE = 5;
    public static final int INTERVALO_MAX_PADRAO = 5;

    private final Random random;
    private final int intervaloMaxChegada;
    private int proximoId = 1;
    private int ultimoInstanteChegada = 0;

    public GeradorDeProcessos() {
        this(System.currentTimeMillis(), INTERVALO_MAX_PADRAO);
    }

    public GeradorDeProcessos(long seed) {
        this(seed, INTERVALO_MAX_PADRAO);
    }

    public GeradorDeProcessos(long seed, int intervaloMaxChegada) {
        if (intervaloMaxChegada < 0) {
            throw new IllegalArgumentException("O intervalo máximo de chegada não pode ser negativo.");
        }
        this.random = new Random(seed);
        this.intervaloMaxChegada = intervaloMaxChegada;
    }

    public Processo gerarProcesso(int instanteChegada) {
        int id = proximoId++;
        int instrucoes = MIN_INSTRUCOES + random.nextInt(MAX_INSTRUCOES - MIN_INSTRUCOES + 1);
        int prioridade = MIN_PRIORIDADE + random.nextInt(MAX_PRIORIDADE - MIN_PRIORIDADE + 1);
        ultimoInstanteChegada = instanteChegada;
        return new Processo(id, instrucoes, prioridade, instanteChegada);
    }

    public List<Processo> gerar(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("A quantidade de processos não pode ser negativa.");
        }
        List<Processo> processos = new ArrayList<>(quantidade);
        for (int i = 0; i < quantidade; i++) {
            int chegada;
            if (i == 0 && proximoId == 1) {
                chegada = 0;
            } else {
                int delta = intervaloMaxChegada == 0 ? 0 : random.nextInt(intervaloMaxChegada + 1);
                chegada = ultimoInstanteChegada + delta;
            }
            processos.add(gerarProcesso(chegada));
        }
        return processos;
    }

    public static List<Processo> clonarLista(List<Processo> originais) {
        List<Processo> copias = new ArrayList<>(originais.size());
        for (Processo p : originais) {
            copias.add(p.copia());
        }
        return copias;
    }
}

