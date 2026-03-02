package com.sysout.sb_reader_arquivo_xml.Job.writer;

import com.sysout.sb_reader_arquivo_xml.domain.Employee;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WriterXml {

    @Bean
    @StepScope
    public ItemWriter<Employee> writerArquivoXml() {
        return items -> items.forEach(System.out::println);
    }
}
