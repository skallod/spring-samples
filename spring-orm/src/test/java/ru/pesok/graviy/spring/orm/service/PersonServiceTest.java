package ru.pesok.graviy.spring.orm.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.pesok.graviy.spring.orm.repository.PersonRepository;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {PersonServiceTest.TestConfig.class, PersonService.class})
//same result @SpringBootTest(classes = {PersonServiceTest.TestConfig.class, PersonService.class})
//@ComponentScan("ru.pesok.graviy.spring.orm.service")
public class PersonServiceTest {

    @MockBean
    PersonRepository personRepository;

    @TestConfiguration
    static class TestConfig {

        @Bean
        public String leogString() {
            return "leog";
        }

    }

    @Autowired
    PersonService personService;

    @Autowired
    @Qualifier("leogString")
    String leogString;

    @Test
    public void test1() {
        System.out.println("personRepository = " + personService);
    }
}
