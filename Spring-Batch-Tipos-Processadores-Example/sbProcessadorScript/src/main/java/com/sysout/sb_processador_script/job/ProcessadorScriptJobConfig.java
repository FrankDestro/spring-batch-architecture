package com.sysout.sb_processador_script.job;

import com.sysout.sb_processador_script.job.domain.Cliente;
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
public class ProcessadorScriptJobConfig {

    private final JobRepository jobRepository;

    @Bean
    public Job processadorValidacaoJob (Step processadorValidacaoStep) {
        return new JobBuilder("processadorValidacaoJob", jobRepository)
                .start(processadorValidacaoStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public Step processadorValidacaoStep (
            ItemReader<Cliente> processadorScriptReader,
            ItemProcessor<Cliente, Cliente> processadorScriptProcessor,
            ItemWriter<Cliente> processadorScriptWriter
    ) {
        PlatformTransactionManager transactionManager = new ResourcelessTransactionManager();

        return new StepBuilder("processadorValidacaoStep", jobRepository)
                .<Cliente, Cliente> chunk(1, transactionManager)
                .reader(processadorScriptReader)
                .processor(processadorScriptProcessor)
                .writer(processadorScriptWriter)
                .build();
    }
}
