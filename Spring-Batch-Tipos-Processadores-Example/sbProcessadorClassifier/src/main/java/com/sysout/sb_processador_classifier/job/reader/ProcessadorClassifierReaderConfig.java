package com.sysout.sb_processador_classifier.job.reader;

import com.sysout.sb_processador_classifier.job.domain.Cliente;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class ProcessadorClassifierReaderConfig {

    @StepScope
    @Bean
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public FlatFileItemReader<Cliente> processadorClassifierReader(
            @Value("#{jobParameters['arquivoClientes']}") Resource arquivoClientes, LineMapper lineMapper) {
        return new FlatFileItemReaderBuilder<Cliente>()
                .name("processadorClassifierReader")
                .resource(arquivoClientes)
                .lineMapper(lineMapper)
                .build();
    }
}
