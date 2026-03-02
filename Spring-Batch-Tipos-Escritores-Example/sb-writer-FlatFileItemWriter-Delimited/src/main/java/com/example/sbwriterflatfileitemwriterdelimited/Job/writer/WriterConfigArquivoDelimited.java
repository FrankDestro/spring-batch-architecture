package com.example.sbwriterflatfileitemwriterdelimited.Job.writer;

import com.example.sbwriterflatfileitemwriterdelimited.Job.Domain.Client;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.WritableResource;


@Configuration
public class WriterConfigArquivoDelimited {

    @Bean
    @StepScope
    public FlatFileItemWriter<Client> writerArquivoDelimited(
            @Value("#{jobParameters['arquivoClientesSaida']}") WritableResource arquivosClientesSaida) {

        return new FlatFileItemWriterBuilder<Client>()
                .name("writerArquivoDelimited")
                .resource(arquivosClientesSaida)
                .delimited()
                .delimiter(";")
                .names(new String[]{"nome", "sobrenome", "idade", "email"})
                .build();
    }
}
