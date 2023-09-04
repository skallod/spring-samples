package net.ttddyy.dsproxy.example.config;

import net.ttddyy.dsproxy.example.oracle.OracleQueryListener;
import net.ttddyy.dsproxy.example.listener.CustomQueryListener;
import net.ttddyy.dsproxy.example.service.QueryListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    @ConditionalOnProperty(value = "spring.datasource.driverClassName", havingValue = "oracle.jdbc.driver.OracleDriver")
    public QueryListener oracleQueryListener() {
        return new OracleQueryListener();
    }
    @Bean
    @ConditionalOnProperty(value = "spring.datasource.driverClassName", havingValue = "org.postgresql.Driver")
    public QueryListener postgresQueryListener() {
        return new CustomQueryListener();
    }
}
