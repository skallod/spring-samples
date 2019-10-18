package ru.galuzin.spring.ioc;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ru.galuzin.spring.ioc.domain.Person;
import ru.galuzin.spring.ioc.service.PersonService;
@ComponentScan
public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(Main.class);
        //context.register(ServicesConfig.class);
        context.refresh();

        PersonService service = context.getBean(PersonService.class);

        Person person = service.getByName("petya");
        System.out.println(person);

        person = service.getByName("petya");
        System.out.println(person);

        System.out.println("service = " + service);
    }
}
