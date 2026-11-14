# Dados Extraídos da Simulação (Seed 42)

## 1. Sequência de Processos Utilizada nos Testes

| Processo | Quantidade de Instruções | Prioridade (1-5) | Instante de Chegada (tick) |
| :--- | :---: | :---: | :---: |
| **P1** | 24 | 4 | 0 |
| **P2** | 26 | 1 | 0 |

**Total de instruções da carga:** 50 instruções.

## 2. Tabela Comparativa dos Algoritmos

| Algoritmo | Ordem de Finalização | Tempo Médio de Espera (ticks) | Tempo Médio de Retorno (ticks) | Trocas de Processos | Ordem de Execução (Despachos na CPU) |
| :--- | :--- | :---: | :---: | :---: | :--- |
| **FCFS (First Come, First Served)** | `P1 -> P2` | **12.00** | 37.00 | **1** | `P1 -> P2` |
| **SJF (Shortest Job First)** | `P1 -> P2` | **12.00** | 37.00 | **1** | `P1 -> P2` |
| **Prioridade (Não Preemptivo)** | `P1 -> P2` | **12.00** | 37.00 | **1** | `P1 -> P2` |
| **Round Robin (Quantum 2)** | `P1 -> P2` | **23.00** | 48.00 | **23** | `P1 -> P2 -> P1 -> P2 -> P1 -> P2 -> P1 -> P2 -> P1 -> P2 -> P1 -> P2 -> P1 -> P2 -> P1 -> P2 -> P1 -> P2 -> P1 -> P2 -> P1 -> P2 -> P1 -> P2` |
| **Round Robin (Quantum 5)** | `P1 -> P2` | **22.00** | 47.00 | **9** | `P1 -> P2 -> P1 -> P2 -> P1 -> P2 -> P1 -> P2 -> P1 -> P2` |
| **Round Robin (Quantum 10)** | `P1 -> P2` | **22.00** | 47.00 | **5** | `P1 -> P2 -> P1 -> P2 -> P1 -> P2` |

## 3. Detalhamento Individual por Processo em Cada Algoritmo

### FCFS (First Come, First Served)

| Processo | Chegada | Instruções | Prioridade | Início | Término | Tempo de Espera | Tempo de Retorno |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| P1 | 0 | 24 | 4 | 0 | 24 | 0 | 24 |
| P2 | 0 | 26 | 1 | 24 | 50 | 24 | 50 |

**Diagrama de Gantt:** `[0..24: P1] [24..50: P2]`

### SJF (Shortest Job First)

| Processo | Chegada | Instruções | Prioridade | Início | Término | Tempo de Espera | Tempo de Retorno |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| P1 | 0 | 24 | 4 | 0 | 24 | 0 | 24 |
| P2 | 0 | 26 | 1 | 24 | 50 | 24 | 50 |

**Diagrama de Gantt:** `[0..24: P1] [24..50: P2]`

### Prioridade (Não Preemptivo)

| Processo | Chegada | Instruções | Prioridade | Início | Término | Tempo de Espera | Tempo de Retorno |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| P1 | 0 | 24 | 4 | 0 | 24 | 0 | 24 |
| P2 | 0 | 26 | 1 | 24 | 50 | 24 | 50 |

**Diagrama de Gantt:** `[0..24: P1] [24..50: P2]`

### Round Robin (Quantum 2)

| Processo | Chegada | Instruções | Prioridade | Início | Término | Tempo de Espera | Tempo de Retorno |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| P1 | 0 | 24 | 4 | 0 | 46 | 22 | 46 |
| P2 | 0 | 26 | 1 | 2 | 50 | 24 | 50 |

**Diagrama de Gantt:** `[0..2: P1] [2..4: P2] [4..6: P1] [6..8: P2] [8..10: P1] [10..12: P2] [12..14: P1] [14..16: P2] [16..18: P1] [18..20: P2] [20..22: P1] [22..24: P2] [24..26: P1] [26..28: P2] [28..30: P1] [30..32: P2] [32..34: P1] [34..36: P2] [36..38: P1] [38..40: P2] [40..42: P1] [42..44: P2] [44..46: P1] [46..50: P2]`

### Round Robin (Quantum 5)

| Processo | Chegada | Instruções | Prioridade | Início | Término | Tempo de Espera | Tempo de Retorno |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| P1 | 0 | 24 | 4 | 0 | 44 | 20 | 44 |
| P2 | 0 | 26 | 1 | 5 | 50 | 24 | 50 |

**Diagrama de Gantt:** `[0..5: P1] [5..10: P2] [10..15: P1] [15..20: P2] [20..25: P1] [25..30: P2] [30..35: P1] [35..40: P2] [40..44: P1] [44..50: P2]`

### Round Robin (Quantum 10)

| Processo | Chegada | Instruções | Prioridade | Início | Término | Tempo de Espera | Tempo de Retorno |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| P1 | 0 | 24 | 4 | 0 | 44 | 20 | 44 |
| P2 | 0 | 26 | 1 | 10 | 50 | 24 | 50 |

**Diagrama de Gantt:** `[0..10: P1] [10..20: P2] [20..30: P1] [30..40: P2] [40..44: P1] [44..50: P2]`

