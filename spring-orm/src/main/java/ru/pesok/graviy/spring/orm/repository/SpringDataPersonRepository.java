package ru.pesok.graviy.spring.orm.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.pesok.graviy.spring.orm.domain.Person;

@Repository
public interface SpringDataPersonRepository extends CrudRepository<Person, Long> {
}
