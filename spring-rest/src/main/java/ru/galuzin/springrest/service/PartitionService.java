package ru.galuzin.springrest.service;

import com.galuzin.autoconf.A;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PartitionService {

    private final MeterRegistry meterRegistry;

//    private final A a1;

    private Counter counter;

    @PostConstruct
    public void init() {
        // Init zero value to metric
        counter = Counter.builder
                        ("tasks_scheduled_execution_createPartitions_count")
        //tasks_scheduled_execution_seconds_count is already in use by another Collector of type MicrometerCollector
        //                ("tasks_scheduled_execution_seconds_count")
                .tag("code_function", "createPartitions5")
                .tag("outcome", "SUCCESS")
                .register(meterRegistry);

        //        var summary = meterRegistry.summary("tasks_scheduled_execution_seconds_count",
        //                List.of(Tag.of("code_function", "createPartitions5"), Tag.of("outcome", "SUCCESS")));
        //        summary.record(0);
        //meterRegistry.remove(Counter.builder("tasks_scheduled_execution_seconds_count"))
    }

    //@EventListener(ApplicationReadyEvent.class)
    @Scheduled(cron = "${my.scheduled.cron.expression-value}")
    public void createPartitions5() throws IOException {
        log.info("Creating partition");
        File file = new File("temp" + UUID.randomUUID());
        file.createNewFile();
        if (file.exists()) {
            if (System.currentTimeMillis() % 2 == 0) {
                if (!file.createNewFile()) {
                    log.warn("Unable to create temp file {}", file.getAbsolutePath());
                    //throw new RuntimeException("Partition already exists.");
                }
            }
        }
        counter.increment();
        log.info("Partitions created");
    }
}
