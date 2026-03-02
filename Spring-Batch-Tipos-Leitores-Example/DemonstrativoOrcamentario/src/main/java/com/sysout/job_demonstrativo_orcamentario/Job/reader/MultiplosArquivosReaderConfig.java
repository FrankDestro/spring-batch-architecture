package com.sysout.job_demonstrativo_orcamentario.Job.reader;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class MultiplosArquivosReaderConfig {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @StepScope
    @Bean
    public MultiResourceItemReader multiplosArquivosLancamentos(
            @Value("#{jobParameters['lancamentos']}") Resource[] lancamentos,
            FlatFileItemReader demonstrativoOrcamentarioReader) {
        return new MultiResourceItemReaderBuilder()
                .name("multiplosArquivosLancamentos")
                .resources(lancamentos)
                .delegate(demonstrativoOrcamentarioReader)
                .build();
    }
}
