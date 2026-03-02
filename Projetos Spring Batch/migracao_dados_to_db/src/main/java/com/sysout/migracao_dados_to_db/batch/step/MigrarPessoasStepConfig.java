package com.sysout.migracao_dados_to_db.batch.step;

import com.sysout.migracao_dados_to_db.batch.domain.Pessoa;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class MigrarPessoasStepConfig {

    private final JobRepository jobRepository;

    @Bean
    public Step migrarPessoaStep(JobRepository jobRepository,
                                PlatformTransactionManager transactionManager,
                                ItemReader<Pessoa> pessoaReader,
                                ItemWriter<Pessoa> pessoaWriter
    ) {
        return new StepBuilder("migrarPessoaStep", jobRepository)
                .<Pessoa, Pessoa>chunk(100, transactionManager)
                .reader(pessoaReader)
                .writer(pessoaWriter)
                .transactionManager(transactionManager)
                .build();
    }
}
