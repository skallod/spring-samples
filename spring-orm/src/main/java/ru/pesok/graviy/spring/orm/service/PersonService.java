package ru.pesok.graviy.spring.orm.service;


import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pesok.graviy.spring.orm.domain.Person;
import ru.pesok.graviy.spring.orm.repository.PersonRepository;
import ru.pesok.graviy.spring.orm.repository.SpringDataPersonRepository;
import ru.pesok.graviy.spring.orm.repository.PersonRepositoryIF;

//import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class PersonService {

    private final PersonRepositoryIF repository;

    private final SpringDataPersonRepository repositoryData;

    @Autowired
    private EntityManager entityManager;

    public PersonService(PersonRepositoryIF repository, SpringDataPersonRepository repositoryData) {
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
//        try(Stream<Person> personStream = repositoryData.findAll()) {
        StreamSupport.stream(repositoryData.findAll().spliterator(), false).forEach(p -> {
                System.out.println("p = " + p);
                p.setName(p.getName() + 1);
                repositoryData.save(p);
                entityManager.detach(p);
            });
//        }
    }

    @Transactional
    public Person findById(Integer id) {
        return repositoryData.findById(id.longValue()).orElse(null);
    }

}
