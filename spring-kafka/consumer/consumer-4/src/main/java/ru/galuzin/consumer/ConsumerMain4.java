package ru.galuzin.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;

@SpringBootApplication(exclude = KafkaAutoConfiguration.class)
public class ConsumerMain4 {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerMain4.class , args);
    }

}
