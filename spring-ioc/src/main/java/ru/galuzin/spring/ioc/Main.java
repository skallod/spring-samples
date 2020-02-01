package ru.galuzin.spring.ioc;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFutureTask;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ru.galuzin.spring.ioc.config.ServicesConfig;
import ru.galuzin.spring.ioc.domain.EventHandler;
import ru.galuzin.spring.ioc.domain.EventServer;
import ru.galuzin.spring.ioc.domain.Person;
import ru.galuzin.spring.ioc.service.OtherService;
import ru.galuzin.spring.ioc.service.PersonService;

import java.util.concurrent.ExecutionException;

//@ComponentScan
public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        //context.register(Main.class);
        context.register(ServicesConfig.class);
        context.refresh();

        PersonService service = context.getBean(PersonService.class);

        Person person = service.getByName("petya");
        System.out.println(person);

        person = service.getByName("petya");
        System.out.println(person);

        System.out.println("service = " + service);

        OtherService otherService = context.getBean(OtherService.class);
        System.out.println("otherService = " + otherService);

        //no qualifying
        //EventHandler eventHandler = context.getBean(EventHandler.class);
        //eventHandler.onEvent(new Object());

        EventServer eventServer = context.getBean(EventServer.class);
        ListenableFutureTask<Void> future = eventServer.registerAsync();
        Futures.addCallback(future, new FutureCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("success "+Thread.currentThread().getName());
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("fail "+Thread.currentThread().getName());
            }
        });
        context.close();
    }
}
