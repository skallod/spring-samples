package ru.pesok.graviy.spring.orm.repository;

import ru.pesok.graviy.spring.orm.domain.Person;

import java.util.List;

public interface PersonRepositoryIF {

    public List<Person> getAll();

    public void deleteFromZone(String zone);

    public void save(Person p);
}
