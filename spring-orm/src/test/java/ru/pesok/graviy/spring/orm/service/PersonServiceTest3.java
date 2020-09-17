package ru.pesok.graviy.spring.orm.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.pesok.graviy.spring.orm.domain.Person;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PersonServiceTestConfig.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class PersonServiceTest3 {

    @Autowired
    PersonService personService;

    @Test
    public void test1() {
        List<Person> all = personService.getAll();
        System.out.println("get all = " + all);
    }
}
