package com.sysout.sb_processador_classifier.job;

import com.sysout.sb_processador_classifier.job.domain.Cliente;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ProcessadorClassifierJobConfig {

    private final JobRepository jobRepository;

    @Bean
    public Job processadorClassifierJob (Step processadorClassifierStep) {
        return new JobBuilder("processadorClassifierJob", jobRepository)
                .start(processadorClassifierStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public Step processadorClassifierStep (
            ItemReader<Cliente> processadorClassifierReader,
            ItemProcessor<Cliente, Cliente> processadorClassifierProcessor,
            ItemWriter<Cliente> processadorClassifierWriter
    ) {
        PlatformTransactionManager transactionManager = new ResourcelessTransactionManager();

        return new StepBuilder("processadorClassifierStep", jobRepository)
                .<Cliente, Cliente> chunk(1, transactionManager)
                .reader(processadorClassifierReader)
                .processor(processadorClassifierProcessor)
                .writer(processadorClassifierWriter)
                .build();
    }
}
