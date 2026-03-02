package com.sysout.sbdistributedtransactions.jobB;

import com.sysout.sbdistributedtransactions.jobB.domain.Pessoa;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class JobBConfig {

    private final JobRepository jobRepository;

    @Bean
    public Job job(Step step) {
        return new JobBuilder("job", jobRepository)
                .start(step)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step step(
            @Qualifier("readerPessoa") ItemReader<Pessoa> reader,
            @Qualifier("compositeWriter") ItemWriter<Pessoa> writer,
            PlatformTransactionManager transactionManager  // Sem Qualifier
    ) {
        return new StepBuilder("step", jobRepository)
                .<Pessoa, Pessoa>chunk(200, transactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }
}
