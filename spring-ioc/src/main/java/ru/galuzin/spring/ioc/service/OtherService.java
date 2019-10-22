package ru.galuzin.spring.ioc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.galuzin.spring.ioc.dao.PersonDao;

//@Service
public class OtherService {

    @Autowired
    private PersonDao personDao;

//    public OtherService(PersonDao personDao){
//        this.personDao = personDao;
//    }

    @Override
    public String toString() {
        return "OtherService{" +
                "personDao=" + personDao +
                '}';
    }
}
