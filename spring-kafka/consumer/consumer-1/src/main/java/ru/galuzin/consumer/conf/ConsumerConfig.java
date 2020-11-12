package ru.galuzin.consumer.conf;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class ConsumerConfig {

    @Value("${my.producer.bootstrapServers}")
    String bootstrapServers;

    @Autowired
    SslConfigProperties sslConfigProperties;

    @Bean
    public ConcurrentMessageListenerContainer<String, String> consumerContainer(
    ) {

        final ContainerProperties containerProperties = new ContainerProperties("test_galuzin_topic");
        containerProperties.setMessageListener(new MessageListener<String,String>() {
            @Override
            public void onMessage(ConsumerRecord<String, String> data) {
                System.out.println("data.value() = " + data.value());
            }
        });
        containerProperties.setAckMode(ContainerProperties.AckMode.RECORD);

        ConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProperties());
        ConcurrentMessageListenerContainer<String, String> listenerContainer = new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);
        listenerContainer.setConcurrency(2);

        listenerContainer.start();
        return listenerContainer;
    }

    @Bean
    public Map<String, Object> consumerProperties() {
        Map<String, Object> consumer = new HashMap<>();
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, "test_galuzin_consumer");
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);//push every n ms
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1000);
        consumer.putAll(sslConfigProperties.toMap());
        return consumer;
    }
}
