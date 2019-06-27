package ru.galuzin.spring.ioc.dao;

import ru.galuzin.spring.ioc.domain.Person;

public interface PersonDao {

    Person findByName(String name);
}
