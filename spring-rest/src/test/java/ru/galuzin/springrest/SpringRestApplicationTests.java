package ru.galuzin.springrest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest//(classes = SpringRestApplicationTests.TestConf.class)
@ContextConfiguration(classes = SpringRestApplicationTests.TestConf.class)
@Slf4j
class SpringRestApplicationTests extends AbstractSpringRestApplicationTests {

    @Test
    void contextLoads() {
    }

    // без всего тоже автоконфигурации не грузятся
    // @Configuration // автоконфигурации не грузятся
    @TestConfiguration // грузятся автоконфигурации
    static class TestConf {
        @Bean
        CommandLineRunner runner() {
            log.info("Create bean test");
            return args -> {
                log.info("Running test");
            };
        }
    }
}
