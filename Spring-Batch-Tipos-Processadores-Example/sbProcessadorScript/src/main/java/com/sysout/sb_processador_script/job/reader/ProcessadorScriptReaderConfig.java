package com.sysout.sb_processador_script.job.reader;

import com.sysout.sb_processador_script.job.domain.Cliente;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class ProcessadorScriptReaderConfig {

    @StepScope
    @Bean
    public FlatFileItemReader<Cliente> processadorScriptReader(
            @Value("#{jobParameters['arquivoClientes']}") Resource arquivoClientes) {
        return new FlatFileItemReaderBuilder<Cliente>()
                .name("processadorValidacaoReader")
                .resource(arquivoClientes)
                .delimited()
                .names("nome", "idade", "email")
                .targetType(Cliente.class)
                .build();
    }
}
