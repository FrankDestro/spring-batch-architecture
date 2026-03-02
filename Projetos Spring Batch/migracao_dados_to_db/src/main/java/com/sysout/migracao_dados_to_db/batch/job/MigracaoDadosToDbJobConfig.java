package com.sysout.migracao_dados_to_db.batch.job;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class MigracaoDadosToDbJobConfig {

    private final JobRepository jobRepository;

    @Bean
    public Job job(
            @Qualifier("migrarPessoaStep") Step migrarPessoaStep,
            @Qualifier("downloadSftpStep") Step downloadSftpStep
    ) {
        return new JobBuilder("job", jobRepository)
                .start(downloadSftpStep)
                .next(migrarPessoaStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }
}
