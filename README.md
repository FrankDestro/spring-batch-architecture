# 📘 Spring Batch — Guia Profissional e Arquitetural

Spring Batch é um framework do ecossistema Spring projetado para processamento robusto, eficiente e escalável de grandes volumes de dados.

Ele organiza a execução em **Jobs**, compostos por **Steps**, utilizando recursos como:

- Chunk Processing
- Controle transacional
- Persistência de metadados
- Restart automático
- Paralelismo
- Processamento distribuído

---

## 📑 Sumário

- [1. Conceitos Fundamentais](#1-conceitos-fundamentais)
- [2. Chunk Processing](#2-chunk-processing)
- [3. Mecanismo de Restart (Checkpoint)](#3-mecanismo-de-restart-checkpoint)
- [4. JobRepository](#4-jobrepository)
- [5. Paralelismo](#5-paralelismo)
- [6. ETL com Spring Batch](#6-etl-com-spring-batch)
- [7. Spring Cloud Data Flow](#7-spring-cloud-data-flow)
- [8. Transações no Spring Batch](#8-transações-no-spring-batch)
  - [8.1 Cenário 1 — Transação Simples](#81-cenário-1--transação-simples)
  - [8.2 Cenário 2 — Transação Distribuída (2PC / XA)](#82-cenário-2--transação-distribuída-2pc--xa)

---

# 1. Conceitos Fundamentais

## 🧱 Job

Um **Job** representa o processo completo de execução de um batch.

### Exemplos:

- Importar usuários
- Gerar relatórios
- Processar pedidos
- Consolidar dados financeiros

### Características

- Composto por uma sequência de **Steps**
- Possui estados como:
  - STARTING
  - STARTED
  - COMPLETED
  - FAILED
- Pode ser reiniciado com base nos metadados persistidos

---

## 🧩 Step

Um **Step** representa uma fase dentro de um Job.

Cada Step executa uma parte específica do processamento.

### Tipos de Step

### ✔️ Tasklet Step

Executa uma única ação.

#### Exemplos:

- Deletar arquivos
- Chamar API
- Mover arquivos
- Executar script

#### Exemplo de implementação

```java
@Bean
public Job imprimeOlaJob(JobRepository jobRepository, Step imprimeOlaStep) {
    return new JobBuilder("imprimeOlaJob", jobRepository)
            .start(imprimeOlaStep)
            .incrementer(new RunIdIncrementer())
            .build();
}

@Bean
public Step imprimeOlaStep(JobRepository jobRepository,
                           PlatformTransactionManager transactionManager) {
    return new StepBuilder("imprimeOlaStep", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                System.out.println("Olá Mundo!");
                return RepeatStatus.FINISHED;
            }, transactionManager)
            .build();
}
```

---

### ✔️ Chunk-Oriented Step

Processa dados em blocos (chunks), ideal para grandes volumes.

---

# 2. Chunk Processing

## 📦 O que é um Chunk?

Chunk é o modelo de processamento baseado em blocos dentro de um Step.

### Fluxo de execução:

1. Ler N itens (ItemReader)
2. Processar N itens (ItemProcessor)
3. Gravar N itens (ItemWriter)
4. Commit da transação

### Benefícios

- Alto desempenho
- Redução de transações
- Restart sem reprocessar tudo

---

### Arquitetura de um processo com Chuck 

<img width="1418" height="828" alt="image" src="https://github.com/user-attachments/assets/526cb56d-3a2f-4284-a1d6-904161808621" />

---

## Exemplo de implementação com Chunk Step

```java
@Configuration
public class ParImparBatchConfig {

    @Bean
    public Job imprimeParImparJob(JobRepository jobRepository, Step imprimeParImparStep) {
        return new JobBuilder("imprimeParImparJob", jobRepository)
                .start(imprimeParImparStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    Step imprimeParImparStep(JobRepository jobRepository,
                             PlatformTransactionManager transactionManager) {
        return new StepBuilder("imprimeParImparStep", jobRepository)
                .<Integer, String>chunk(1, transactionManager)
                .reader(contaAteDezReader())
                .processor(parOuImparProcessor())
                .writer(parOuImparWriter())
                .build();
    }

    private ItemReader<Integer> contaAteDezReader() {
        List<Integer> numeros = List.of(1,2,3,4,5,6,7,8,9,10);
        return new ListItemReader<>(numeros);
    }

    private ItemProcessor<? super Integer, String> parOuImparProcessor() {
        return item -> item % 2 == 0
                ? String.format("Item %s é Par", item)
                : String.format("Item %s é Ímpar", item);
    }

    private ItemWriter<? super String> parOuImparWriter() {
        return itens -> itens.forEach(System.out::println);
    }
}
```

---

# 3. Mecanismo de Restart (Checkpoint)

O Spring Batch salva metadados no **JobRepository**, como:

- Último chunk processado
- Status do Step
- Parâmetros usados
- Erros ocorridos

### Resultado

Se a execução for interrompida, ao reiniciar:

O Job continua exatamente de onde parou.

É literalmente um sistema de checkpoint.

---

# 4. JobRepository

O **JobRepository** armazena os metadados de execução do batch.

Ele registra:

- Execuções de Jobs
- Execuções de Steps
- Contextos (ExecutionContext)
- Checkpoints
- Sucessos e falhas

Você apenas configura a fonte de dados.  
O Spring gerencia o restante automaticamente.

---

## Estrutura das Tabelas Geradas

| Tabela | Descrição |
|--------|------------|
| BATCH_JOB_INSTANCE | Identifica instâncias únicas de Job |
| BATCH_JOB_EXECUTION | Registra cada execução do Job |
| BATCH_JOB_EXECUTION_PARAMS | Guarda JobParameters |
| BATCH_STEP_EXECUTION | Registra execuções de Step |
| BATCH_STEP_EXECUTION_CONTEXT | Contexto persistido do Step |
| BATCH_JOB_EXECUTION_CONTEXT | Contexto persistido do Job |
| BATCH_STEP_EXECUTION_SEQ | Sequenciador de Step |
| BATCH_JOB_EXECUTION_SEQ | Sequenciador de Job |

---

# 5. Paralelismo

O Spring Batch permite escalabilidade de duas formas:

## ⚙️ Escala Vertical

- Steps paralelos
- Step multithread
- Processamento simultâneo de chunks

Indicado quando o servidor possui boa capacidade de CPU/RAM.

---

## 🌐 Escala Horizontal

- Particionamento remoto
- Processamento distribuído
- Integração com RabbitMQ ou Kafka
- Execução em múltiplos workers

Indicado para clusters e Kubernetes.

---

# 6. ETL com Spring Batch

Spring Batch é amplamente utilizado em cenários ETL.

## Extract
Ler dados de arquivos, bancos, APIs ou streams.

## Transform
Validar, limpar, enriquecer e aplicar regras de negócio.

## Load
Persistir dados em banco, gerar arquivos ou publicar em filas.

---

# 7. Spring Cloud Data Flow

Ferramenta para orquestrar Jobs e pipelines de dados baseados em Spring.

### Recursos

- Painel web
- Deploy distribuído
- Monitoramento
- Agendamento
- Versionamento
- Execução em Kubernetes

Utilizado em soluções corporativas e arquiteturas de dados em larga escala.


---

# 8. Transações no Spring Batch

Spring Batch depende fortemente de controle transacional para garantir:

- Integridade dos dados
- Atomicidade do chunk
- Consistência em caso de falha
- Capacidade de restart

Nesta seção são apresentados dois cenários:

1. Transação simples (múltiplos bancos sem XA)
2. Transação distribuída (2-Phase Commit / XA)

---

## 8.1 Cenário 1 — Transação Simples (JobRepository + DataSource de Negócio)

### ✔ Quando ocorre

Este cenário acontece quando a aplicação possui mais de um banco de dados.

Exemplo comum:

- Banco A → usado pelo **JobRepository** (metadados do Spring Batch)
- Banco B → usado para dados de negócio (clientes, pedidos, arquivos processados etc.)

Mesmo que existam dois bancos, não há transação distribuída — cada operação usa apenas um recurso por vez.

---

### ✔ Problema padrão do Spring Batch

Por padrão, o Spring Batch utiliza o **TransactionManager associado ao JobRepository**.

Se o Step tentar gravar no Banco B, mas o TransactionManager estiver apontando para o Banco A:

- A transação não será gerenciada corretamente
- Pode ocorrer erro de commit
- Pode haver rollback no banco errado

---

### ✔ Consequência prática

Se mal configurado:

- Chunk pode não ser atômico
- Pode ocorrer gravação parcial
- Inconsistência entre metadados e dados de negócio
- Restart pode falhar

---

### ✔ Solução correta

Criar um **TransactionManager específico** para o DataSource de negócio e associá-lo explicitamente ao Step.

---

### Exemplo

```java
@Bean
public PlatformTransactionManager businessTxManager(
        @Qualifier("businessDataSource") DataSource ds) {
    return new DataSourceTransactionManager(ds);
}

@Bean
public Step clienteStep(StepBuilderFactory factory,
                        @Qualifier("businessTxManager") PlatformTransactionManager txManager) {

    return factory.get("clienteStep")
            .<Cliente, Cliente>chunk(300)
            .transactionManager(txManager)
            .build();
}
```

---

### ✔ Visão Geral — Mapa Mental

O diagrama abaixo demonstra a estrutura de uma aplicação Spring Batch com múltiplos bancos de dados no Cenário 1. A utilização do @Qualifier permite amarrar explicitamente cada DataSource ao seu TransactionManager e ao Step correspondente. Essa prática é fundamental para evitar conflitos transacionais, garantir previsibilidade na execução dos Jobs e assegurar que cada operação seja executada no banco de dados correto, mesmo em cenários com múltiplos DataSources e um único JobRepository.

<img width="967" height="1097" alt="image" src="https://github.com/user-attachments/assets/c083978d-8d2e-4f0f-9657-5773f6e94806" />


Estrutura do cenário:

- JobRepository → Banco A
- Dados de negócio → Banco B
- Step explicitamente vinculado ao TransactionManager correto
- Uso de @Qualifier para evitar ambiguidades

Boa prática fundamental:

Sempre que houver múltiplos DataSources, declare explicitamente qual TransactionManager pertence a cada Step.

---

### ✔ Conclusão do Cenário 1

Sempre que existir mais de um banco na aplicação, mesmo sem XA, é recomendável:

- Criar TransactionManagers separados
- Declarar explicitamente o TransactionManager no Step
- Evitar depender da configuração automática do Spring

Isso garante integridade do chunk e previsibilidade transacional.




## 8.2 Cenário 2 — Transação Distribuída (2-Phase Commit / XA)

### Arquitetura do cenário 2 

<img width="1252" height="621" alt="image" src="https://github.com/user-attachments/assets/ef82a8f5-4ad1-448a-b693-29019ed5e545" />

### ✔ Quando ocorre

Este cenário acontece quando o mesmo chunk precisa ser confirmado simultaneamente em múltiplos recursos transacionais.

Exemplos:

- Ler dados de uma fonte X
- Gravar o mesmo chunk em vários bancos ao mesmo tempo:
  - Banco 1 (clientes)
  - Banco 2 (auditoria)
  - Banco 3 (analytics)
  - Banco 4…8

Ou ainda:

- Banco + mensageria (Kafka, RabbitMQ, JMS)
- Banco + outro microserviço transacional
- Banco + serviço externo que exige confirmação transacional

---

### ✔ Por que exige 2-Phase Commit

Porque todos os recursos precisam confirmar a transação em conjunto.

Se apenas um recurso falhar:

→ Todos os demais devem executar rollback.

Isso garante consistência total entre os sistemas envolvidos.

---

### ✔ Características

- O Spring precisará utilizar um **TransactionManager XA**
- Os DataSources devem ser configurados com drivers XA
  - Atomikos
  - Narayana
  - Bitronix

#### Comportamento do Chunk no 2PC

1. **Prepare Phase**
   - Cada recurso confirma que está pronto para realizar o commit.

2. **Commit Phase**
   - O commit final só ocorre se todos os participantes confirmarem sucesso na fase anterior.

---

### ✔ Quando usar realmente

Apenas quando não é permitido qualquer tipo de inconsistência entre os recursos.

Exemplo:

- Escrita simultânea e obrigatória em múltiplos bancos críticos
- Cenários regulatórios ou financeiros onde consistência forte é mandatória

---

### ✔ Desvantagens

- Muito mais lento
- Muito mais complexo
- Aumenta latência do chunk
- Difícil de debugar
- Maior complexidade operacional

---

### ✔ Recomendação profissional

Utilize 2-Phase Commit somente quando for estritamente necessário.

Sempre que possível, prefira alternativas como:

- Eventual Consistency
- Arquitetura orientada a eventos
- Publicação de eventos após commit
- Estratégias de compensação


## 8.3 Papel do @Qualifier nos Cenários Transacionais

Sempre que existem dois ou mais beans do mesmo tipo (principalmente `DataSource` ou `PlatformTransactionManager`), o Spring não sabe qual deve injetar automaticamente.

Nesses casos, é obrigatório utilizar `@Qualifier` para indicar explicitamente qual bean deve ser utilizado.

---

### ✔ Por que é necessário

Em aplicações com múltiplos bancos, é comum existir:

- `DataSource` do JobRepository
- `DataSource` de negócio
- `TransactionManager` do JobRepository
- `TransactionManager` de negócio

Sem `@Qualifier`, o Spring lançará erro de ambiguidade de bean.

---

### ✔ Exemplo

```java
@Autowired
@Qualifier("businessDataSource")
private DataSource businessDataSource;
```

Sem o uso de `@Qualifier`, ocorrerá erro de injeção:

- NoUniqueBeanDefinitionException

---

### ✔ Boa prática

Sempre que houver múltiplos `DataSource` ou `TransactionManager`:

- Declare explicitamente os beans
- Nomeie corretamente
- Use `@Qualifier` na injeção
- Amarre explicitamente o TransactionManager no Step

Isso evita comportamento implícito e imprevisível.

---

# 8.4 Resumo Final

## ✔ Cenário 1 — Transação Simples (Multi-Banco)

- JobRepository → Banco A
- Dados de negócio → Banco B
- Necessário criar `TransactionManager` separado para cada banco
- Obrigatório definir `.transactionManager(...)` no Step

---

## ✔ Cenário 2 — 2-Phase Commit (XA)

- Múltiplos bancos precisam ser gravados juntos no mesmo chunk
- Necessário utilizar `XA Transaction Manager`
- DataSources devem ser XA
- Deve ser evitado sempre que possível devido à complexidade

---

## ✔ Regras Práticas

- Se existe mais de um banco → configure TransactionManagers separados
- Se o mesmo chunk grava em múltiplos bancos simultaneamente → pode exigir XA
- Sempre use `@Qualifier` quando houver múltiplos beans do mesmo tipo
- Evite depender de configuração automática em cenários multi-DataSource  

















































# 9. Arquitetura Interna (Avançado)

Esta seção aprofunda no funcionamento interno do framework.

---

## 9.1 Modelo de Execução Interno

### Fluxo completo de execução

(JobLauncher → Job → Step → Chunk.)

### Papel do JobLauncher

-

### Papel do JobRepository

-

---

## 9.2 Estruturas de Execução

### JobInstance

-

### JobExecution

-

### StepExecution

-

### ExecutionContext

-

---

## 9.3 Ciclo de Vida de um Step

### Estados possíveis

-

### Transições de estado

-

### Restart de Step

-

---

## 9.4 Controle Transacional Interno

### Onde a transação começa

-

### Onde ocorre commit

-

### Onde ocorre rollback

-

---

# 10. Problemas Reais e Aprendizados

Seção dedicada a experiências práticas e erros enfrentados.

---

## 10.1 Erro com @BeforeWrite

### Problema

-

### Causa

-

### Solução

-

---

## 10.2 Total duplicado com MultiResourceItemReader

### Cenário

-

### Diagnóstico

-

### Correção

-

---

## 10.3 Uso de FlatFileFooterCallback

### Objetivo

-

### Implementação

-

### Observações importantes

-

---

## 10.4 ResourcelessTransactionManager

### Quando utilizar

-

### Limitações

-

---

# 11. Boas Práticas

---

## 11.1 Organização de Jobs

-

## 11.2 Separação de responsabilidade

-

## 11.3 Configuração de commit interval

-

## 11.4 Monitoramento e logging

-

## 11.5 Estratégias para ambientes produtivos

-

---

# 12. Referências e Materiais de Estudo

- Documentação oficial
- Artigos técnicos
- Livros
- Experimentos próprios
- Repositórios de exemplo
