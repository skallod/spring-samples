package ru.galuzin.spring.ioc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import ru.galuzin.spring.ioc.dao.PersonDao;
import ru.galuzin.spring.ioc.dao.PersonDaoRandom;
import ru.galuzin.spring.ioc.dao.PersonDaoSimple;
import ru.galuzin.spring.ioc.domain.EventHandler;
import ru.galuzin.spring.ioc.domain.EventServer;
import ru.galuzin.spring.ioc.service.*;

import java.util.Collections;

@Configuration
//@EnableAspectJAutoProxy
@Import({DaoConfig.class})
@PropertySource(value="application.properties")
public class ServicesConfig {

    @Value("${db.timeout:1000}")
    private Integer dbTimeout=2000;

    @Bean
    public PersonService personService(PersonDao dao) {
        return new PersonServiceImpl(dao);
    }

    @Bean
    public OtherService otherService(PersonDao dao) {
        return new OtherService();//dao);
    }

    @Bean
    public StartStopService startStopService(){
        return new StartStopService();
    }

    /**
     * override bean
     * @return
     */
    @Bean
    public PersonDao personDao(@Value("${test.property:test5}")String unknownStr) {
        System.out.println("create random dao " + unknownStr);
        return new PersonDaoRandom("not injected","not injected");
    }

    @Bean
    public EventHandler<String> stringEventHandler(){
        return () -> System.out.println("string event handler");
    }

    @Bean
    public EventHandler<Integer> integerEventHandler(){
        return () -> System.out.println("integer event handler");
    }

    @Bean
    public EventServer eventServer( EventHandler<String> eventHandler ){
        return new EventServer(Collections.singletonList(eventHandler));
    }

    @Bean
    public LogService logService(){
        return new LogService();
    }
}
