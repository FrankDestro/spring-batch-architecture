package com.sysout.sb_reader_arquivo_delimitado.job.reader;

import com.sysout.sb_reader_arquivo_delimitado.job.domain.Cliente;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class ReaderConfig {

    @Bean
    @StepScope
    public FlatFileItemReader<Cliente> readerArquivoDelimitation() {
        return new FlatFileItemReaderBuilder<Cliente>()
                .name("readerArquivoDelimitation")
                .resource(new FileSystemResource("files/clientes.txt"))
                .delimited()
                .names(new String [] {"nome", "sobrenome", "idade", "email"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Cliente.class);
                }})
                .build();
    }
}
