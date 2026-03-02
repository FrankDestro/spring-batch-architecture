package com.sysout.sbdistributedtransactions.jobB.reader;

import com.sysout.sbdistributedtransactions.jobB.domain.Pessoa;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class PessoaItemReaderConfig {

    @Bean
    @StepScope  // importante para parametrização dinâmica
    public FlatFileItemReader<Pessoa> readerPessoa() {
        return new FlatFileItemReaderBuilder<Pessoa>()
                .name("pessoaReader")
                .resource(new FileSystemResource("files/pessoas.csv"))
                .linesToSkip(1) // pula o cabeçalho CSV
                .delimited()
                .names("nome", "email", "dataNascimento", "idade", "id")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Pessoa.class);
                }})
                .build();
    }
}
