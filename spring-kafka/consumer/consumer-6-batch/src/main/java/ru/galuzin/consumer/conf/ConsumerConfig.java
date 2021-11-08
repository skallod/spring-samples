package ru.galuzin.consumer.conf;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.ExponentialBackOff;
import org.springframework.util.backoff.FixedBackOff;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.TopicPartitionOffset;
import ru.galuzin.dto.Author;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class ConsumerConfig {
    final Logger log = LoggerFactory.getLogger(ConsumerConfig.class);

    final String bootstrapServers = "localhost:9092";

    @Value("${ru.galuzin.consumer.attempts:9223372036854775807}")
    long attempts;

    @Bean
    public ConcurrentMessageListenerContainer<String, Author> consumerContainer(
    ) {

        final ContainerProperties containerProperties = new ContainerProperties("test_galuzin_topic");
//        SeekToCurrentBatchErrorHandler
        final SeekToCurrentBatchErrorHandler errorHandler = new SeekToCurrentBatchErrorHandler();
        errorHandler.setBackOff(new FixedBackOff(3_000, attempts));

        //на recovery можно переотправить сообщение в другую очередь
//        final RecoveringBatchErrorHandler errorHandler = new RecoveringBatchErrorHandler (
//                new FixedBackOff(5000L, 3)
//        );

//        errorHandler.setAckAfterHandle(true);
//        errorHandler.setCommitRecovered(false);
        final AtomicInteger counter = new AtomicInteger(0);
        containerProperties.setMessageListener(
                new BatchAcknowledgingMessageListener<String, Author>() {
                    @Override
                    public void onMessage(List<ConsumerRecord<String, Author>> data, Acknowledgment acknowledgment) {
                        if (!data.isEmpty()) {
                            final ConsumerRecord<String, Author> record = data.iterator().next();
                            log.info("batch list size {}, {}, {}, {}, {} {}",
                                    data.size(), record.topic(), record.partition(), record.offset(), record.key(), counter);
                            counter.incrementAndGet();
                        }
                        // save to db
                        if (true) {
                            throw new IllegalStateException("db connection fail");
                        }
                        acknowledgment.acknowledge();
                    }
                }
        );
        containerProperties.setAckMode(ContainerProperties.AckMode.MANUAL);

        ConsumerFactory<String, Author> consumerFactory = new DefaultKafkaConsumerFactory<>(
                consumerProperties(),
                new StringDeserializer(),
                new JsonDeserializer<>(Author.class));
        ConcurrentMessageListenerContainer<String, Author> listenerContainer = new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);
        listenerContainer.setConcurrency(2);
        listenerContainer.setBatchErrorHandler(errorHandler);

        listenerContainer.start();
        return listenerContainer;
    }

    @Bean
    public Map<String, Object> consumerProperties() {
        Map<String, Object> consumer = new HashMap<>();
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        //обработка ошибок дес
//        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
//        consumer.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, "test_galuzin_consumer");
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);//push every n ms
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1000);
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 10_000);
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1048576);
//        consumer.putAll(sslConfigProperties.toMap());
        return consumer;
    }
}
