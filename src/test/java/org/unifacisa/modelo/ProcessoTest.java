package org.unifacisa.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProcessoTest {

    @Test
    void deveDecrementarInstrucoesEFinalizarAoChegarAZero() {
        Processo processo = new Processo(1, 3, 4, 0);
        assertFalse(processo.finalizado());
        assertEquals(3, processo.getInstrucoesRestantes());

        processo.executarInstrucao();
        assertEquals(2, processo.getInstrucoesRestantes());
        assertFalse(processo.finalizado());

        processo.executarInstrucao();
        processo.executarInstrucao();
        assertEquals(0, processo.getInstrucoesRestantes());
        assertTrue(processo.finalizado());
    }

    @Test
    void deveLancarExcecaoAoExecutarProcessoJaFinalizado() {
        Processo processo = new Processo(1, 1, 2, 0);
        processo.executarInstrucao();
        assertTrue(processo.finalizado());
        assertThrows(IllegalStateException.class, processo::executarInstrucao);
    }

    @Test
    void deveValidarLimitesDePrioridadeEParametros() {
        assertThrows(IllegalArgumentException.class, () -> new Processo(1, 10, 0));
        assertThrows(IllegalArgumentException.class, () -> new Processo(1, 10, 6));
        assertThrows(IllegalArgumentException.class, () -> new Processo(0, 10, 3));
        assertThrows(IllegalArgumentException.class, () -> new Processo(1, 0, 3));
        assertThrows(IllegalArgumentException.class, () -> new Processo(1, 10, 3, -1));
    }

    @Test
    void deveCalcularTempoDeEsperaERetornoCorretamente() {
        Processo processo = new Processo(1, 15, 3, 5);
        processo.registrarInicioSeNecessario(10);
        processo.setTermino(32);

        assertEquals(27, processo.tempoRetorno()); // 32 - 5
        assertEquals(12, processo.tempoEspera());  // 32 - 5 - 15
    }

    @Test
    void copiaDevePreservarAtributosOriginaisNoEstadoInicial() {
        Processo original = new Processo(2, 20, 5, 4);
        original.executarInstrucao();
        original.setTermino(30);

        Processo copia = original.copia();
        assertNotSame(original, copia);
        assertEquals(2, copia.getId());
        assertEquals(20, copia.getInstrucoesTotais());
        assertEquals(20, copia.getInstrucoesRestantes());
        assertEquals(5, copia.getPrioridade());
        assertEquals(4, copia.getChegada());
        assertEquals(-1, copia.getTermino());
    }
}

