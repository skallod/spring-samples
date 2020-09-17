package ru.pesok.graviy.spring.orm.service;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import ru.pesok.graviy.spring.orm.domain.Person;
import ru.pesok.graviy.spring.orm.repository.PersonRepository;
import ru.pesok.graviy.spring.orm.repository.PersonRepositoryIF;

import java.util.Collections;

@TestConfiguration
@ComponentScan(basePackages = "ru.pesok.graviy.spring.orm.service")
public class PersonServiceTestConfig {

//        @Bean
//        public PersonRepositoryIF personRepository() {
//            PersonRepositoryIF mock = Mockito.mock(PersonRepositoryIF.class);
//            Mockito.when(mock.getAll()).thenReturn(Collections.singletonList(new Person(){{
//                setId(1);setName("Rumata");
//            }}));
//            return mock;
//        }
//
//        @Bean
//        PersonService personService () {
//            return new PersonService(personRepository());
//        }
}
