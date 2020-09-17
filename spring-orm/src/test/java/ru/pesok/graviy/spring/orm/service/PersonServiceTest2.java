package ru.pesok.graviy.spring.orm.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PersonServiceTestConfig.class)
public class PersonServiceTest2 {

    @Autowired
    PersonService personService;

    @Test
    public void test1() {
        personService.getAll();
        System.out.println("personService = " + personService);
    }
}
