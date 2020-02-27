package ru.pesok.graviy.spring.orm.service;


import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.Transactional;
import ru.pesok.graviy.spring.orm.domain.Person;

import java.util.List;

public class PersonService {

    private static final Logger log = LoggerFactory.getLogger(PersonService.class);

//    private final PersonRepository repository;

//    @Autowired
    private final PersonServiceNested personServiceNested;


    private final LocalSessionFactoryBean localSessionFactoryBean;

    public PersonService(PersonServiceNested personServiceNested, LocalSessionFactoryBean localSessionFactoryBean) {
        this.personServiceNested = personServiceNested;
        this.localSessionFactoryBean = localSessionFactoryBean;
    }

    @Transactional
    public void save(Person p){
        Session session = localSessionFactoryBean.getObject().getCurrentSession();
        session.saveOrUpdate(p);
//        repository.save(p);
    }

    @Transactional
    public void saveWithLock(int id, long timout, String newName, int nestedId){
        long time = System.currentTimeMillis();
        Session session = localSessionFactoryBean.getObject().getCurrentSession();
        log.info("gal session = " + session.hashCode());
        Person load = session.load(Person.class, id, LockOptions.UPGRADE);
        try {
            Thread.sleep(timout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        load.setName(load.getName()+newName);
        session.saveOrUpdate(load);
        log.info( "gal save with lock timing " + (System.currentTimeMillis() - time));
        Person person = session.find(Person.class, nestedId);
        log.info("gal person from nested "+person);
//        repository.save(p);
    }

    @Transactional
    public void saveWithLockWithNested(int id, long timout, String newName, int nestedId){
        long time = System.currentTimeMillis();
        Session session = localSessionFactoryBean.getObject().getCurrentSession();
        log.info("gal session = " + session.hashCode());
        Person load = session.load(Person.class, id, LockOptions.UPGRADE);
        try {
            Thread.sleep(timout/2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        personServiceNested.updatePerson(timout/2, nestedId, newName);
        Person person = session.find(Person.class, nestedId);
        log.info("gal person from nested "+person);
        load.setName(load.getName()+newName);
        session.saveOrUpdate(load);
        log.info("gal save with lock timing " + (System.currentTimeMillis() - time));
//        repository.save(p);
    }

    @Transactional
    public List<Person> getAll(){
        Session session = localSessionFactoryBean.getObject().getCurrentSession();
        Criteria criteria = session.createCriteria(Person.class);
//        Person load = session.load(Person.class, 2);
        //session.detach(load);
        return (List<Person>)criteria.list();
        //return repository.getAll();
    }

    @Transactional
    public Person get(int id){
        Session session = localSessionFactoryBean.getObject().getCurrentSession();
        Person load = session.find(Person.class, id);
        //session.detach(load);
        return load;
        //return repository.getAll();
    }

//    @Transactional
//    public void deleteFromZone(String zone){
//        repository.deleteFromZone(zone);
//    }

}
