package org.unifacisa.escalonador;

import org.junit.jupiter.api.Test;
import org.unifacisa.modelo.Processo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EscalonadorTest {

    @Test
    void fcfsDeveRespeitarOrdemDeChegada() {
        Escalonador esc = new Escalonador(Politica.FCFS);
        esc.adicionar(new Processo(1, 30, 1, 0));
        esc.adicionar(new Processo(2, 10, 5, 1));
        esc.adicionar(new Processo(3, 20, 3, 2));

        assertEquals(1, esc.proximo().getId());
        assertEquals(2, esc.proximo().getId());
        assertEquals(3, esc.proximo().getId());
        assertNull(esc.proximo());
    }

    @Test
    void sjfDeveSelecionarMenorQuantidadeDeInstrucoesEDesempatarPorChegada() {
        Escalonador esc = new Escalonador(Politica.SJF);
        esc.adicionar(new Processo(1, 30, 5, 0));
        esc.adicionar(new Processo(2, 10, 1, 0));
        esc.adicionar(new Processo(3, 10, 4, 1));
        esc.adicionar(new Processo(4, 20, 2, 2));

        assertEquals(2, esc.proximo().getId());
        assertEquals(3, esc.proximo().getId());
        assertEquals(4, esc.proximo().getId());
        assertEquals(1, esc.proximo().getId());
        assertNull(esc.proximo());
    }

    @Test
    void prioridadeDeveSelecionarMaiorPrioridadeEDesempatarPorChegada() {
        Escalonador esc = new Escalonador(Politica.PRIORIDADE);
        esc.adicionar(new Processo(1, 15, 2, 0));
        esc.adicionar(new Processo(2, 40, 5, 0));
        esc.adicionar(new Processo(3, 10, 5, 1));
        esc.adicionar(new Processo(4, 25, 4, 2));

        assertEquals(2, esc.proximo().getId());
        assertEquals(3, esc.proximo().getId());
        assertEquals(4, esc.proximo().getId());
        assertEquals(1, esc.proximo().getId());
        assertNull(esc.proximo());
    }

    @Test
    void roundRobinDeveReenfileirarNoFimDaFila() {
        Escalonador esc = new Escalonador(Politica.ROUND_ROBIN, 2);
        Processo p1 = new Processo(1, 10, 3, 0);
        Processo p2 = new Processo(2, 10, 3, 0);
        esc.adicionar(p1);
        esc.adicionar(p2);

        assertEquals(2, esc.quantum());
        assertEquals(1, esc.proximo().getId());
        esc.devolver(p1);
        assertEquals(2, esc.proximo().getId());
        assertEquals(1, esc.proximo().getId());
    }
}

