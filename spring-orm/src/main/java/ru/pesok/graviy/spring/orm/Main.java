package ru.pesok.graviy.spring.orm;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.pesok.graviy.spring.orm.domain.Person;
import ru.pesok.graviy.spring.orm.service.PersonService;

import javax.annotation.PostConstruct;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@SpringBootApplication
public class Main {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Main.class);
    }

    @Autowired
    private PersonService service;

    @PostConstruct
    public void init() {
        System.out.println("init start");
        service.save(new Person(1,"Pushkin","room"));
        service.save(new Person(2,"Errt","kitchen"));
        service.save(new Person(3,"Ghh","room"));
        service.save(new Person(4,"Zxx","room"));
        service.save(new Person(5,"Xcc","kitchen"));
        //service.save(new Person(1,"Pushkin"));
        List<Person> all = service.getAll();
        System.out.println("before "+all);
        service.deleteFromZone("kitchen");
        all = service.getAll();
        System.out.println("after "+all);
    }
}
