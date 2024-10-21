package ru.galuzin.springrest.conf;

import io.micrometer.core.instrument.MeterRegistry;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import ru.galuzin.springrest.health.CustomDbHealthIndicator;

import javax.sql.DataSource;

@Configuration
public class AppConf {

    @Bean
    public DataSource mainDataSource() {
        final PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/main");
        dataSource.setUser("postgres");
        dataSource.setPassword("mysecretpassword");
        return dataSource;
    }

    @Bean
    public DataSource secondaryDataSource() {
        final var dataSource = new PGSimpleDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:35432/postgres");
        dataSource.setUser("postgres");
        dataSource.setPassword("mysecretpassword");
        return dataSource;
    }

    @Bean
    public HealthIndicator customDbHealthIndicator (
        DataSource mainDataSource,
        DataSource secondaryDataSource
    ) {
        return new CustomDbHealthIndicator(mainDataSource, secondaryDataSource);
    }

    @Bean(name = "appTaskExecutor")
    public ThreadPoolTaskExecutor appTaskExecutor(MeterRegistry meterRegistry) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setThreadNamePrefix("gal_app");
        executor.setThreadGroupName("gal_group");
        executor.setAllowCoreThreadTimeOut(true);
        executor.setKeepAliveSeconds(30);
        // похоже нельзя executor.initialize();//todo можно ли так делать

//        executor.setQueueCapacity(50);
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        executor.initialize();
        return executor;
    }



}
