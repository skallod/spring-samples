package ru.galuzin.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;

@SpringBootApplication(exclude = KafkaAutoConfiguration.class)
public class BatchConsumer2Main {
    public static void main(String[] args) {
        SpringApplication.run(BatchConsumer2Main.class);
    }
}
