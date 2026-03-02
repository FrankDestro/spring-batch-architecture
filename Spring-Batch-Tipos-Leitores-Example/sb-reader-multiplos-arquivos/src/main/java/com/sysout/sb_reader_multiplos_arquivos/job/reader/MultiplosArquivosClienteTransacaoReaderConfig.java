package com.sysout.sb_reader_multiplos_arquivos.job.reader;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class MultiplosArquivosClienteTransacaoReaderConfig {
    /**
     * ItemReader responsável por orquestrar a leitura de múltiplos arquivos.
     *
     * - Recebe uma lista de Resources via JobParameters
     * - Itera arquivo por arquivo
     * - Para cada arquivo, delega a leitura para um ItemReader customizado
     *
     * OBS: Este reader NÃO lê linhas diretamente.
     * Ele apenas controla o ciclo de leitura dos arquivos.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @StepScope
    @Bean
    public MultiResourceItemReader multiplosArquivosClienteTransacaoReader (
            @Value("#{jobParameters['arquivosClientes']}") Resource[] arquivosClientes,
            FlatFileItemReader leituraArquivoMultiplosFormatosReader) {
        return new MultiResourceItemReaderBuilder<>()
                .name("multiplosArquivosClienteTransacaoReader")
                .resources(arquivosClientes)
                // Delegate responsável pela leitura real do conteúdo do arquivo
                // e pelo agrupamento Cliente + Transações
                .delegate(new ArquivoClienteTransacaoReader(leituraArquivoMultiplosFormatosReader)) // Delegando para um leitor customizado
                .build();
    }
}
