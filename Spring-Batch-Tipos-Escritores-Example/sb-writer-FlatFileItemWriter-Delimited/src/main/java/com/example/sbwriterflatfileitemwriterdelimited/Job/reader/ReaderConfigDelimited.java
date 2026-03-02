package com.example.sbwriterflatfileitemwriterdelimited.Job.reader;

import com.example.sbwriterflatfileitemwriterdelimited.Job.Domain.Client;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class ReaderConfigDelimited {

    @Bean
    @StepScope
    public FlatFileItemReader<Client> readerArquivosDelimited(
            @Value("#{jobParameters['arquivoClientes']}") Resource arquivoClientes) {

        return new FlatFileItemReaderBuilder<Client>()
                .name("clientReader")
                .resource(arquivoClientes)
                .delimited()
                .names("nome", "sobrenome", "idade", "email")
                .targetType(Client.class)
                .build();
    }
}
