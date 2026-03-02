package com.sysout.sbdistributedtransactions.jobB.writer;

import com.sysout.sbdistributedtransactions.jobB.domain.Pessoa;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class PessoaItemWriterConfig {

    private static final String UPSERT_SQL = """
        INSERT INTO pessoa (id, nome, email, data_nascimento, idade)
        VALUES (:id, :nome, :email, :dataNascimento, :idade)
        ON DUPLICATE KEY UPDATE
            nome = VALUES(nome),
            email = VALUES(email),
            data_nascimento = VALUES(data_nascimento),
            idade = VALUES(idade)
        """;

    @Bean(name = "compositeWriter")
    public CompositeItemWriter<Pessoa> compositeWriter(
            @Qualifier("writerDb1") ItemWriter<Pessoa> writerDb1,
            @Qualifier("writerDb2") ItemWriter<Pessoa> writerDb2) {
        return new CompositeItemWriterBuilder<Pessoa>()
                .delegates(List.of(writerDb1, writerDb2))
                .build();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<Pessoa> writerDb1(@Qualifier("dataSourceOne") DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Pessoa>()
                .dataSource(dataSource)
                .sql(UPSERT_SQL)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .build();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<Pessoa> writerDb2(@Qualifier("dataSourceTwo") DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Pessoa>()
                .dataSource(dataSource)
                .sql(UPSERT_SQL)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .build();
    }

}
