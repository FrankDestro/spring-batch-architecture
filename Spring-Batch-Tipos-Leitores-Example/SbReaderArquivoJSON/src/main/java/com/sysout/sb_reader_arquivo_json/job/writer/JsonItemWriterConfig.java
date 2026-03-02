package com.sysout.sb_reader_arquivo_json.job.writer;

import com.sysout.sb_reader_arquivo_json.job.domain.User;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class JsonItemWriterConfig {

    @Bean
    public JsonFileItemWriter<User> jsonItemWriter() {
        return new JsonFileItemWriterBuilder<User>()
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                .resource(new FileSystemResource("output.json"))
                .name("jsonItemWriter")
                .build();
    }
}
