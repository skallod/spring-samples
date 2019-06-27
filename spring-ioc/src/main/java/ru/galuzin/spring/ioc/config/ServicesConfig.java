package ru.galuzin.spring.ioc.config;

import org.springframework.context.annotation.Bean;
import ru.galuzin.spring.ioc.dao.PersonDao;
import ru.galuzin.spring.ioc.service.PersonService;
import ru.galuzin.spring.ioc.service.PersonServiceImpl;
//@Configuration
public class ServicesConfig {
    @Bean
    public PersonService personService(PersonDao dao) {
        return new PersonServiceImpl(dao);
    }
}
