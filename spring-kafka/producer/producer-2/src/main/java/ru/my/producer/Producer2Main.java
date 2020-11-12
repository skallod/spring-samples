package ru.my.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
public class Producer2Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(Producer2Main.class, args);
        {
            KafkaTemplate<String, String> template = (KafkaTemplate<String, String>) context.getBean("templateString");
            ListenableFuture<SendResult<String, String>> send = template.send("test_galuzin_topic", UUID.randomUUID().toString(), "{\"author8\":\"from string\"}");
            System.out.println("result = " + send.get().toString());
        }
    }
}
