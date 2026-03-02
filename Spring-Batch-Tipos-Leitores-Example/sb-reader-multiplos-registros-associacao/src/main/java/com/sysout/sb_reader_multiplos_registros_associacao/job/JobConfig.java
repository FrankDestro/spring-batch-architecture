package com.sysout.sb_reader_multiplos_registros_associacao.job;

import com.sysout.sb_reader_multiplos_registros_associacao.job.reader.ClienteTransacaoAssociationItemReader;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
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
    public Job jobArquivoMultiplosFormatos(Step stepArquivoMultiplosFormatos) {
        return new JobBuilder("jobArquivoMultiplosFormatos", jobRepository)
                .start(stepArquivoMultiplosFormatos)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step stepArquivoMultiplosFormatos(
            @Qualifier("arquivoMultiplosFormatosItemReader") FlatFileItemReader itemReaderArquivoMultiplosFormatos,
            @Qualifier("writerArquivoMultiplosFormatos") ItemWriter writerArquivoMultiplosFormatos
    ) {
        PlatformTransactionManager txManager = new ResourcelessTransactionManager();
        return new StepBuilder("step", jobRepository)
                .chunk(1, txManager)
                .reader(new ClienteTransacaoAssociationItemReader(itemReaderArquivoMultiplosFormatos))
                .writer(writerArquivoMultiplosFormatos)
                .build();
    }
}
