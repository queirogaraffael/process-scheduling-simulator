# Simulador de Algoritmos de Escalonamento de Processos

Trabalho da disciplina de Sistemas Operacionais (Fase I) — UNIFACISA.

Simulador em Java que executa quatro algoritmos de escalonamento de CPU (FCFS, SJF, Prioridade e Round Robin) sobre a mesma sequência de processos e compara o tempo médio de espera e a quantidade de trocas de processo.

## Integrantes

- Rodrigo Lucas Oliveira Coelho
- Gabriel Barbosa de Souto
- Isthanlley Miranda Fragoso
- David Flor Batista
- João Felipe Ramos Lustosa de Lima
- Raffael Queiroga

## Requisitos

- Java 17
- Maven (opcional, só para compilar e rodar os testes)

## Como executar

Compile o projeto:

```
mvn compile
```

Execute o simulador:

```
java -cp target/classes org.unifacisa.Main
```

Opções disponíveis:

| Opção | Descrição | Padrão |
| :--- | :--- | :--- |
| `--delay=ms` | Pausa em milissegundos a cada ciclo da CPU, para acompanhar a execução | 20 |
| `--seed=N` | Semente do gerador de processos | 42 |
| `--processos=N` | Quantidade de processos gerados | 10 |
| `--quiet` | Não mostra o log ciclo a ciclo, só a tabela final | desligado |

Exemplos:

```
java -cp target/classes org.unifacisa.Main --delay=0 --quiet
java -cp target/classes org.unifacisa.Main --processos=15 --seed=99
```

Com o delay padrão, a execução completa leva cerca de meio minuto. Use `--delay=0` para ver tudo instantaneamente.

## Testes

```
mvn test
```

Os testes usam JUnit 5 e cobrem o processo, o gerador, o escalonador e a simulação (incluindo um cenário pequeno com resultado calculado à mão).

## Como funciona

A simulação usa tempo lógico: cada ciclo da CPU executa uma instrução do processo atual. Por isso o resultado é sempre o mesmo para a mesma semente.

Classes principais (pacote `org.unifacisa`):

- `modelo.Processo`: ID, instruções, prioridade (1 a 5) e instantes de chegada, início e término.
- `modelo.GeradorDeProcessos`: gera processos com ID incremental, 10 a 50 instruções e prioridade de 1 a 5.
- `escalonador.Escalonador`: guarda a fila de prontos e escolhe o próximo processo conforme a política.
- `escalonador.Politica`: FCFS, SJF, PRIORIDADE e ROUND_ROBIN.
- `simulacao.Cpu`: executa os processos, uma instrução por ciclo, e mostra os processos em execução, aguardando e finalizados.
- `simulacao.Simulacao`: roda os seis cenários (FCFS, SJF, Prioridade e Round Robin com quantum 2, 5 e 10) com cópias da mesma lista de processos.
- `Main`: ponto de entrada.

## Saídas

Ao rodar, o programa mostra a tabela comparativa no console e grava o arquivo `RELATORIO_DADOS.md` com os dados de cada processo e o diagrama de Gantt de cada cenário.

## Relatório

O relatório comparativo está em [RELATORIO.md](RELATORIO.md).

