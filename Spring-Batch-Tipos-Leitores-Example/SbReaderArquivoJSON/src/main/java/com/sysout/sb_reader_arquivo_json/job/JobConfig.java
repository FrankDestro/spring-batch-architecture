package com.sysout.sb_reader_arquivo_json.job;

import com.sysout.sb_reader_arquivo_json.job.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class JobConfig {

    private final JobRepository jobRepository;

    @Bean
    public Job jobArquivoJson (Step stepArquivoJson) {
        return new JobBuilder("jobArquivoJson", jobRepository)
                .start(stepArquivoJson)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step stepArquivoJson (
            @Qualifier("jsonItemReader") JsonItemReader<User> jsonItemReader,
            @Qualifier("jsonItemWriter") JsonFileItemWriter<User> jsonItemWriter
    ) {
        PlatformTransactionManager txManager = new ResourcelessTransactionManager();

        return new StepBuilder("step", jobRepository)
                .<User, User>chunk(1, txManager)
                .reader(jsonItemReader)
                .writer(jsonItemWriter)
                .build();
    }
}
