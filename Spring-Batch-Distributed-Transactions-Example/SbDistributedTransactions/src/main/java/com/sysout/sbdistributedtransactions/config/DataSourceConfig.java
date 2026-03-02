package com.sysout.sbdistributedtransactions.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.UserTransaction;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DataSourceConfig {

    // ========= JOB REPOSITORY (SPRING BATCH) - NÃO XA =========
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public HikariDataSource batchDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    // ========= DATASOURCE XA DB 1 =========
    @Bean
    @ConfigurationProperties(prefix = "app1.datasource.xa-properties")
    public Properties xaPropertiesOne() {
        return new Properties();
    }

    @Bean
    public DataSource dataSourceOne() {
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setXaDataSourceClassName("com.mysql.cj.jdbc.MysqlXADataSource");
        ds.setUniqueResourceName("appDS1");
        ds.setXaProperties(xaPropertiesOne());
        ds.setPoolSize(10);
        return ds;
    }

    // ========= DATASOURCE XA DB 2 =========
    @Bean
    @ConfigurationProperties(prefix = "app2.datasource.xa-properties")
    public Properties xaPropertiesTwo() {
        return new Properties();
    }

    @Bean
    public DataSource dataSourceTwo() {
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setXaDataSourceClassName("com.mysql.cj.jdbc.MysqlXADataSource");
        ds.setUniqueResourceName("dsTwo");
        ds.setXaProperties(xaPropertiesTwo());
        ds.setPoolSize(10);
        return ds;
    }

    // ========= BEANS DO ATOMIKOS =========
    @Bean
    public UserTransaction userTransaction() throws Throwable {
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        userTransactionImp.setTransactionTimeout(300);
        return userTransactionImp;
    }

    @Bean
    public TransactionManager atomikosTransactionManager() {
        UserTransactionManager manager = new UserTransactionManager();
        manager.setForceShutdown(false);
        return manager;
    }

    // ========= TRANSACTION MANAGER DO SPRING =========
    @Bean
    public PlatformTransactionManager transactionManager() throws Throwable {
        JtaTransactionManager jta = new JtaTransactionManager(userTransaction(), atomikosTransactionManager());
        jta.setAllowCustomIsolationLevels(true);
        return jta;
    }
}