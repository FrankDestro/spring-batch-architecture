package com.example.sbwriterflatfileitemwriter.Job.writer;

import com.example.sbwriterflatfileitemwriter.Job.Domain.Client;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;


@Configuration
public class WriterConfigArquivoLarguraFixa {

    @Bean
    @StepScope
    public FlatFileItemWriter<Client> writerArquivoLarguraFixa(
            @Value("#{jobParameters['arquivoClientesSaida']}") WritableResource arquivosClientesSaida) {
        return new FlatFileItemWriterBuilder<Client>()
                .name("writerArquivoLarguraFixa")
                .resource(arquivosClientesSaida)
                .formatted()
                .format("%-9s %-9s %-2s %-19s")
                .names(new String[]{"nome", "sobrenome", "idade", "email"})
                .build();
    }
}
