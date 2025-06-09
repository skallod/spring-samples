package ru.galuzin.springrest.conf;

import com.galuzin.autoconf.TestProperties1;
import com.galuzin.autoconf.TestProperties3;
import io.micrometer.core.instrument.MeterRegistry;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import ru.galuzin.springrest.health.CustomDbHealthIndicator;
import ru.galuzin.springrest.service.MyCachedService;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties({TestProperties1.class, TestProperties3.class})
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

    @Bean(name = "customDbHealthIndicator")
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

    @Primary
    @Bean
    @ConditionalOnProperty(name = "test-settings.services.listener.cache-enabled", havingValue = "true")
    public MyCachedService myCachedService(
            @Qualifier("configurationCacheManager") CacheManager cacheManager
    ) {
        return new MyCachedService(cacheManager.getCache("tags"));
    }

}
