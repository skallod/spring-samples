package ru.galuzin.spring.ioc.service;

import ru.galuzin.spring.ioc.domain.Person;

public interface PersonService {

    Person getByName(String name);
}
