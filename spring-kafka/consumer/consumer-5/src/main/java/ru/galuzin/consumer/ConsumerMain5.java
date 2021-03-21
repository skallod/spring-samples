package ru.galuzin.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

public class ConsumerMain5 {

    public static final Logger log = LoggerFactory.getLogger(ConsumerMain5.class);

    final static String consumerGroup = "test_slow_consumer_galuzin_2";
    final static String TOPIC = "test_galuzin";
    final static String bootstrapConf = "localhost:9092";

    public static void main(String[] args) {
        final Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapConf);
        props.put("group.id", consumerGroup);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);


//        props.put(ConsumerConfig.CONCUR);
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        try {
            consumer.subscribe(Collections.singleton(TOPIC), new ConsumerMain5.SaveOffsetsOnRebalance(consumer, Collections.<String, Long>emptyMap()));
            consumer.poll(0);
            consumer.beginningOffsets(consumer.assignment()).entrySet().forEach(entry -> {
                log.info("begging offset {} {} {}", entry.getKey().topic(), entry.getKey().partition(), entry.getValue());
            });
//            consumer.commitSync();
        } finally {
            consumer.close();
        }
    }

    public static class SaveOffsetsOnRebalance implements ConsumerRebalanceListener {

        KafkaConsumer<String, String> consumer;

        Map<String, Long> mainConsumerOffsets;

        public SaveOffsetsOnRebalance(KafkaConsumer<String, String> consumer, Map<String, Long> mainConsumerOffsets) {
            this.consumer = consumer;
            this.mainConsumerOffsets = mainConsumerOffsets;
        }

        public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
            //Перед ребалансировкой
        }
        //вызывается при poll
        public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
            for(TopicPartition partition: partitions) {
                log.info("assign topic {} partition {}", partition.topic(), partition.partition());
//                consumer.seek(partition, 0);
            }
            consumer.seekToBeginning(partitions);//good - перекинул на 0 хотя был офсет 70
        }
    }
}
