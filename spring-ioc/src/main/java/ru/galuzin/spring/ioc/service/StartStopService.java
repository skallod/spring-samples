package ru.galuzin.spring.ioc.service;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class StartStopService implements InitializingBean, DisposableBean {

    public StartStopService() {
        System.out.println("start-stop-service constructor");
    }

    public void afterPropertiesSet() throws Exception {
        System.out.println("start-stop-service start");
    }

    public void destroy() throws Exception {
        System.out.println("start-stop-service stop");
    }
}
