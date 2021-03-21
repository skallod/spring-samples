package ru.galuzin.kafka.monitoring.service;

import com.omarsmak.kafka.consumer.lag.monitoring.client.KafkaConsumerLagClient;
import com.omarsmak.kafka.consumer.lag.monitoring.client.KafkaConsumerLagClientFactory;
import com.omarsmak.kafka.consumer.lag.monitoring.client.data.Lag;
import com.omarsmak.kafka.consumer.lag.monitoring.client.data.Offsets;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListOffsetsResult;
import org.apache.kafka.clients.admin.OffsetSpec;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class LagService {

    final static Logger log = LoggerFactory.getLogger(LagService.class);

    final KafkaConsumerLagClient kafkaConsumerLagClient;

    final String consumerGroup = "test_slow_consumer_galuzin";

    final String bootstrapConf = "localhost:9092";

    final String TOPIC = "test_galuzin";

    private final AdminClient adminClient;

    public LagService() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapConf);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "none");

        kafkaConsumerLagClient = KafkaConsumerLagClientFactory.create(props);

        this.adminClient = AdminClient.create(
                props
        );
    }

    public synchronized List<Lag> getAllLags(String consumerGroup) {
        return kafkaConsumerLagClient.getConsumerLag(consumerGroup);
    }
    public synchronized Optional<Lag> findOne(String consumerGroup, String topicName) {
        return getAllLags(consumerGroup)
                .stream()
                .filter(lag -> lag.getTopicName().equals(topicName))
                .findFirst();
    }
    public void getLag2() {
        try {
            final Lag requestTopicLag = findOne(consumerGroup, TOPIC).orElseThrow(() ->
                    new RuntimeException("Topic {" + TOPIC
                            + "} not found for consumer group {" + consumerGroup + "}"));
            requestTopicLag.getLagPerPartition().entrySet().forEach(lagPP -> {
                log.info("*** lag {} {}", lagPP.getKey(), lagPP.getValue());
            });
            requestTopicLag.getLatestConsumerOffsets().forEach((key, value) -> log.info("*** cons offset {} {}", key, value));
//            requestTopicLag.getLatestTopicOffsets().entrySet().forEach(lagPP -> {
//                log.info("*** topic offset {} {}", lagPP.getKey(), lagPP.getValue());
//            });
//            kafkaConsumerLagClient.getTopicOffsets(TOPIC).getOffsetPerPartition().entrySet().forEach(lagPP -> {
//                log.info("*** topic offset2 {} {}", lagPP.getKey(), lagPP.getValue());
//            });
            final ListOffsetsResult topicStartOffsets = adminClient.listOffsets(
                    requestTopicLag.getLatestTopicOffsets().keySet().stream().map(k -> new TopicPartition(TOPIC, k))
                            .collect(Collectors.toMap(tp -> tp, tp -> OffsetSpec.earliest()))
            );
            final Map<TopicPartition, ListOffsetsResult.ListOffsetsResultInfo> topicPartitionListOffsetsResultInfoMap = topicStartOffsets.all().get();
            topicPartitionListOffsetsResultInfoMap.forEach((key, value) -> log.info("topic start offset {} {} {}", key.topic(), key.partition(), value.offset()));
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public void getLag() {
        final List<Lag> consumerLag = kafkaConsumerLagClient.getConsumerLag(consumerGroup);
        //getMessageFromLag(consumerLag);
    }

    private void getMessageFromLag(List<Lag> consumerLag) {
        final HashSet<String> topics = new HashSet<>();
        final HashMap<String,Long> mainConsumerOffsets = new HashMap<>();
        consumerLag.forEach(lag -> {
            log.info("Consumer lag {}, {}", lag.getTopicName(), lag.getTotalLag());
            topics.add(lag.getTopicName());
            lag.getLatestConsumerOffsets().entrySet().forEach( entry ->
                    mainConsumerOffsets.put(lag.getTopicName()+entry.getKey(),entry.getValue()));
        });
        try {
            monitoringConsumer(topics, mainConsumerOffsets);
        } catch (OffsetOutOfRangeException e) {
            log.error("",e);
        }
    }

    public void monitoringConsumer(Set<String> topics, Map<String, Long> mainConsumerOffsets) {
        final Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapConf);
        props.put("group.id", consumerGroup + "_monitoring");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);


//        props.put(ConsumerConfig.CONCUR);
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        try {
            consumer.subscribe(topics, new LagService.SaveOffsetsOnRebalance(consumer, mainConsumerOffsets));
            consumer.poll(0);

//            for (TopicPartition partition : consumer.assignment())
//                consumer.seek(partition, mainConsumerOffsets.get(partition.topic() + partition.partition()));

            ConsumerRecords<String, String> records = consumer.poll(100);
            final Map<String, String> firstMessageInLag = new HashMap<>();
            for (ConsumerRecord<String, String> record : records) {
                firstMessageInLag.putIfAbsent(record.topic()+"-"+record.partition(), record.value());
            }
            firstMessageInLag.entrySet().forEach(enrty -> {
                log.info("first message in lag {} {}", enrty.getKey(), enrty.getValue());
            });
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
                final Long offset = mainConsumerOffsets.get(partition.topic() + partition.partition());
                consumer.seek(partition, offset);
                log.info("assign topic {} partition {} offset {}", partition.topic(), partition.partition(), offset);
            }
        }
    }

}
