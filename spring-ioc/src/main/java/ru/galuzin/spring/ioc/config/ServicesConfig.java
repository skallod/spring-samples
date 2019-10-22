package ru.galuzin.spring.ioc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.galuzin.spring.ioc.dao.PersonDao;
import ru.galuzin.spring.ioc.service.OtherService;
import ru.galuzin.spring.ioc.service.PersonService;
import ru.galuzin.spring.ioc.service.PersonServiceImpl;
import ru.galuzin.spring.ioc.service.StartStopService;

@Configuration
@Import({DaoConfig.class})
public class ServicesConfig {

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
}
