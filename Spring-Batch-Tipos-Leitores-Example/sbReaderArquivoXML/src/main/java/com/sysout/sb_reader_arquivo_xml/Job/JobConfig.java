package com.sysout.sb_reader_arquivo_xml.Job;

import com.sysout.sb_reader_arquivo_xml.domain.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
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
    public Job jobArquivoXml (Step stepArquivoXml) {
        return new JobBuilder("jobArquivoXml", jobRepository)
                .start(stepArquivoXml)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step stepArquivoXml (
            @Qualifier("itemReaderArquivoXml") StaxEventItemReader<Employee> itemReaderArquivoXml,
            @Qualifier("writerArquivoXml") ItemWriter<Employee> writerArquivoXml
    ) {
        PlatformTransactionManager txManager = new ResourcelessTransactionManager();

        return new StepBuilder("step", jobRepository)
                .<Employee, Employee>chunk(1, txManager)
                .reader(itemReaderArquivoXml)
                .writer(writerArquivoXml)
                .build();
    }
}
