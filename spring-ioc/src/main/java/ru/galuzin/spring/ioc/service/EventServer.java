package ru.galuzin.spring.ioc.service;

import com.google.common.util.concurrent.ListenableFutureTask;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventServer {

    private final Collection<EventHandler> eventHandlers;

    private final ThreadPoolTaskExecutor executor;

    public EventServer(Collection<EventHandler> eventHandlers) {
        this.eventHandlers = eventHandlers;
        executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setThreadNamePrefix("event-executor");
        executor.initialize();
    }

    public void register() {
        eventHandlers.forEach(eh -> eh.onEvent());
    }

    public com.google.common.util.concurrent.ListenableFutureTask<Void> registerAsync() {
        ListenableFutureTask<Void> future = ListenableFutureTask.create(() -> {
            System.out.println("curr thread " + Thread.currentThread().getName());
            register();
            return null;
        });
        executor.submit(future);
        return future;
    }

    public ListenableFuture<?> registerEvent() {
        return executor.submitListenable(() -> {
            System.out.println("curr thread " + Thread.currentThread().getName());
            register();
        });
    }

    public void close(){
        executor.shutdown();
    }

}
