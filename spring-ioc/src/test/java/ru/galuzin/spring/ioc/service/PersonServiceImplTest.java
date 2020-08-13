package ru.galuzin.spring.ioc.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.galuzin.spring.ioc.dao.PersonDao;
import ru.galuzin.spring.ioc.domain.Person;

import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
//@SpringBootTest
public class PersonServiceImplTest {

    @TestConfiguration
    static class PersonServiceImplTestConfiguration {

        @Bean
        public PersonDao personDao() {
            return Mockito.mock(PersonDao.class);
        }

        @Bean
        public PersonServiceImpl personService() {
            return new PersonServiceImpl(personDao());
        }
    }

    @MockBean
    LogService logService;

    @Autowired
    PersonDao personDao;

    @Autowired
    PersonServiceImpl personService;

    @Test
    public void getByName() {
        System.out.println("test g");
        //Mockito.when(logService.log(Mockito.anyString())).;
        final Person mocked = new Person("mocked", 0);
        Mockito.when(personDao.findByName(Mockito.anyString())).thenReturn(mocked);
        final Person returned = personService.getByName("test");
        Assert.assertThat(returned.getName(), is("mocked"));
    }
}
