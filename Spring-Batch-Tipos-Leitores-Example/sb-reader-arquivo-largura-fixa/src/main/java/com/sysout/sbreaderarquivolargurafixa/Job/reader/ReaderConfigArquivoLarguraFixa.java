package com.sysout.sbreaderarquivolargurafixa.Job.reader;

import com.sysout.sbreaderarquivolargurafixa.Job.Domain.Client;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class ReaderConfigArquivoLarguraFixa {

    @Bean
    @StepScope
    public FlatFileItemReader<Client> readerArquivosLarguraFixa() {
        return new FlatFileItemReaderBuilder<Client>()
                .name("clientReader")
                .resource(new FileSystemResource("files/clientes.txt"))
                .fixedLength()
                .columns(new Range[] {new Range(1,10), new Range(11,20), new Range(21,23), new Range(24,43)})
                .names(new String [] {"nome", "sobrenome", "idade", "email"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Client.class);
                }})
                .build();
    }
}
