package ru.pesok.graviy.spring.orm;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.pesok.graviy.spring.orm.domain.TaskType;
import ru.pesok.graviy.spring.orm.service.PersonService;
import ru.pesok.graviy.spring.orm.service.TaskService;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@SpringBootApplication
public class Main {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class);
        TaskService taskService1 = context.getBean(TaskService.class);
//        while (true) {
            taskService1.getAll();
            Thread.sleep(1_000);
//        }
    }

    @Autowired
    private PersonService service;

    @Autowired
    private TaskService taskService;

    @PostConstruct
    public void init() {
        System.out.println("init start");
//        taskService.save(new TaskType(7,"DATA_TEST"));
//        List<TaskType> all = taskService.getAll();
//        System.out.println("all = " + all);
//        service.save(new Person(1,"Pushkin","room"));
//        service.save(new Person(2,"Errt","kitchen"));
//        service.save(new Person(3,"Ghh","room"));
//        service.save(new Person(4,"Zxx","room"));
//        service.save(new Person(5,"Xcc","kitchen"));
//        //service.save(new Person(1,"Pushkin"));
//        List<Person> all = service.getAll();
//        System.out.println("before "+all);
//        service.deleteFromZone("kitchen");
//        all = service.getAll();
//        System.out.println("after "+all);
    }
}
