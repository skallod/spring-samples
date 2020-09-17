package ru.pesok.graviy.spring.orm.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.pesok.graviy.spring.orm.domain.Person;
import ru.pesok.graviy.spring.orm.repository.PersonRepository;
import ru.pesok.graviy.spring.orm.repository.PersonRepositoryIF;
import ru.pesok.graviy.spring.orm.repository.TaskRepository;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PersonServiceTestConfig.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class PersonServiceTest3 {

    @MockBean
    TaskRepository taskRepository;

    @MockBean
    PersonRepository personRepository;

    @Autowired
    PersonService personService;

    @Autowired
    TaskService taskService;

    /**
     * autowired - cglib объекты - поддерживает транзакционный слой
     * mock - mock
     */
    @Test
    public void test1() {
        List<Person> all = personService.getAll();
        System.out.println("get all = " + all);
    }
}
