# 📘 Spring Batch — Resumo Profissional

Spring Batch é um framework projetado para o processamento robusto, eficiente e escalável de grandes volumes de dados.
Ele organiza a execução em Jobs, compostos por Steps, e utiliza recursos como chunking, paralelismo e persistência de metadados para garantir confiabilidade.

🔹 1. Conceitos Fundamentais

## 🧱 Job

Um Job representa o processo completo a ser executado em um batch.

* Exemplos: Importar usuários, Gerar relatórios, Processar pedidos,

### Características:

Composto por uma sequência de Steps, possui estados como: STARTING, STARTED, COMPLETED, FAILED e pode ser reiniciado com base nos metadados armazenados

## 🧩 Step

Um Step é uma fase do Job.
Cada Step implementa uma parte específica do processamento.

Tipos de Steps:

✔️ Tasklet Step: Executa uma única ação.
* Exemplos: deletar arquivos, chamar API, mover arquivos.


### Exemplo de implemtação com Tasklet Step

```java
    @Bean
    public Job imprimeOlaJob (JobRepository jobRepository, Step imprimeOlaStep) {
        return new JobBuilder("imprimeOlaJob", jobRepository)
                .start(imprimeOlaStep)
                .incrementer(new RunIdIncrementer()) // Permite executar o job varias vezes.
                .build();
    }

    // STEP
    @Bean
    public Step imprimeOlaStep (JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("imprimeOlaStep", jobRepository)
                .tasklet((StepContribution contribution, ChunkContext chunkContext) -> {
                    System.out.println("Ola Mundo!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
```

✔️ Chunk-Oriented Step: Processa dados em blocos (chunks), ideal para grandes volumes.

### 📦 Chunk

Chunk é o modo de processamento dentro de um Step baseado na leitura, processamento e gravação em lotes.

Fluxo de um Chunk: 

1. Ler N itens (ItemReader)

2. Processar N itens (ItemProcessor)

3. Gravar N itens (ItemWriter)

<img width="749" height="294" alt="image" src="https://github.com/user-attachments/assets/e79aee48-3ec3-462d-81b5-69a2b0b200f8" />

Benefícios: Alto desempenho, Menos transações, Suporte a restart sem recomeçar do zero

Exemplo de implementação com Chunk Step

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
    Step imprimeParImparStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("imprimeParImparStep", jobRepository)
                .<Integer, String>chunk(1, transactionManager)
                .reader(contaAteDezReader())
                .processor(parOuImparProcessor())
                .writer(parOuImparWriter())
                .build();
    }

    private ItemReader<Integer> contaAteDezReader() {
        List<Integer> numerosDeUmAteDez = List.of(1,2,3,4,5,6,7,8,9,10);
        return new ListItemReader<>(numerosDeUmAteDez);
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




🔹 2. Mecanismo de Restart (Checkpoint)

O Spring Batch salva metadados no JobRepository, como: Último chunk processado, Status do step, Parâmetros usados, Erros ocorridos

Resultado: Se a execução for interrompida, ao reiniciar O Job continua exatamente de onde parou. é literalmente um checkpoint, como em videogames.

🔹 3. JobRepository

O JobRepository armazena os metadados de execução do batch.

Ele registra:

* Execuções de Jobs

* Execuções de Steps

* Contextos e variáveis

* Checkpoints

* Sucessos e falhas

* Importante: O Spring gerencia isso automaticamente, Você apenas configura a fonte de dados, Essencial para: restart, Paralelismo e Controle transacional

Estrutura da Tabela gerada automáticamente pelo JobRepository

| Tabela                     | Descrição                                                                                   |
|----------------------------|---------------------------------------------------------------------------------------------|
| BATCH_JOB_INSTANCE         | Armazena as instâncias de Jobs, identificando cada execução distinta com parâmetros únicos. |
| BATCH_JOB_EXECUTION        | Registra cada execução do Job, com dados como status, data de início e fim, e informações de falha. |
| BATCH_JOB_EXECUTION_PARAMS | Guarda os parâmetros usados na execução do Job (JobParameters).                             |
| BATCH_STEP_EXECUTION       | Registra cada execução de Step dentro de um Job, incluindo status, data e métricas.         |
| BATCH_STEP_EXECUTION_CONTEXT | Armazena o contexto de execução do Step — dados persistidos para possibilitar restart e checkpoint. |
| BATCH_JOB_EXECUTION_CONTEXT | Guarda o contexto da execução do Job — semelhante ao Step, mas em nível de Job.            |
| BATCH_STEP_EXECUTION_SEQ   | Sequenciador para a chave primária de BATCH_STEP_EXECUTION (usado em alguns bancos).        |
| BATCH_JOB_EXECUTION_SEQ    | Sequenciador para a chave primária de BATCH_JOB_EXECUTION (usado em alguns bancos).         |



///////////////////////////////////////////////////////////////


























🔹 4. Paralelismo no Spring Batch

Spring Batch oferece duas formas de escalar seu processamento:

⚙️ Escala Vertical (no mesmo Job)

Steps paralelos

Step multithread, processando chunks com múltiplas threads

Indicada quando o servidor possui boa capacidade de CPU/RAM.

🌐 Escala Horizontal (várias máquinas)

Particionamento remoto

Processamento distribuído com RabbitMQ/Kafka

Vários workers processando partes distintas do dataset

Ideal para clusters, Kubernetes e grandes volumes.

Observação:

Jobs distintos podem rodar em paralelo sem problema, mesmo se seus Steps forem paralelos internamente.

🔹 5. Spring Batch no ETL

Spring Batch é amplamente usado para cenários de ETL:

Extract

Ler dados de arquivos, bancos, APIs, streams…

Transform

Validar, limpar, enriquecer, converter, aplicar regras de negócio.

Load

Salvar em banco, gerar arquivos, publicar em filas…

🔹 6. Spring Cloud Data Flow

Ferramenta para orquestrar Jobs do Spring Batch e pipelines de dados.

Recursos:

Painel web

Deploy distribuído

Monitoramento

Agendamentos

Versionamento

Execução em Kubernetes

Usado em soluções de dados corporativas e de larga escala.
