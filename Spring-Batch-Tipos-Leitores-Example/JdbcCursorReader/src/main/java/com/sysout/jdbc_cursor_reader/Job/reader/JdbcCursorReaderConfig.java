package com.sysout.jdbc_cursor_reader.Job.reader;

import com.sysout.jdbc_cursor_reader.Job.domain.Cliente;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class JdbcCursorReaderConfig {

    @Bean
    public JdbcCursorItemReader<Cliente> jdbcCursorItemReader(
            @Qualifier("appDataSource") DataSource dataSource) {

        return new JdbcCursorItemReaderBuilder<Cliente>()
                .name("jdbcCursorItemReader")
                .dataSource(dataSource)
                .sql("Select * from cliente")
                 .rowMapper(new BeanPropertyRowMapper<>(Cliente.class))
//                .rowMapper(rowMapper())
                .build();
    }


    private RowMapper<Cliente> rowMapper() {
        return new RowMapper<Cliente>() {

            public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
                if (rs.getRow() < 10 )
                    throw new SQLException(String.format("Encerrando a execução- Cliente inválido %s", rs.getString("email")));
                else return clienteRowMapper(rs);
            }

            public Cliente clienteRowMapper(ResultSet rs) throws SQLException {
                Cliente cliente = new Cliente();
                cliente.setNome(rs.getString("nome"));
                cliente.setSobrenome(rs.getString("sobrenome"));
                cliente.setIdade(rs.getString("idade"));
                cliente.setEmail(rs.getString("email"));
                return cliente;
            }
        };

    }
}


