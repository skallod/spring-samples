package ru.galuzin.spring.ioc.service;

import org.springframework.beans.factory.annotation.Autowired;

public class LogService {

    @Autowired
    StartStopService startStopService;

    public LogService() {
        System.out.println("log-service constructor");
    }

    public void log(String str) {
        System.out.println("log-service str = " + str);
    }
}
