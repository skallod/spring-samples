package com.galuzin.autoconf;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@ConditionalOnProperty(value = "test-settings.services.autoconfiguration",
        matchIfMissing = true) // true - бины создаются без настройки , false - не создаются
@EnableConfigurationProperties(TestProperties1.class)
public class TestConfig1 {

    @Bean
    public A aClass(TestProperties1 tp1) {
        return new A();
    }
}
