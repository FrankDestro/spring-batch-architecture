package com.sysout.sb_reader_arquivo_json.job.reader;

import com.sysout.sb_reader_arquivo_json.job.domain.User;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class JsonItemReaderConfig {

    @Bean
    @StepScope
    public JsonItemReader<User> jsonItemReader(
            @Value("#{jobParameters['arquivoEmployees']}") Resource arquivoEmployee) {

        return new JsonItemReaderBuilder<User>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(User.class))
                .resource(arquivoEmployee)
                .name("jsonItemReader")
                .build();
    }
}
