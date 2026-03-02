package com.sysout.sb_processador_script.job.writer;


import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessadorScriptWriterConfig {

    @Bean
    @StepScope
    public ItemWriter processadorScriptWriter() {
        return items -> items.forEach(System.out::println);

    }
}
