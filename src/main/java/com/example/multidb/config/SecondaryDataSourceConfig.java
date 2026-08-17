package com.example.multidb.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Secondary datasource: backs the OWNER database and everything under
 * com.example.multidb.owner.model / .repository
 */
@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.multidb.owner.repository",
        entityManagerFactoryRef = "ownerEntityManagerFactory",
        transactionManagerRef = "ownerTransactionManager"
)
public class SecondaryDataSourceConfig {

    @Bean(name = "ownerDataSourceProperties")
    @ConfigurationProperties(prefix = "app.datasource.secondary")
    public DataSourceProperties ownerDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "ownerDataSource")
    public DataSource ownerDataSource(
            @Qualifier("ownerDataSourceProperties") DataSourceProperties properties) {
        return DataSourceBuilder.create()
                .url(properties.getJdbcUrl())
                .username(properties.getUsername())
                .password(properties.getPassword())
                .driverClassName(properties.getDriverClassName())
                .build();
    }

    @Bean(name = "ownerEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean ownerEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("ownerDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.example.multidb.owner.model")
                .persistenceUnit("owner")
                .build();
    }

    @Bean(name = "ownerTransactionManager")
    public PlatformTransactionManager ownerTransactionManager(
            @Qualifier("ownerEntityManagerFactory") LocalContainerEntityManagerFactoryBean ownerEntityManagerFactory) {
        return new JpaTransactionManager(ownerEntityManagerFactory.getObject());
    }
}
