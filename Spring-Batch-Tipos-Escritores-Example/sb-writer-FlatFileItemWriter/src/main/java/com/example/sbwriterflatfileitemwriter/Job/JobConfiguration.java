package com.example.sbwriterflatfileitemwriter.Job;

import com.example.sbwriterflatfileitemwriter.Job.Domain.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class JobConfiguration {

    private final JobRepository jobRepository;

    @Bean
    public Job jobArquivoLarguraFixa(Step stepArquivoLarguraFixa) {
        return new JobBuilder("jobArquivoLarguraFixa", jobRepository)
                .start(stepArquivoLarguraFixa)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step stepArquivoLarguraFixa(
            @Qualifier("readerArquivosLarguraFixa") ItemReader<Client> readerArquivosLarguraFixa,
            @Qualifier("writerArquivoLarguraFixa") FlatFileItemWriter<Client> writerArquivoLarguraFixa
    ) {
        PlatformTransactionManager txManager = new ResourcelessTransactionManager();
        return new StepBuilder("step", jobRepository)
                .<Client, Client>chunk(4, txManager)
                .reader(readerArquivosLarguraFixa)
                .writer(writerArquivoLarguraFixa)
                .build();
    }
}
