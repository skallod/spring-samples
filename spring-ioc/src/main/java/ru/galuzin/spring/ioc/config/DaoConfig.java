package ru.galuzin.spring.ioc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import ru.galuzin.spring.ioc.dao.PersonDao;
import ru.galuzin.spring.ioc.dao.PersonDaoRandom;

//@Configuration
//public class DaoConfig {
//    @Bean
//    @Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
//    public PersonDao personDao() {
//        return new PersonDaoRandom();
//    }
//}
