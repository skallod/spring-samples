package ru.galuzin.spring.ioc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import ru.galuzin.spring.ioc.dao.PersonDao;
import ru.galuzin.spring.ioc.dao.PersonDaoRandom;
import ru.galuzin.spring.ioc.dao.PersonDaoSimple;

@Configuration
public class DaoConfig {
    @Bean
    //@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public PersonDao personDao() {
        System.out.println("create bean");
        return new PersonDaoSimple();
    }
}
