package com.sysout.job_demonstrativo_orcamentario.Job.writer;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WriterDemonstrativoOrcamentarioConfig {

    @Bean
    @StepScope
    public ItemWriter writerDemonstrativoOrcamentario() {
        return items -> items.forEach(System.out::println);
    }
}
