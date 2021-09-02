package ru.galuzin.config_server_client.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class Controller {

    final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Value("${my.greeting:good1}")
    String myGreeting;

    @Value("${my.filtering:false}")
    boolean filtering;

    @GetMapping("/greeting")
    public String greeting() {
        logger.trace("greeting trace {}", filtering);
        return myGreeting;
    }
}
