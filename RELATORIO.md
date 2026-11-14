# Relatório: Simulador de Algoritmos de Escalonamento de Processos

## 3.1. Implementação

O simulador foi feito em Java 17. Em vez de usar o tempo real do computador, trabalhamos com ciclos (ticks): cada ciclo da CPU executa uma instrução do processo atual. Assim o resultado não depende da velocidade da máquina e, com a mesma entrada, sempre sai igual.

### Entidades

- **Processo:** guarda o ID, a quantidade de instruções (total e restantes), a prioridade (1 a 5, sendo 5 a maior) e os instantes de chegada, início e término. A cada execução na CPU, uma instrução é decrementada; quando chega a zero, o processo está finalizado. O tempo de espera é calculado como término − chegada − instruções totais.
- **GeradorDeProcessos:** cria os processos com ID incremental, instruções sorteadas entre 10 e 50 e prioridade sorteada entre 1 e 5. Usamos uma semente fixa (42) e cópias da mesma lista, para que todos os algoritmos recebam exatamente os mesmos processos e os mesmos instantes de chegada.
- **Escalonador:** mantém a fila de processos prontos e escolhe o próximo a ir para a CPU.
- **CPU:** a cada ciclo, coloca na fila os processos que chegaram, pede um processo ao escalonador (se não houver nenhum, espera), executa uma instrução e mostra na tela os processos em execução, aguardando e finalizados. Também tem um `sleep` opcional por ciclo para dar para acompanhar a execução.
- **Simulação:** roda os seis cenários (FCFS, SJF, Prioridade e Round Robin com quantum 2, 5 e 10), cada um com uma cópia nova dos processos, e monta a tabela comparativa.

### Algoritmos

- **FCFS:** atende os processos na ordem de chegada. Não é preemptivo.
- **SJF:** escolhe o processo com menos instruções restantes. Não é preemptivo; em caso de empate, vale quem chegou primeiro.
- **Prioridade:** escolhe o processo de maior prioridade. Não é preemptivo; em caso de empate, vale quem chegou primeiro.
- **Round Robin:** cada processo usa a CPU por no máximo um quantum. Se não terminar, volta para o fim da fila.

A CPU não sabe qual algoritmo está rodando. Ela só pergunta ao escalonador qual é o quantum e quem é o próximo. Nos algoritmos não preemptivos o quantum é praticamente infinito, então o processo só sai da CPU quando termina.

Uma troca de processo é contada quando o processo que entra na CPU é diferente do último que executou.

## 3.2. Resultados

Os testes usaram 10 processos, num total de 246 instruções:

| Processo | Instruções | Prioridade | Chegada |
| :--- | :---: | :---: | :---: |
| P1 | 24 | 4 | 0 |
| P2 | 26 | 1 | 0 |
| P3 | 36 | 4 | 1 |
| P4 | 15 | 3 | 2 |
| P5 | 24 | 3 | 4 |
| P6 | 44 | 2 | 6 |
| P7 | 39 | 5 | 6 |
| P8 | 17 | 2 | 8 |
| P9 | 10 | 2 | 11 |
| P10 | 11 | 3 | 15 |

| Algoritmo | Ordem de finalização | Espera média (ticks) | Trocas de processo |
| :--- | :--- | :---: | :---: |
| FCFS | P1, P2, P3, P4, P5, P6, P7, P8, P9, P10 | 117,00 | 9 |
| SJF | P1, P9, P10, P4, P8, P5, P2, P3, P7, P6 | 78,00 | 9 |
| Prioridade | P1, P7, P3, P4, P5, P10, P6, P8, P9, P2 | 115,70 | 9 |
| Round Robin (quantum 2) | P9, P10, P4, P8, P1, P5, P2, P3, P7, P6 | 153,50 | 123 |
| Round Robin (quantum 5) | P9, P4, P10, P1, P8, P5, P2, P3, P7, P6 | 153,30 | 52 |
| Round Robin (quantum 10) | P9, P4, P8, P1, P10, P2, P5, P3, P7, P6 | 155,20 | 28 |

Nos três algoritmos não preemptivos a ordem de execução é igual à de finalização, porque cada processo roda até o fim. No Round Robin os processos se alternam várias vezes; a sequência completa de execução e o diagrama de Gantt de cada cenário estão em `RELATORIO_DADOS.md`.

## 3.3. Análise

**1. Qual algoritmo teve o menor tempo médio de espera?**
O SJF, com 78,00 ticks. Depois que o P1 terminou, ele passou na frente os processos mais curtos (P9, P10, P4 e P8), e isso tirou rapidamente vários processos da fila. O FCFS ficou em 117,00 e o Prioridade em 115,70.

**2. Como o quantum influenciou o Round Robin?**
Quanto menor o quantum, mais trocas: foram 123 com quantum 2, 52 com quantum 5 e 28 com quantum 10. Por outro lado, com quantum 2 todos os processos pegaram a CPU logo no início (o P10 começou no tick 32), enquanto com quantum 10 o P10 só começou no tick 100. A espera média mudou pouco (153,50, 153,30 e 155,20), mas o quantum 5 teve o melhor equilíbrio: muito menos trocas que o 2, com a mesma espera média.

**3. Qual algoritmo fez mais trocas de processo?**
O Round Robin com quantum 2, com 123 trocas. Os algoritmos não preemptivos fizeram só 9, porque cada processo roda até terminar (10 processos, 9 trocas).

**4. Vantagens e desvantagens**

- **FCFS:** é simples e justo na ordem de chegada. O problema é que processos curtos ficam presos atrás de longos; o P9, com 10 instruções, esperou 214 ticks.
- **SJF:** dá a menor espera média. Em um sistema real seria preciso saber quanto cada processo vai demorar, e os processos longos podem nunca ser atendidos se chegarem sempre processos curtos.
- **Prioridade:** atende primeiro o que é mais importante; o P7, de prioridade 5, esperou 18 ticks, contra 163 no FCFS. Em compensação, o P2, de prioridade 1, chegou no tick 0 e só começou no tick 220.
- **Round Robin:** divide a CPU de forma justa e todo mundo começa a ser atendido cedo. Em compensação, a espera média e o número de trocas são maiores, principalmente com quantum pequeno.

Também existe o custo de decidir. FCFS e Round Robin só pegam o primeiro da fila. SJF e Prioridade precisam olhar a fila inteira a cada escolha. Num sistema real isso gasta CPU; no simulador, o tempo de decisão foi considerado zero.

**5. Qual é o mais adequado para sistemas interativos?**
O Round Robin. Em um sistema interativo o que importa é o usuário ser atendido rápido, e nos outros algoritmos um processo longo pode segurar a CPU por dezenas de ciclos. No Round Robin todos recebem a CPU em intervalos curtos e regulares. Um quantum médio, como 5, evita tanto o excesso de trocas quanto o comportamento de FCFS.

## 3.4. Conclusão

O SJF foi o melhor em tempo médio de espera (78,00 ticks), mas depende de saber o tamanho dos processos e pode deixar os longos esperando. O FCFS e o Prioridade ficaram parecidos (117,00 e 115,70); o Prioridade favoreceu os processos importantes, mas deixou o P2 esperar até o fim. O Round Robin teve a maior espera média (entre 153,30 e 155,20), porém foi o que atendeu todos os processos mais cedo, e o quantum define o equilíbrio entre responsividade e número de trocas.

Não existe um algoritmo melhor em tudo: a escolha depende do objetivo, seja menor espera média, atender o que é urgente ou manter o sistema responsivo.
