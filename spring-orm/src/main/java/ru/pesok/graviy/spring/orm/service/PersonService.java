package ru.pesok.graviy.spring.orm.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pesok.graviy.spring.orm.domain.Person;
import ru.pesok.graviy.spring.orm.repository.PersonRepositoryData;
import ru.pesok.graviy.spring.orm.repository.PersonRepositoryIF;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PersonService {

    private final PersonRepositoryIF repository;

    private final PersonRepositoryData repositoryData;

    @Autowired
    private EntityManager entityManager;

    public PersonService(PersonRepositoryIF repository, PersonRepositoryData repositoryData) {
        this.repository = repository;
        this.repositoryData = repositoryData;
    }

    @Transactional
    public void save(Person p) {
        repository.save(p);
    }

    @Transactional(readOnly = true)
    public List<Person> getAll() {
        return repository.getAll();
    }

    @Transactional
    public void deleteFromZone(String zone){
        repository.deleteFromZone(zone);
    }

    @Transactional
    public void streamAll(){
        try(Stream<Person> personStream = repositoryData.findAll()) {
            personStream.forEach(p -> {
                System.out.println("p = " + p);
                p.setName(p.getName() + 1);
                repositoryData.saveAndFlush(p);
                entityManager.detach(p);
            });
        }
    }

    public Person findById(Integer id) {
        return repositoryData.findById(id);
    }

}
