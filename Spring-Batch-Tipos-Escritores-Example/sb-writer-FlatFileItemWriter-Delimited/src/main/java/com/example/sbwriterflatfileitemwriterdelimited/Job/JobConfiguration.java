package com.example.sbwriterflatfileitemwriterdelimited.Job;

import com.example.sbwriterflatfileitemwriterdelimited.Job.Domain.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
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
    public Job jobArquivoDelimited(Step stepArquivoDelimited) {
        return new JobBuilder("jobArquivoDelimited", jobRepository)
                .start(stepArquivoDelimited)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step stepArquivoDelimited(
            @Qualifier("readerArquivosDelimited") ItemReader<Client> readerArquivosDelimited,
            @Qualifier("writerArquivoDelimited") ItemWriter<Client> writerArquivoDelimited
    ) {
        PlatformTransactionManager txManager = new ResourcelessTransactionManager();
        return new StepBuilder("step", jobRepository)
                .<Client, Client>chunk(4, txManager)
                .reader(readerArquivosDelimited)
                .writer(writerArquivoDelimited)
                .build();
    }
}
