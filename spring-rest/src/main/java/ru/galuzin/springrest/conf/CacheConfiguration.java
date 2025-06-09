package ru.galuzin.springrest.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfiguration {

    @Bean("configurationCacheManager")
    public CacheManager tagsByEntityIdCacheManager(
            @Value("${test-settings.services.expire-after-write-sec:5}")
            int configurationCacheExpireAfterWriteSec) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager("tags", "services");
        caffeineCacheManager.setCaffeine(Caffeine.newBuilder().maximumSize(1000).expireAfterWrite(configurationCacheExpireAfterWriteSec, TimeUnit.SECONDS));
        return caffeineCacheManager;
    }
}
