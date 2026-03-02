package com.sysout.job_demonstrativo_orcamentario.Job.reader;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class ReaderDemonstrativoOrcamentarioConfig {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @StepScope
    @Bean
    public FlatFileItemReader demonstrativoOrcamentarioReader(
            @Value("#{jobParameters['lancamentos']}") Resource lancamentos, LineMapper lancamentoLineMapper) {
        return new FlatFileItemReaderBuilder()
                .name("demonstrativoOrcamentarioReader")
                .resource(lancamentos)
                .lineMapper(lancamentoLineMapper)
                .build();

    }
}
