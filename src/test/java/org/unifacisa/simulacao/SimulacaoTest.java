package org.unifacisa.simulacao;

import org.junit.jupiter.api.Test;
import org.unifacisa.escalonador.Politica;
import org.unifacisa.modelo.GeradorDeProcessos;
import org.unifacisa.modelo.Processo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimulacaoTest {

    private List<Processo> cenarioCalculadoAMao() {
        return List.of(
                new Processo(1, 3, 2, 0),
                new Processo(2, 2, 5, 0),
                new Processo(3, 1, 1, 0)
        );
    }

    @Test
    void deveCalcularMetricasExatasParaFcfs() {
        Simulacao sim = new Simulacao(0, false);
        Resultado res = sim.executar(cenarioCalculadoAMao(), Politica.FCFS);

        assertEquals(List.of(1, 2, 3), res.getOrdemExecucao());
        assertEquals(List.of(1, 2, 3), res.getOrdemFinalizacao());
        assertEquals(8.0 / 3.0, res.getTempoMedioEspera(), 1e-6);
        assertEquals(2, res.getTrocasDeProcesso());
        assertEquals(6, res.getTempoTotal());
    }

    @Test
    void deveCalcularMetricasExatasParaSjf() {
        Simulacao sim = new Simulacao(0, false);
        Resultado res = sim.executar(cenarioCalculadoAMao(), Politica.SJF);

        assertEquals(List.of(3, 2, 1), res.getOrdemExecucao());
        assertEquals(List.of(3, 2, 1), res.getOrdemFinalizacao());
        assertEquals(4.0 / 3.0, res.getTempoMedioEspera(), 1e-6);
        assertEquals(2, res.getTrocasDeProcesso());
        assertEquals(6, res.getTempoTotal());
    }

    @Test
    void deveCalcularMetricasExatasParaPrioridade() {
        Simulacao sim = new Simulacao(0, false);
        Resultado res = sim.executar(cenarioCalculadoAMao(), Politica.PRIORIDADE);

        assertEquals(List.of(2, 1, 3), res.getOrdemExecucao());
        assertEquals(List.of(2, 1, 3), res.getOrdemFinalizacao());
        assertEquals(7.0 / 3.0, res.getTempoMedioEspera(), 1e-6);
        assertEquals(2, res.getTrocasDeProcesso());
        assertEquals(6, res.getTempoTotal());
    }

    @Test
    void deveCalcularMetricasExatasParaRoundRobin() {
        Simulacao sim = new Simulacao(0, false);
        Resultado res = sim.executar(cenarioCalculadoAMao(), Politica.ROUND_ROBIN, 1);

        assertEquals(List.of(1, 2, 3, 1, 2, 1), res.getOrdemExecucao());
        assertEquals(List.of(3, 2, 1), res.getOrdemFinalizacao());
        assertEquals(8.0 / 3.0, res.getTempoMedioEspera(), 1e-6);
        assertEquals(5, res.getTrocasDeProcesso());
        assertEquals(6, res.getTempoTotal());
    }

    @Test
    void cpuDeveAguardarQuandoNaoExistiremProcessosDisponiveis() {
        List<Processo> processos = List.of(
                new Processo(1, 2, 3, 0),
                new Processo(2, 2, 3, 5)
        );
        Simulacao sim = new Simulacao(0, false);
        Resultado res = sim.executar(processos, Politica.FCFS);

        assertEquals(7, res.getTempoTotal());
        assertEquals(3, res.getCiclosOciosos());
        assertEquals(0.0, res.getTempoMedioEspera(), 1e-6);
        assertEquals(0, res.getTrocasDeProcesso());
    }

    @Test
    void bateriaCompletaDeveExecutarOsSeisCenariosComMesmaCargaDeInstrucoes() {
        List<Processo> base = new GeradorDeProcessos(42L).gerar(10);
        int somaInstrucoes = base.stream().mapToInt(Processo::getInstrucoesTotais).sum();

        Simulacao sim = new Simulacao(0, false);
        List<Resultado> resultados = sim.executarBateriaCompleta(base);

        assertEquals(6, resultados.size());
        for (Resultado r : resultados) {
            assertEquals(10, r.getProcessosFinalizados().size());
            assertTrue(r.getProcessosFinalizados().stream().allMatch(Processo::finalizado));
            assertEquals(somaInstrucoes, r.getTempoTotal() - r.getCiclosOciosos());
        }
    }
}

