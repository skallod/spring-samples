package ru.galuzin.springrest;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

@SpringBootApplication
@Slf4j
public class SpringRestApplication {

    public static void main(String[] args) {
//        Temp opposite = opposite(null);
        final ConfigurableApplicationContext context = SpringApplication.run(SpringRestApplication.class, args);
//        Gauge
//        .builder("first.counter")
//        // description только один раз для каждой метрики
//        .description("first counter descr")
//        .tag("method", "get")
//         // можно inject MeterRegistry а можно глобальный CompositeRegistry
//        .register(Metrics.globalRegistry);
        //todo context aware
//        final String s = UUID.randomUUID().toString();
//        System.out.println("s = " + s);
//        final ThreadPoolTaskExecutor executor = context.getBean("appTaskExecutor", ThreadPoolTaskExecutor.class);
//        final MeterRegistry meterRegistry = context.getBean(MeterRegistry.class);
//        meterRegistry.gauge("galExecutorQueueSize",
//                executor.getThreadPoolExecutor().getQueue(), BlockingQueue::size);
//        meterRegistry.gauge("galExecutorActiveCount",
//                executor.getThreadPoolExecutor(), ThreadPoolExecutor::getActiveCount);
    }

    static Temp opposite (Temp temp) {
        return switch (temp) {
            case T1 -> Temp.T3;
            case T2 -> Temp.T2;
            case T3 -> {
                log.warn("Unexpected T3 here");
                yield Temp.T1;
            }
            default -> {
                log.warn("Null");
                yield null;
            }
        };
    }

    static enum Temp {T1, T2, T3}

}
