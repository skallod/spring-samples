package ru.galuzin.consumer.batch.config;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Refresher implements ConsumerSeekAware {

    volatile String currentLastMessage;
    volatile Integer partitionId;//todo thread local

    @KafkaListener(id = "listMsgAckConsumer", topics = "batch_topic", containerFactory = "refreshKafkaListenerContainerFactory")
    public void listen16(List<Message<String>> list
            , Acknowledgment acknowledgment,
                         Consumer<String, String> consumer) {
        acknowledgment.acknowledge();
        Set<TopicPartition> assignment = consumer.assignment();
        Map<Integer, Long> topicPartitionLongMap = consumer.endOffsets(assignment).entrySet().
                stream().collect(Collectors.toMap(e->e.getKey().partition(),Map.Entry::getValue));
        for(Message<String> mes : list) {
            if(mes.getHeaders().get("kafka_receivedMessageKey", String.class).equals("test")) {
                currentLastMessage = mes.getPayload();
                partitionId = mes.getHeaders().get("kafka_receivedPartitionId", Integer.class);
            }
            if(currentLastMessage == null || partitionId == null) {
                continue;
            }
            long mesOffset = mes.getHeaders().get("kafka_offset", Long.class);
            int mesPartitionId = mes.getHeaders().get("kafka_receivedPartitionId", Integer.class);
            if(partitionId!=null &&
                    currentLastMessage!=null &&
                    mesPartitionId == partitionId &&
                    topicPartitionLongMap.get(partitionId)-1 == mesOffset) { //last message
                apply(currentLastMessage==null?"NORMAL":currentLastMessage);
                currentLastMessage = null; //not apply until new message
            }
        }
    }

    private void apply(String s) {
        System.out.println("galuzin apply  = " + s);
    }

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        System.out.println("assignments = " + assignments);
        assignments.forEach((t,o)->callback.seekToBeginning(t.topic(),t.partition()));
    }

//    @Override
//    public void onIdleContainer(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
//        System.out.println("assignments = " + assignments);
//    }
}
