package ru.galuzin.springrest.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class GreetingController {

//    private static final String COUNTER_BY_NAME = "counter.by.name";
    public static final String GREETING = "/getTasks";

    private final MeterRegistry meterRegistry;
    Map<String, Counter> counterPerOwner = new ConcurrentHashMap<String, Counter>();

    public GreetingController(final MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @GetMapping(GREETING)
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "None") String name) {
        incrementCounter(RequestMethod.GET.name(), GREETING, name);
        try {
            Thread.sleep(new Random().nextInt(100) + 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hello " + name;
    }

    public void incrementCounter(String method, String uri, String owner) {
        final Counter counter = counterPerOwner.computeIfAbsent(method + uri + owner, key -> buildCounter(method, uri, owner));
        counter.increment();
    }

    public Counter buildCounter(String method, String uri, String owner) {
        return Counter
            .builder("request.counter")
            .description("request counter descr")
            .tag("method", method)
            .tag("uri", uri)
            .tag("owner", owner)
            .register(meterRegistry);
    }

}