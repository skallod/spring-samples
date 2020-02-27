package ru.pesok.graviy.spring.orm;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.pesok.graviy.spring.orm.config.HiberConf;
import ru.pesok.graviy.spring.orm.domain.Person;
//import ru.pesok.graviy.spring.orm.domain.TaskType;
import ru.pesok.graviy.spring.orm.service.PersonService;
//import ru.pesok.graviy.spring.orm.service.TaskService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.*;

//@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
//@SpringBootApplication
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(HiberConf.class);
        PersonService service = context.getBean(PersonService.class);
        service.save(new Person(1,"Pushkin","alks"));
        service.save(new Person(2,"Esenin","siniy"));
        //service.save(new Person(3,"Tolstoy","mus"));
        List<Person> all = service.getAll();
        log.info("gal all = " + all);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        {
            Person person = service.get(1);
            log.info("person = " + person);
            Future<?> submit1 = executor.submit(() -> service.saveWithLockWithNested(1, 30_000, "_2", 2));
            Future<?> submit2 = executor.submit(() -> {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                service.saveWithLock(1, 50, "_3",2);
            });
            try {
                submit1.get();
                submit2.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        Person person = service.get(1);
        log.info("person finish = " + person);
        executor.shutdown();
//        SpringApplication.run(Main.class);
    }

    @Autowired
    private PersonService service;

//    @Autowired
//    private TaskService taskService;

    @PostConstruct
    public void init() {
        System.out.println("init start");
//        taskService.save(new TaskType(7,"DATA_TEST"));
//        service.save(new Person(2,"Tolstoy","tst"));
        List<Person> all = service.getAll();
        System.out.println("all = " + all);
//        service.save(new Person(2,"Errt","kitchen"));
//        //service.save(new Person(1,"Pushkin"));
//        List<Person> all = service.getAll();
//        System.out.println("before "+all);
//        service.deleteFromZone("kitchen");
//        all = service.getAll();
//        System.out.println("after "+all);
    }
}
