package com.sysout.spring_batch.Config.JobRefatorado.Step;

import com.sysout.spring_batch.Config.JobRefatorado.tasklet.ImprimeOlaTasklet;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class StepConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public StepConfig(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Step imprimeOlaStep(ImprimeOlaTasklet imprimeOlaTasklet) {
        return new StepBuilder("imprimeOlaStep", jobRepository)
                .tasklet(imprimeOlaTasklet, transactionManager)
                .build();
    }
}
