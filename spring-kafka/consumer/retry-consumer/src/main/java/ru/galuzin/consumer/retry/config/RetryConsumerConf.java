package ru.galuzin.consumer.retry.config;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.RoundRobinAssignor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.serializer.*;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import ru.galuzin.dto.Book;

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
    public ProducerFactory<String, Book> createProducerFactory() {
        final Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());

        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, Book> mainContainer() {
        final ContainerProperties containerProperties = new ContainerProperties(MAIN_TOPIC);
        KafkaTemplate<String, Book> kafkaTemplate = new KafkaTemplate<>(createProducerFactory());
        containerProperties.setMessageListener(new MyMessageListener(kafkaTemplate));
        containerProperties.setAckMode(ContainerProperties.AckMode.RECORD);
        ConcurrentMessageListenerContainer<String, Book> listenerContainer =
                new ConcurrentMessageListenerContainer<>(consumerFactory(), containerProperties);
        listenerContainer.setConcurrency(10);
        final BackOff backOff = new FixedBackOff(10_000, FixedBackOff.UNLIMITED_ATTEMPTS);

        SeekToCurrentErrorHandler errorHandler = new SeekToCurrentErrorHandler(new DeadLetterPublishingRecoverer(kafkaTemplate, (cr, e) -> {
            //если npe сюда не заходит
            //ecли deserial except сюда заходит
            System.out.println("gal th = " + Thread.currentThread().getId() + " dlt exc = " + e.getMessage());
            if (e.getCause() != null && e.getCause() instanceof DeserializationException) {
                return new TopicPartition(COMPACTED_TOPIC, cr.partition());
            } else {
                return new TopicPartition(COMPACTED_TOPIC, cr.partition());
            }
        }), backOff);

        /* not work {
            @Override
            public void handle(Exception e, ConsumerRecord<?, ?> data) {
                System.out.println("gal th = " + Thread.currentThread().getId() + " first m override = " + e.getMessage());
            }

            @Override
            public void handle(Exception e, ConsumerRecord<?, ?> data, Consumer<?, ?> consumer) {
                System.out.println("gal th = " + Thread.currentThread().getId() + " second m override = " + e.getMessage());
            }
        };

        /*{ records list unknown which is exception
            @Override
            public void handle(Exception e, List<ConsumerRecord<?, ?>> records,
                               Consumer<?, ?> consumer, MessageListenerContainer container) {
                System.out.println("gal th = " + Thread.currentThread().getId() + " handle override = " + e.getMessage());
                if (e.getCause() != null && e.getCause() instanceof DeserializationException) {
                    System.out.println("gal th = " + Thread.currentThread().getId() + " DLT");
                } else {
                    System.out.println("gal th = " + Thread.currentThread().getId() + " RETRY");
                    handleExceptoin();
                }
                super.handle(e, records, consumer, container);
            }
        };*/
        listenerContainer.setErrorHandler(errorHandler);
        listenerContainer.start();
        return listenerContainer;
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, Book> compactedContainer() {
        final ContainerProperties containerProperties = new ContainerProperties(COMPACTED_TOPIC);
        containerProperties.setMessageListener(new CompactedMessageListener());
        containerProperties.setAckMode(ContainerProperties.AckMode.RECORD);
        ConcurrentMessageListenerContainer<String, Book> listenerContainer =
                new ConcurrentMessageListenerContainer<>(consumerFactory(), containerProperties);
        listenerContainer.setConcurrency(10);
        final BackOff backOff = new FixedBackOff(10_000, FixedBackOff.UNLIMITED_ATTEMPTS);
        final SeekToCurrentErrorHandler errorHandler = new SeekToCurrentErrorHandler(backOff);
        listenerContainer.setErrorHandler(errorHandler);
        listenerContainer.start();
        return listenerContainer;
    }

    @Bean
    public ConsumerFactory<String, Book> consumerFactory() {
        Map<String, Object> consumer = new HashMap<>();
        consumer.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        consumer.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        consumer.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumer.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        consumer.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
//        ErrorHandlingDeserializer<Book> errorHandlingDeserializer =
//                new ErrorHandlingDeserializer<>(new JsonDeserializer<>(Book.class));
        //consumer.put(JsonDeserializer.TRUSTED_PACKAGES, "ru.sbrf.journal.standin");
        consumer.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumer.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);//push every n ms
        consumer.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 60000);
        //consumer.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1000);

//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfiguration.getBootstrapServers());
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
//        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        consumer.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        consumer.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, RoundRobinAssignor.class.getName());
        consumer.put(ConsumerConfig.GROUP_ID_CONFIG, "gal-group-1");
        return new DefaultKafkaConsumerFactory<>(consumer
        );
