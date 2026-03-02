package com.sysout.sb_reader_arquivo_delimitado.job;

import com.sysout.sb_reader_arquivo_delimitado.job.domain.Cliente;
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

@Configuration
public class JobConfiguration {

    private final JobRepository jobRepository;

    public JobConfiguration(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Bean
    public Job jobArquivoDelimitado(Step stepArquivoDelimitado) {
        return new JobBuilder("jobArquivoDelimitado", jobRepository)
                .start(stepArquivoDelimitado)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step stepArquivoDelimitado(
            @Qualifier("readerArquivoDelimitation") ItemReader<Cliente> readerArquivoDelimitation,
            @Qualifier("writerArquivoDelimitation") ItemWriter<Cliente> writerArquivoDelimitation
    ) {
        PlatformTransactionManager txManager = new ResourcelessTransactionManager();

        return new StepBuilder("stepArquivoDelimitado", jobRepository)
                .<Cliente, Cliente>chunk(4, txManager)
                .reader(readerArquivoDelimitation)
                .writer(writerArquivoDelimitation)
                .build();
    }
}
