package ru.galuzin.spring.ioc.service;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class TaskService {

    final ThreadPoolTaskExecutor executor;

    public TaskService() {
        this.executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(0);
        executor.setMaxPoolSize(2);
        executor.setKeepAliveSeconds(42);
        //executor.setQueueCapacity(0);//without it do via 1 thread and put to queue
        executor.initialize();
    }

    public void execute(Runnable task){
        executor.execute(task);
    }

}
