package ru.galuzin.spring.ioc.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.galuzin.spring.ioc.domain.Person;

import java.util.Random;

@Component(value = "personRandom")
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PersonDaoRandom implements PersonDao {

    //private final Random random = new Random();
    final Person person;

    public PersonDaoRandom(){
        person = new Person("vasjya", new Random().nextInt(100));
    }

    public Person findByName(String name) {
        return person;//new Person("vasya",random.nextInt(11));
    }
}
