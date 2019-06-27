package ru.galuzin.spring.ioc.dao;

import org.springframework.stereotype.Component;
import ru.galuzin.spring.ioc.domain.Person;
@Component(value = "personSimple")
public class PersonDaoSimple implements PersonDao {

    public Person findByName(String name) {
        return new Person(name, 18);
    }
}
