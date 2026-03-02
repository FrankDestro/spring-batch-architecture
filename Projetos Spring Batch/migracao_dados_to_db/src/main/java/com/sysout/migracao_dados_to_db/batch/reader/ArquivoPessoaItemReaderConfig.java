package com.sysout.migracao_dados_to_db.batch.reader;

import com.sysout.migracao_dados_to_db.batch.domain.Pessoa;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.util.Date;

@Configuration
public class ArquivoPessoaItemReaderConfig {

    @Bean
    @StepScope
    public FlatFileItemReader<Pessoa> pessoaReader(
            @Value("${sftp.dir.local.download}") String localDir
    ) {
        String filePath = localDir + "/pessoas.csv";

        return new FlatFileItemReaderBuilder<Pessoa>()
                .name("pessoaReader")
                .resource(new FileSystemResource(filePath))
                .linesToSkip(1)
                .delimited()
                .delimiter(DelimitedLineTokenizer.DELIMITER_COMMA)
                .names("nome", "email", "dataNascimento", "idade", "id")
                .fieldSetMapper(fieldSetMapper())
                .build();
    }

    private FieldSetMapper<Pessoa> fieldSetMapper() {
        return fieldSet -> {
            Pessoa pessoa = new Pessoa();
            pessoa.setNome(fieldSet.readString("nome"));
            pessoa.setEmail(fieldSet.readString("email"));
            pessoa.setDataNascimento(new Date(fieldSet.readDate("dataNascimento", "yyyy-MM-dd HH:mm:ss").getTime()));
            pessoa.setIdade(fieldSet.readInt("idade"));
            pessoa.setId(fieldSet.readInt("id"));
            return pessoa;
        };
    }
}