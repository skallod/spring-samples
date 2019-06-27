package ru.galuzin.spring.ioc.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.galuzin.spring.ioc.dao.PersonDao;
import ru.galuzin.spring.ioc.domain.Person;
@Service
public class PersonServiceImpl implements PersonService {

    private final PersonDao dao;

    public PersonServiceImpl(@Qualifier(value = "personRandom") PersonDao dao) {
        this.dao = dao;
    }

    public Person getByName(String name) {
        return dao.findByName(name);
    }
}
