package com.sysout.sb_processador_classifier.job.writer;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessadorClassifierWriterConfig {

    @Bean
    @StepScope
    public ItemWriter processadorClassifierWriter() {
        return items -> items.forEach(System.out::println);

    }
}
