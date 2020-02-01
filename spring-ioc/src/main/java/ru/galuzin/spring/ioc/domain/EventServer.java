package ru.galuzin.spring.ioc.domain;

import com.google.common.util.concurrent.ListenableFutureTask;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventServer {

    private final Collection<EventHandler> eventHandlers;

    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    public EventServer(Collection<EventHandler> eventHandlers){
        this.eventHandlers = eventHandlers;
    }

    public void register(){
        eventHandlers.forEach(eh -> eh.onEvent() );
    }

    public com.google.common.util.concurrent.ListenableFutureTask<Void> registerAsync(){
        ListenableFutureTask<Void> future = ListenableFutureTask.create(() -> {
            System.out.println("curr thread " + Thread.currentThread().getName());
            register();
            return null;
        });
        executor.submit(future);
        return future;
    }
}
