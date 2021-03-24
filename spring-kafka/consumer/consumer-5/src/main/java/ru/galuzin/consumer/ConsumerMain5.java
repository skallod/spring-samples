package ru.galuzin.consumer;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.UnknownTopicOrPartitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ConsumerMain5 {

    public static final Logger log = LoggerFactory.getLogger(ConsumerMain5.class);

    final static String consumerGroup = "test_slow_consumer";
    final static String TOPIC = "test_galuzin";
    final static String bootstrapConf = "localhost:9092";

    public static void main(String[] args) throws Exception {
        final Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapConf);
        props.put("group.id", consumerGroup);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "none");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        saveOffsets(props);
        if (true) return;

        AdminClient adminClient = AdminClient.create(props);

//        try {
        //кидает ExecutionException UnknownTopicOrPartitionException если нет топика
        final Map<String, TopicDescription> topicDescriptionMap = getTopicDescription(adminClient);
        log.info("topic description {}", topicDescriptionMap);
        final Map<TopicPartition, OffsetAndMetadata> consumerOffsetMap = adminClient.listConsumerGroupOffsets(consumerGroup).partitionsToOffsetAndMetadata().get();
        log.info("galuzin consumer map is empty {}  ; {}", consumerOffsetMap.isEmpty(), consumerOffsetMap);
        //check consumer has not already subscribed
        if (consumerOffsetMap.keySet().stream().noneMatch(key -> key.topic().equals(TOPIC))) {
//may be use if consumer.beggingOffsets not work
//            final Map<TopicPartition, OffsetSpec> topicOffsetsRequest = topicDescriptionMap.get(TOPIC).partitions().stream().
//                    collect(Collectors.toMap(partInfo -> new TopicPartition(TOPIC, partInfo.partition()), partInfo -> OffsetSpec.earliest()));
//            final Map<TopicPartition, ListOffsetsResult.ListOffsetsResultInfo> topicStartOffstets = adminClient.listOffsets(topicOffsetsRequest).all().get();
//            log.info("topicStartOffstets = " + topicStartOffstets);
            saveOffsets(props);
        }
    }

    private static Map<String, TopicDescription> getTopicDescription(AdminClient adminClient) throws InterruptedException, ExecutionException {
        final Map<String, TopicDescription> topicDescriptionMap;
        try {
            topicDescriptionMap = adminClient.describeTopics(Collections.singletonList(TOPIC)).all().get();
            System.out.println("stringTopicDescriptionMap = " + topicDescriptionMap);
        } catch (Exception e) {
            if (e.getCause() instanceof UnknownTopicOrPartitionException) {
                throw new RuntimeException("Не найден топик " + TOPIC);
            } else {
                throw e;
            }
        }
        return topicDescriptionMap;
    }

    private static void saveOffsets(Properties props) {
        KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<String, byte[]>(props);
        try {
            consumer.subscribe(Collections.singleton(TOPIC), new SaveOffsetsOnRebalance(consumer));
            final ConsumerRecords<String, byte[]> poll = consumer.poll(100);
            System.out.println("poll = " + poll);
            consumer.beginningOffsets(consumer.assignment()).entrySet().forEach(entry -> {
                log.info("begging offset {} {} {}", entry.getKey().topic(), entry.getKey().partition(), entry.getValue());
            });
            // good
            final Map<TopicPartition, OffsetAndMetadata> offsetMap = consumer.beginningOffsets(consumer.assignment()).entrySet().stream().
                    collect(Collectors.toMap(entry -> entry.getKey(), entry -> new OffsetAndMetadata(entry.getValue())));
            consumer.commitSync(offsetMap);
        } finally {
            consumer.close();
        }
        log.info("consumer work finish");
    }

    public static class SaveOffsetsOnRebalance implements ConsumerRebalanceListener {

        KafkaConsumer<String, byte[]> consumer;

//        Map<String, Long> mainConsumerOffsets;

        public SaveOffsetsOnRebalance(KafkaConsumer<String, byte[]> consumer/*, Map<String, Long> mainConsumerOffsets*/) {
            this.consumer = consumer;
//            this.mainConsumerOffsets = mainConsumerOffsets;
        }

        public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
            //Перед ребалансировкой
        }

        //вызывается при poll
        public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
            for (TopicPartition partition : partitions) {
                log.info("assign topic {} partition {}", partition.topic(), partition.partition());
//                consumer.seek(partition, 0);
            }
            consumer.seekToBeginning(partitions);//good - перекинул на 0 хотя был офсет 70
        }
    }
}
