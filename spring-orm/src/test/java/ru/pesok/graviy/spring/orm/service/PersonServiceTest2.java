package ru.pesok.graviy.spring.orm.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.pesok.graviy.spring.orm.repository.PersonRepository;
import ru.pesok.graviy.spring.orm.repository.TaskRepository;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PersonServiceTestConfig.class)
public class PersonServiceTest2 {

    @MockBean
    TaskRepository taskRepository;

    @MockBean
    PersonRepository personRepository;

    @Autowired
    PersonService personService;

    @Autowired
    TaskService taskService;

    /**
     * Здесь autowired обычные объекты - без транзакционного слоя
     * а mock мок объекты
     */
    @Test
    public void test1() {
        personService.getAll();
        System.out.println("personService = " + personService);
    }
}
