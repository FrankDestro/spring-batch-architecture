package com.sysout.sb_jdbc_paging.Job.writer;

import com.sysout.sb_jdbc_paging.Job.domain.Cliente;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JdbcPagingWriterConfig {

    @Bean
    public ItemWriter<Cliente> jdbcPagingWriter() {
        return clientes -> clientes.forEach(System.out::println);
    }
}
