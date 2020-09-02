package ru.galuzin.consumer.batch.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class BatchConsumerConf {

    @Bean
    public Refresher refresher(){
        return new Refresher();
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>>
        refreshKafkaListenerContainerFactory(
            ConsumerFactory<String, String> refreshConsumerFactory,
            Refresher refresher
    ) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(refreshConsumerFactory);
        factory.setConcurrency(3);
        factory.setAutoStartup(true);
        factory.setBatchListener(true);
        factory.getContainerProperties().setMessageListener(refresher);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> refreshConsumerFactory() {
        Map<String, Object> consumer = new HashMap<>();
        consumer.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        consumer.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumer.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        //consumer.put(JsonDeserializer.TRUSTED_PACKAGES, "ru.sbrf.journal.standin");
        consumer.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumer.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);//push every n ms
        consumer.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1000);
        return new DefaultKafkaConsumerFactory<>(consumer);
    }
}
