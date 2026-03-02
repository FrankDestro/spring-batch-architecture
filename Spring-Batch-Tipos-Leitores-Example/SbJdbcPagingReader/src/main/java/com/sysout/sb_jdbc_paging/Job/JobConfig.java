package com.sysout.sb_jdbc_paging.Job;

import com.sysout.sb_jdbc_paging.Job.domain.Cliente;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

    private final JobRepository jobRepository;

    @Bean
    public Job jobJdbcReader(Step step) {
        return new JobBuilder("jobJdbcReader", jobRepository)
                .start(step)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step step(
            @Qualifier("jdbcPagingItemReader") JdbcPagingItemReader<Cliente> jdbcPagingItemReader,
            @Qualifier("jdbcPagingWriter") ItemWriter<Cliente> jdbcPagingWriter,
            @Qualifier("transactionManagerAppJdbc")
            PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder("step", jobRepository)
                .<Cliente, Cliente>chunk(2, transactionManager)
                .reader(jdbcPagingItemReader)
                .writer(jdbcPagingWriter)
                .build();
    }
}
