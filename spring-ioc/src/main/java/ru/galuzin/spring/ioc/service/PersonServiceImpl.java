package ru.galuzin.spring.ioc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.galuzin.spring.ioc.dao.PersonDao;
import ru.galuzin.spring.ioc.domain.Person;
@Service
public class PersonServiceImpl implements PersonService {

    private final PersonDao dao;

    @Autowired
    private LogService logService;

    public PersonServiceImpl(/*@Qualifier(value = "personRandom"*/ PersonDao dao) {
        this.dao = dao;
    }

    public Person getByName(String name) {
        logService.log(name);
        return dao.findByName(name);
    }

    @Override
    public String toString() {
        return "PersonServiceImpl{" +
                "dao=" + dao +
                '}';
    }
}
