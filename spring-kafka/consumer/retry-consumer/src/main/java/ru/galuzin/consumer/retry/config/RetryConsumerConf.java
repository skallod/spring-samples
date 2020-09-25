package ru.galuzin.consumer.retry.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.RoundRobinAssignor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@EnableKafka
public class RetryConsumerConf {


    public static final String COMPACTED_TOPIC = "gal-compacted-topic";

    public static final String MAIN_TOPIC = "gal-test-topic";

    AtomicInteger mainTopicCounter = new AtomicInteger(0);

    AtomicInteger compactedTopicCounter = new AtomicInteger(0);

    //not work
//    @Bean
//    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>>
//    refreshKafkaListenerContainerFactory(
//            ConsumerFactory<String, String> refreshConsumerFactory
//    ) {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//
//        factory.setConsumerFactory(refreshConsumerFactory);
//        factory.setConcurrency(3);
//        factory.setAutoStartup(true);
//        factory.setBatchListener(false);
//        factory.getContainerProperties().setMessageListener(new MyMessageListener());
//        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
//        final BackOff backOff = new FixedBackOff(10_000, FixedBackOff.UNLIMITED_ATTEMPTS);
//        final SeekToCurrentErrorHandler errorHandler = new SeekToCurrentErrorHandler(backOff);
//        factory.setErrorHandler(errorHandler);
//        return factory;
//    }

    @Bean
    public ProducerFactory<String, String> createProducerFactory() {
        final Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, String> mainContainer() {
        final ContainerProperties containerProperties = new ContainerProperties(MAIN_TOPIC);
        containerProperties.setMessageListener(new MyMessageListener(new KafkaTemplate<>(createProducerFactory())));
        containerProperties.setAckMode(ContainerProperties.AckMode.RECORD);
        ConcurrentMessageListenerContainer<String, String> listenerContainer =
                new ConcurrentMessageListenerContainer<>(consumerFactory(), containerProperties);
        listenerContainer.setConcurrency(10);
        final BackOff backOff = new FixedBackOff(10_000, FixedBackOff.UNLIMITED_ATTEMPTS);
        final SeekToCurrentErrorHandler errorHandler = new SeekToCurrentErrorHandler(backOff);
        listenerContainer.setErrorHandler(errorHandler);
        listenerContainer.start();
        return listenerContainer;
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, String> compactedContainer() {
        final ContainerProperties containerProperties = new ContainerProperties(COMPACTED_TOPIC);
        containerProperties.setMessageListener(new CompactedMessageListener());
        containerProperties.setAckMode(ContainerProperties.AckMode.RECORD);
        ConcurrentMessageListenerContainer<String, String> listenerContainer =
                new ConcurrentMessageListenerContainer<>(consumerFactory(), containerProperties);
        listenerContainer.setConcurrency(10);
        final BackOff backOff = new FixedBackOff(10_000, FixedBackOff.UNLIMITED_ATTEMPTS);
        final SeekToCurrentErrorHandler errorHandler = new SeekToCurrentErrorHandler(backOff);
        listenerContainer.setErrorHandler(errorHandler);
        listenerContainer.start();
        return listenerContainer;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> consumer = new HashMap<>();
        consumer.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        consumer.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumer.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        //consumer.put(JsonDeserializer.TRUSTED_PACKAGES, "ru.sbrf.journal.standin");
        consumer.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumer.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);//push every n ms
        //consumer.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1000);

//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfiguration.getBootstrapServers());
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
//        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
//        props.put(JsonDeserializer.TRUSTED_PACKAGES, "ru.vtb.smartreplication.api.dto");
//        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        consumer.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, RoundRobinAssignor.class.getName());
        consumer.put(ConsumerConfig.GROUP_ID_CONFIG, "gal-group-1");
        return new DefaultKafkaConsumerFactory<>(consumer);
    }

    private class MyMessageListener implements MessageListener<String, String> {

        KafkaOperations<String, String> kafkaTemplate;


        public MyMessageListener(KafkaOperations<String, String> kafkaTemplate) {
            this.kafkaTemplate = kafkaTemplate;
        }

        @Override
        public void onMessage(ConsumerRecord<String, String> data) {
            final String key = data.key();
            final String value = data.value();
//            changeHandler.handleChange(key, value);
            //handle
            System.out.println("gal th = "+ Thread.currentThread().getId()
                    + " ; key = " + key + " ; val = " + value + " ; part = " + data.partition() + " ; off = " + data.offset());
            int valInt = -1;
            try {
                valInt = Integer.parseInt(value);
            } catch (Exception e){
                e.printStackTrace();
            }
            if ( valInt == 3 && data.partition() == 8
                && mainTopicCounter.incrementAndGet() < 10 ) {
                Message<String> message = MessageBuilder
                        .withPayload(value)
                        .setHeader(KafkaHeaders.TOPIC, COMPACTED_TOPIC)
                        .setHeader(KafkaHeaders.MESSAGE_KEY, key)
                        .setHeader(KafkaHeaders.PARTITION_ID, data.partition())
                        .setHeader(KafkaHeaders.DLT_EXCEPTION_FQCN, "DLT_EXCEPTION_FQCN")
                        .setHeader(KafkaHeaders.DLT_EXCEPTION_MESSAGE, "main counter "+mainTopicCounter.get())
                        .setHeader(KafkaHeaders.DLT_EXCEPTION_STACKTRACE, "unknown state(RetryConsumerConf:156)")
                        .build();
                kafkaTemplate.send(message);
                kafkaTemplate.flush();
                throw new IllegalStateException("unknown state");
            }
        }
    }

    private class CompactedMessageListener implements MessageListener<String, String> {

        public CompactedMessageListener() {
        }

        @Override
        public void onMessage(ConsumerRecord<String, String> data) {
            final String key = data.key();
            final String value = data.value();
            int valInt = Integer.parseInt(value);
//            changeHandler.handleChange(key, value);
            //handle
            byte[] errMesHeader = Arrays.stream(data.headers().toArray()).filter(h -> h.key().equals(KafkaHeaders.DLT_EXCEPTION_MESSAGE))
                    .findAny().orElse(new Header() {
                        @Override
                        public String key() {
                            return "empty key";
                        }

                        @Override
                        public byte[] value() {
                            return "empty value".getBytes();
                        }
                    }).value();
            System.out.println("gal th = "+ Thread.currentThread().getId()
                    + " ; key = " + key + " ; val = " + value + " ; part = " + data.partition() + " ; off = " + data.offset() +
                    " ; err mess = " + new String(errMesHeader)
            );
            if ( valInt==3 && compactedTopicCounter.incrementAndGet() < 10 ) {
                throw new IllegalStateException("unknown compacted");
            }
        }
    }
}