//            , new StringDeserializer(), errorHandlingDeserializer );
    }

    private class MyMessageListener implements MessageListener<String, Book> {

        KafkaOperations<String, Book> kafkaTemplate;


        public MyMessageListener(KafkaOperations<String, Book> kafkaTemplate) {
            this.kafkaTemplate = kafkaTemplate;
        }

        @Override
        public void onMessage(ConsumerRecord<String, Book> data) {
            String key = data.key();
            Book value = data.value();
            try {
                System.out.println("gal th = " + Thread.currentThread().getId()
                        + " ; key = " + key + " ; val = " + value + " ; part = " + data.partition() + " ; off = " + data.offset());
                System.out.println("gal th = " + Thread.currentThread().getId() + " author = " + value.getAuthor8().length());
            }catch ( Exception e) {
                System.out.println("gal th = " + Thread.currentThread().getId() + "e.getMessage() = " + e.getMessage());
                handleExceptoin(data, key, value, e, kafkaTemplate);
                throw e;
            }
//            if ( value.getAuthor().equals("pushka") && data.partition() == 1
//                && mainTopicCounter.incrementAndGet() < 10 ) {
//                throw new IllegalStateException("unknown state");
//            }
        }

    }

    private void handleExceptoin(ConsumerRecord<String, Book> data, String key, Book value, Exception e, KafkaOperations<String, Book> kafkaTemplate) {
        try{
            Message<Book> message = MessageBuilder
                    .withPayload(value)
                    .setHeader(KafkaHeaders.TOPIC, COMPACTED_TOPIC)
                    .setHeader(KafkaHeaders.MESSAGE_KEY, key)
                    .setHeader(KafkaHeaders.DLT_EXCEPTION_FQCN, e.getClass().getName().getBytes())
                    .setHeader(KafkaHeaders.DLT_EXCEPTION_MESSAGE, Optional.ofNullable(e.getMessage()).map(String::getBytes).orElse(null))
                    .setHeader(KafkaHeaders.DLT_EXCEPTION_STACKTRACE, ExceptionUtils.getStackTrace(e))
                    .setHeader(KafkaHeaders.DLT_ORIGINAL_TOPIC, data.topic().getBytes(StandardCharsets.UTF_8))
                    .setHeader(KafkaHeaders.DLT_ORIGINAL_PARTITION, ByteBuffer.allocate(Integer.BYTES).putInt(data.partition()).array())
                    .setHeader(KafkaHeaders.DLT_ORIGINAL_OFFSET, ByteBuffer.allocate(Long.BYTES).putLong(data.offset()).array())
                    .setHeader(KafkaHeaders.DLT_ORIGINAL_TIMESTAMP, ByteBuffer.allocate(Long.BYTES).putLong(data.timestamp()).array())
                    .setHeader(KafkaHeaders.DLT_ORIGINAL_TIMESTAMP_TYPE, data.timestampType().toString().getBytes(StandardCharsets.UTF_8))
                    .build();
            kafkaTemplate.send(message);
            kafkaTemplate.flush();
        }catch (Exception e1) {
            System.out.println("gal th = " + Thread.currentThread().getId() + "; e1.getMessage() = " + e1.getMessage());
            e1.printStackTrace();
        }
    }

    private class CompactedMessageListener implements MessageListener<String, Book> {

        public CompactedMessageListener() {
        }

        @Override
        public void onMessage(ConsumerRecord<String, Book> data) {
            final String key = data.key();
            final Book value = data.value();
//            changeHandler.handleChange(key, value);
            //handle
            String errMesHeader = Arrays.stream(data.headers().toArray()).filter(h -> h.key().equals(KafkaHeaders.DLT_EXCEPTION_MESSAGE))
                    .findAny().map(h->h.value()).map(bytes -> new String(bytes)).orElse(null);
            String errStack = Arrays.stream(data.headers().toArray()).filter(h -> h.key().equals(KafkaHeaders.DLT_EXCEPTION_STACKTRACE))
                    .findAny().map(h->h.value()).map(bytes -> new String(bytes)).orElse(null);
            //todo попытаться вывести инфу об ошибке
            System.out.println("gal th = "+ Thread.currentThread().getId()
                    + " ; key = " + key + " ; val = " + value + " ; part = " + data.partition() + " ; off = " + data.offset() +
                    " ; err mess = " + errMesHeader + " ; err stack =" + errStack
            );
            //System.out.println("gal th = " + Thread.currentThread().getId() + " author " + value.getAuthor4().length());
//            if ( value.getAuthor().equals("compact") && compactedTopicCounter.incrementAndGet() < 10 ) {
//                throw new IllegalStateException("unknown compacted");
//            }
        }
    }
}
