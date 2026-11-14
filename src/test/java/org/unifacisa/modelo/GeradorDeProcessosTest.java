package org.unifacisa.modelo;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GeradorDeProcessosTest {

    @Test
    void deveGerarProcessosDentroDasFaixasEspecificadasComIdIncremental() {
        GeradorDeProcessos gerador = new GeradorDeProcessos(12345L);
        List<Processo> processos = gerador.gerar(100);

        assertEquals(100, processos.size());
        int chegadaAnterior = 0;

        for (int i = 0; i < processos.size(); i++) {
            Processo p = processos.get(i);
            assertEquals(i + 1, p.getId(), "O ID deve ser incremental começando em 1");
            assertTrue(p.getInstrucoesTotais() >= 10 && p.getInstrucoesTotais() <= 50,
                    "As instruções devem estar entre 10 e 50");
            assertTrue(p.getPrioridade() >= 1 && p.getPrioridade() <= 5,
                    "A prioridade deve estar entre 1 e 5");
            assertTrue(p.getChegada() >= chegadaAnterior,
                    "Os instantes de chegada devem ser não decrescentes");
            chegadaAnterior = p.getChegada();
        }
    }

    @Test
    void mesmaSeedDeveProduzirSequenciaIdenticaDeProcessos() {
        List<Processo> lista1 = new GeradorDeProcessos(42L).gerar(10);
        List<Processo> lista2 = new GeradorDeProcessos(42L).gerar(10);

        assertEquals(lista1.size(), lista2.size());
        for (int i = 0; i < lista1.size(); i++) {
            Processo p1 = lista1.get(i);
            Processo p2 = lista2.get(i);
            assertEquals(p1.getId(), p2.getId());
            assertEquals(p1.getInstrucoesTotais(), p2.getInstrucoesTotais());
            assertEquals(p1.getPrioridade(), p2.getPrioridade());
            assertEquals(p1.getChegada(), p2.getChegada());
        }
    }
}

