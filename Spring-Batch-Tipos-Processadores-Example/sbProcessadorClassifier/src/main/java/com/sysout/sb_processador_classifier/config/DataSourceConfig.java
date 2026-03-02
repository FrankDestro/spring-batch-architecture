package com.sysout.sb_processador_classifier.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    // DATA SOURCE SPRING BATCH JOB_REPOSITORY
    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource springDS() {
        return DataSourceBuilder.create().build();
    }

    // DATA SOURCE 1
    @Bean
    @ConfigurationProperties(prefix = "app1.datasource.business")
    public DataSource appDS1() {
        return DataSourceBuilder.create().build();
    }

    // TRANSACTION MANAGER DATA SOURCE 1
    @Bean
    public PlatformTransactionManager transactionManagerApp(@Qualifier("appDS1") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
