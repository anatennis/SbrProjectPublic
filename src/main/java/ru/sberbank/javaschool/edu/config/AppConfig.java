package ru.sberbank.javaschool.edu.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories("ru.sberbank.javaschool")
@EntityScan("ru.sberbank.javaschool")
public class AppConfig {

    @Autowired
    private Environment env;

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:/liquibase/master.db.changelog.xml");
        liquibase.setDataSource(dataSource);
        return liquibase;
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(env.getProperty("spring.datasource.url"));
        hikariConfig.setUsername(env.getProperty("spring.datasource.username"));
        hikariConfig.setPassword(env.getProperty("spring.datasource.password"));
        hikariConfig.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
        return new HikariDataSource(hikariConfig);
    }
}
