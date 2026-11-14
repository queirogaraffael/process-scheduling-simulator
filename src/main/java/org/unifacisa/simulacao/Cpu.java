package org.unifacisa.simulacao;

import org.unifacisa.escalonador.Escalonador;
import org.unifacisa.modelo.Processo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Cpu {

    private final Escalonador escalonador;
    private final long delayMs;
    private final boolean exibirCiclos;

    private final List<Processo> pendentes = new ArrayList<>();
    private final List<Processo> finalizados = new ArrayList<>();
    private final List<Integer> ordemExecucao = new ArrayList<>();
    private final List<Integer> ordemFinalizacao = new ArrayList<>();
    private final List<String> fatiasGantt = new ArrayList<>();

    private Processo atual;
    private Processo ultimoNaCpu;
    private int tempo;
    private int fatiaRestante;
    private int trocasDeProcesso;
    private int ciclosOciosos;

    private int inicioFatiaAtual = 0;
    private int idFatiaAtual = -1;

    public Cpu(Escalonador escalonador, long delayMs, boolean exibirCiclos) {
        this.escalonador = Objects.requireNonNull(escalonador, "O escalonador não pode ser nulo.");
        if (delayMs < 0) {
            throw new IllegalArgumentException("O delay não pode ser negativo.");
        }
        this.delayMs = delayMs;
        this.exibirCiclos = exibirCiclos;
    }

    public Resultado executar(List<Processo> processos) {
        reiniciarEstado(processos);

        if (exibirCiclos) {
            System.out.println("\n================================================================================");
            System.out.println(" INICIANDO SIMULAÇÃO: " + escalonador.descricao());
            System.out.println("================================================================================");
        }

        liberarChegadas(tempo);

        while (!terminou()) {
            if (atual == null) {
                atual = escalonador.proximo();
                if (atual == null) {
                    registrarTickGantt(0, tempo);
                    ultimoNaCpu = null;
                    tempo++;
                    ciclosOciosos++;
                    liberarChegadas(tempo);
                    if (exibirCiclos) {
                        imprimirEstado();
                    }
                    sleep();
                    continue;
                }
                registrarDespacho(atual);
                fatiaRestante = escalonador.quantum();
            }

            registrarTickGantt(atual.getId(), tempo);
            atual.executarInstrucao();
            tempo++;
            fatiaRestante--;

            liberarChegadas(tempo);

            if (exibirCiclos) {
                imprimirEstado();
            }

            if (atual.finalizado()) {
                atual.setTermino(tempo);
                finalizados.add(atual);
                ordemFinalizacao.add(atual.getId());
                atual = null;
            } else if (fatiaRestante == 0) {
                escalonador.devolver(atual);
                atual = null;
            }

            sleep();
        }

        fecharUltimaFatiaGantt();

        double somaEspera = 0.0;
        double somaRetorno = 0.0;
        for (Processo p : finalizados) {
            somaEspera += p.tempoEspera();
            somaRetorno += p.tempoRetorno();
        }
        int totalProcessos = finalizados.size();
        double mediaEspera = totalProcessos == 0 ? 0.0 : somaEspera / totalProcessos;
        double mediaRetorno = totalProcessos == 0 ? 0.0 : somaRetorno / totalProcessos;

        if (exibirCiclos) {
            System.out.printf("--> Fim [%s] | Tempo total: %d ticks | Espera média: %.2f | Trocas: %d%n",
                    escalonador.descricao(), tempo, mediaEspera, trocasDeProcesso);
        }

        return new Resultado(
                escalonador.descricao(),
                escalonador.getPolitica(),
                escalonador.quantum(),
                new ArrayList<>(ordemExecucao),
                new ArrayList<>(ordemFinalizacao),
                mediaEspera,
                mediaRetorno,
                trocasDeProcesso,
                tempo,
                ciclosOciosos,
                new ArrayList<>(finalizados),
                String.join(" ", fatiasGantt)
        );
    }

    private void reiniciarEstado(List<Processo> processos) {
        pendentes.clear();
        pendentes.addAll(processos);
        pendentes.sort(Comparator.comparingInt(Processo::getChegada).thenComparingInt(Processo::getId));
        finalizados.clear();
        ordemExecucao.clear();
        ordemFinalizacao.clear();
        fatiasGantt.clear();
        atual = null;
        ultimoNaCpu = null;
        tempo = 0;
        fatiaRestante = 0;
        trocasDeProcesso = 0;
        ciclosOciosos = 0;
        inicioFatiaAtual = 0;
        idFatiaAtual = -1;
    }

    private boolean terminou() {
        return atual == null && !escalonador.temProcessos() && pendentes.isEmpty();
    }

    private void liberarChegadas(int instanteAtual) {
        Iterator<Processo> it = pendentes.iterator();
        while (it.hasNext()) {
            Processo p = it.next();
            if (p.getChegada() <= instanteAtual) {
                escalonador.adicionar(p);
                it.remove();
            } else {
                break;
            }
        }
    }

    private void registrarDespacho(Processo novoProcesso) {
        novoProcesso.registrarInicioSeNecessario(tempo);
        if (ultimoNaCpu != null && ultimoNaCpu.getId() != novoProcesso.getId()) {
            trocasDeProcesso++;
        }
        if (ordemExecucao.isEmpty() || ordemExecucao.get(ordemExecucao.size() - 1) != novoProcesso.getId()) {
            ordemExecucao.add(novoProcesso.getId());
        }
        ultimoNaCpu = novoProcesso;
    }

    private void registrarTickGantt(int idExecutando, int tickInicio) {
        if (idFatiaAtual == -1) {
            idFatiaAtual = idExecutando;
            inicioFatiaAtual = tickInicio;
        } else if (idFatiaAtual != idExecutando) {
            fatiasGantt.add(formatarFatiaGantt(inicioFatiaAtual, tickInicio, idFatiaAtual));
            idFatiaAtual = idExecutando;
            inicioFatiaAtual = tickInicio;
        }
    }

    private void fecharUltimaFatiaGantt() {
        if (idFatiaAtual != -1) {
            fatiasGantt.add(formatarFatiaGantt(inicioFatiaAtual, tempo, idFatiaAtual));
            idFatiaAtual = -1;
        }
    }

    private String formatarFatiaGantt(int inicio, int fim, int id) {
        String rotulo = id == 0 ? "OCIOSA" : "P" + id;
        return String.format("[%d..%d: %s]", inicio, fim, rotulo);
    }

    private void imprimirEstado() {
        String emExecucaoStr = atual == null
                ? "Nenhum (CPU aguardando)"
                : String.format("P%d (restantes=%d/%d, prio=%d)",
                        atual.getId(), atual.getInstrucoesRestantes(),
                        atual.getInstrucoesTotais(), atual.getPrioridade());

        List<Processo> fila = escalonador.aguardando();
        String aguardandoStr = fila.isEmpty()
                ? "Nenhum"
                : fila.stream()
                        .map(p -> String.format("P%d(%d instr, prio=%d)",
                                p.getId(), p.getInstrucoesRestantes(), p.getPrioridade()))
                        .collect(Collectors.joining(", "));

        String finalizadosStr = finalizados.isEmpty()
                ? "Nenhum"
                : finalizados.stream()
                        .map(p -> "P" + p.getId())
                        .collect(Collectors.joining(", "));

        System.out.printf("[Ciclo %03d] Em execução: %-32s | Aguardando: %-40s | Finalizados: %s%n",
                tempo, emExecucaoStr, aguardandoStr, finalizadosStr);
    }

    private void sleep() {
        if (delayMs > 0) {
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

