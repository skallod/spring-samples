package ru.galuzin.springrest.controller;


import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.galuzin.springrest.conf.AppConf;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyControllerTest.TestConfig.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class MyControllerTest implements ApplicationContextAware {

    @Autowired
    MockMvc mvc;

    @Test
    public void test() throws Exception {
        mvc.perform(post("/api/v1/testpost")).andExpect(status().isAccepted());
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
