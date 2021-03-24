package ru.galuzin.consumer.conf;

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
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.TopicPartitionOffset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Configuration
//@EnableKafka
public class ConsumerConfig {

    private static final Logger log = LoggerFactory.getLogger(ConsumerConfig.class);

    //@Value("${my.producer.bootstrapServers}")
    public static final String GROUP_ID = "test_slow_consumer";
    private static final String TOPIC_NAME = "test_galuzin";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    private volatile boolean switcher = true;
    private int i = 10;

    @Bean
    public ConcurrentMessageListenerContainer<String, String> consumerContainer2() {
        final ContainerProperties containerProperties = new ContainerProperties(getTPO());//TOPIC_NAME);
        containerProperties.setMessageListener(new MessageListener<String,String>() {
            @Override
            public void onMessage(ConsumerRecord<String, String> data) {
                log.info("data handle start {}", i);
                try {
                    Thread.sleep(i * 1000);
                    //rebalanceInitiator();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("data.value() = " + data.value());
            }
        });
        containerProperties.setAckMode(ContainerProperties.AckMode.RECORD);

        ConsumerFactory<String, String> consumerFactory =
                new DefaultKafkaConsumerFactory<>(consumerProperties("none"));
        ConcurrentMessageListenerContainer<String, String> listenerContainer = new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);
        listenerContainer.setConcurrency(2);

        listenerContainer.start();
        return listenerContainer;
    }

    private TopicPartitionOffset[] getTPO () {
        //topic start offset test_galuzin 1 300
        //topic start offset test_galuzin 0 0
        return new TopicPartitionOffset[]{
                new TopicPartitionOffset(TOPIC_NAME, 0, 0L),
                new TopicPartitionOffset(TOPIC_NAME, 1, 250L)
        };
    }

    private void rebalanceInitiator() {
        if (i < 5 && switcher) {
            i ++;
            if (i == 5) {
                switcher = false;
            }
        } else if ( i > 1 ) {
            i --;
        }
    }

    public Map<String, Object> consumerProperties(String reset) {
        Map<String, Object> consumer = new HashMap<>();
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, reset);
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);//push every n ms
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 100);
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 11_000);
        return consumer;
    }
}
