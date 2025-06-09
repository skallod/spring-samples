package ru.galuzin.springrest.controller;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.galuzin.springrest.conf.AppConf;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.ZoneOffset.UTC;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = MyControllerTest.TestConfig.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties")
@Slf4j
public class MyControllerTest implements ApplicationContextAware {

    @Autowired
    MockMvc mvc;

    @Test
    public void test() throws Exception {
        mvc.perform(post("/api/v1/testpost")).andExpect(status().isAccepted());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(UTC);
//        OffsetDateTime.ofInstant(Instant.now(), UTC);
        Instant now = Instant.now();
        System.out.println("now = " + now);
        String formatted = formatter.format(now);
        Instant nowParsed = Instant.from(formatter.parse(formatted));
        System.out.println("nowParsed = " + nowParsed);
        log.info(formatted);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }


    @Configuration
    @EnableAutoConfiguration
    @ComponentScan(
        basePackages = {"ru.galuzin.springrest.controller"}
//    ,
//            excludeFilters = {
//                    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = WebApplication.class)
//            }
    )
    @Import(AppConf.class)
    static class TestConfig {
    }
}
