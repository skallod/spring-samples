package ru.pesok.graviy.spring.orm.service;

import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.pesok.graviy.spring.orm.domain.Person;

public class PersonServiceNested {

    private static final Logger log = LoggerFactory.getLogger(PersonServiceNested.class);

    private final LocalSessionFactoryBean localSessionFactoryBean;

    public PersonServiceNested(LocalSessionFactoryBean localSessionFactoryBean) {
        this.localSessionFactoryBean = localSessionFactoryBean;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updatePersonRN(long l, int nestedId,
                               //если совпадает с внешним, то deadlock
                               String newNested) {
        _updatePerson(l, nestedId, newNested);
    }

    @Transactional
    public void updatePerson(long l, int nestedId,
                             //если совпадает с внешним, то deadlock
                             String newNested) {
        _updatePerson(l, nestedId, newNested);
    }

    private void _updatePerson(long l, int nestedId, String newNested) {
        long time = System.currentTimeMillis();
        Session session = localSessionFactoryBean.getObject().getCurrentSession();
        log.info("gal session nested = " + session.hashCode());
        Person loadNested = session.load(Person.class, nestedId, LockOptions.UPGRADE);
        log.info("gal nested after load");
        try {
            Thread.sleep(l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        loadNested.setName(loadNested.getName() + newNested);
        session.saveOrUpdate(loadNested);
        log.info("gal save with lock timing " + loadNested.getName() +
                " ; " + (System.currentTimeMillis() - time));
    }
}
