package com.sysout.sb_jdbc_paging.Job.reader;

import com.sysout.sb_jdbc_paging.Job.domain.Cliente;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

@Configuration
public class JdbcPagingReaderConfig {

    @Bean
    public JdbcPagingItemReader<Cliente> jdbcPagingItemReader(
            @Qualifier("appDataSource")DataSource dataSource,
            PagingQueryProvider queryProvider) {
         return new JdbcPagingItemReaderBuilder<Cliente>()
                 .name("jdbcPagingItemReader")
                 .dataSource(dataSource)
                 .queryProvider(queryProvider)
                 .pageSize(5)
                 .rowMapper(new BeanPropertyRowMapper<>(Cliente.class))
                 .build();
    }

    @Bean
    public SqlPagingQueryProviderFactoryBean queryProviderFactoryBean (
            @Qualifier("appDataSource") DataSource dataSource)
    {
        SqlPagingQueryProviderFactoryBean queryProviderFactoryBean = new SqlPagingQueryProviderFactoryBean();
        queryProviderFactoryBean.setDataSource(dataSource);
        queryProviderFactoryBean.setSelectClause("select *");
        queryProviderFactoryBean.setFromClause("cliente");
        queryProviderFactoryBean.setSortKey("email");
        return queryProviderFactoryBean;
    }
}
