package ru.pesok.graviy.spring.orm.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import ru.pesok.graviy.spring.orm.domain.Person;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public class PersonRepository  {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PlatformTransactionManager tm;

    @Transactional
    public void save(Person p) {
        em.persist(p);
    }

    @Transactional(readOnly = true)
    public List<Person> getAll() {
        //jpql
        //return em.createQuery("select s from Person s", Person.class).getResultList();
        //native
        return em.createNativeQuery("select * from person s", Person.class).getResultList();
    }

    @Transactional
    public void deleteFromZone(String zone){
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaDelete<Person> criteriaDelete = builder.createCriteriaDelete(Person.class);
        Root<Person> root = criteriaDelete.from(Person.class);
        criteriaDelete.where(builder.equal(root.get("zone"),zone));
        em.createQuery(criteriaDelete).executeUpdate();
    }

}
