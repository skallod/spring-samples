package ru.galuzin.springrest;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

@SpringBootApplication
@EnableSwagger2
public class SpringRestApplication {

    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(SpringRestApplication.class, args);
//        Gauge
//                .builder("first.counter")
//                // description только один раз для каждой метрики
//                .description("first counter descr")
//                .tag("method", "get")
//                // можно inject MeterRegistry а можно глобальный CompositeRegistry
//                .register(Metrics.globalRegistry);
        //todo context aware
        final String s = UUID.randomUUID().toString();
        System.out.println("s = " + s);
        final ThreadPoolTaskExecutor executor = context.getBean("appTaskExecutor", ThreadPoolTaskExecutor.class);
        final MeterRegistry meterRegistry = context.getBean(MeterRegistry.class);
        meterRegistry.gauge("galExecutorQueueSize",
                executor.getThreadPoolExecutor().getQueue(), BlockingQueue::size);
        meterRegistry.gauge("galExecutorActiveCount",
                executor.getThreadPoolExecutor(), ThreadPoolExecutor::getActiveCount);
    }

}
