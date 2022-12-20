package ru.galuzin.springrest.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class MetricController {

    private final Counter firstCounter; /*= Counter
            .builder("first.counter")
            // description только один раз для каждой метрики
            .description("first counter descr")
            .tag("method", "get")
            // можно inject MeterRegistry а можно глобальный CompositeRegistry
            .register(Metrics.globalRegistry);*/

    private final ThreadPoolTaskExecutor taskExecutor;

    // проверить в метриках
    // повесить мертику длина очереди

    @Autowired
    public MetricController(MeterRegistry meterRegistry,
                            @Qualifier("appTaskExecutor") ThreadPoolTaskExecutor taskExecutor) {
        this.firstCounter = Counter
                .builder("first.counter")
                .description("first counter descr")
                .tag("method", "get")
                .register(meterRegistry);
        this.taskExecutor = taskExecutor;
    }

    @GetMapping(path = "/getWithCounter")
    public Map<String, Object> get() {
        firstCounter.increment();
        double value = firstCounter.count();
        return Map.of("firstCounterValue", (long) value);
    }

    @GetMapping(path = "/getWithThread")
    public Map<String, Object> getWithThread() {
        taskExecutor.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        });
        return Map.of("taskValue", "done" );
    }
}