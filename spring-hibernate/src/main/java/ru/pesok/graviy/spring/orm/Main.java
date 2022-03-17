package ru.pesok.graviy.spring.orm;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.EventListener;
import ru.pesok.graviy.spring.orm.config.HiberConf;
import ru.pesok.graviy.spring.orm.domain.Person;
//import ru.pesok.graviy.spring.orm.domain.TaskType;
import ru.pesok.graviy.spring.orm.service.PersonService;
//import ru.pesok.graviy.spring.orm.service.TaskService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.*;

//@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@SpringBootApplication
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

//    @Autowired
//    private PersonService service;

    public static void main(String[] args) throws Exception {
        final ConfigurableApplicationContext context = SpringApplication.run(Main.class);
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(HiberConf.class);
        new Main().entryPoint(context.getBean(PersonService.class));
    }


    @PostConstruct
    public void init() {
        System.out.println("init start");
//        taskService.save(new TaskType(7,"DATA_TEST"));
//        service.save(new Person(2,"Tolstoy","tst"));
//        List<Person> all = service.getAll();
    }

//    @EventListener(ApplicationReadyEvent.class)
    public void entryPoint(PersonService service) throws Exception {
        List<Person> all = service.getAll();
        log.info("gal all = " + all);
        service.save(new Person(1, "Pushkin", "alks"));
        service.save(new Person(2, "Esenin", "siniy"));
        all = service.getAll();
        log.info("gal all = " + all);
        testRequiresNewNotCleanParentTxLock(service);
        //testReentrentLockAndSessionCache(service);
    }

    public void testRequiresNewNotCleanParentTxLock(PersonService service) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        {
            Person person = service.get(1);
            log.info("person = " + person);
            Future<?> submit1 = executor.submit(() -> service
                    .saveWithLockWithNested(1, 10_000, "_2", 2, true));
            Future<?> submit2 = executor.submit(() -> {
                try {
                    Thread.sleep(6_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                service.saveWithLockAndReadNested(1, "_3", 2);
            });
            submit1.get();
            submit2.get();
        }
        Person person = service.get(1);
        log.info("person finish = " + person);
        executor.shutdown();
        // first tx get lock
        // second tx (nested) requered new not release lock
        // third tx wait first tx then update name
        if (!"Pushkin_2_2_3".equals(person.getName())
                || !"Esenin_2".equals(service.get(2).getName())) {
            throw new RuntimeException("test fail");
        }
    }

    public void testReentrentLockAndSessionCache(PersonService service) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        {
            Person person = service.get(1);
            log.info("person = " + person);
            Future<?> submit1 = executor.submit(() -> service
                    .saveWithLockWithNested(1, 4_000, "_2", 1, false));
            submit1.get();
        }
        Person person = service.get(1);
        log.info("person finish = " + person);
        executor.shutdown();
        //+_2 first transaction
        //+_2 nested transacion , session cache
        //+_2 first transaction
        if (!"Pushkin_2_2_2".equals(person.getName())) {
            throw new RuntimeException("assert fail");
        }
    }
}
