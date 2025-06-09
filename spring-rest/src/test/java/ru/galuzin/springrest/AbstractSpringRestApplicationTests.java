package ru.galuzin.springrest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest//(classes = AbstractSpringRestApplicationTests.TestConf.class)
@ContextConfiguration(classes = AbstractSpringRestApplicationTests.TestConf.class) // складывается при наследовании
@Slf4j
class AbstractSpringRestApplicationTests {

    // без всего тоже автоконфигурации не грузятся
    // @Configuration // автоконфигурации не грузятся
    @TestConfiguration // грузятся автоконфигурации
    static class TestConf {
        @Bean
        CommandLineRunner runnerAbstract() {
            log.info("Create bean abstract test");
            return args -> {
               log.info("Running abstract test");
           };
        }
    }
}
