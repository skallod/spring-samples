package ru.pesok.graviy.spring.orm.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pesok.graviy.spring.orm.domain.Person;
import ru.pesok.graviy.spring.orm.repository.PersonRepository;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepository repository;

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void save(Person p){
        repository.save(p);
    }

    @Transactional(readOnly = true)
    public List<Person> getAll(){
        return repository.getAll();
    }

    @Transactional
    public void deleteFromZone(String zone){
        repository.deleteFromZone(zone);
    }

}
