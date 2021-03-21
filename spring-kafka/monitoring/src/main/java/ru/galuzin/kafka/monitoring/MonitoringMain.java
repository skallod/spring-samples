package ru.galuzin.kafka.monitoring;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import ru.galuzin.kafka.monitoring.service.LagService;

import java.util.Collection;
import java.util.Properties;

@SpringBootApplication(exclude = {KafkaAutoConfiguration.class})
public class MonitoringMain {

    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(MonitoringMain.class);
        final LagService lagService = context.getBean(LagService.class);
        while (true) {
            lagService.getLag2();
            try {
                Thread.sleep(5_000);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
    }

    public void nativeConsumerConnectToEmptyTopic() {

    }
        //цикл обработки
//        try {
//            while (true) {
//                ConsumerRecords<String, String> records = consumer.poll(100);
//                for (ConsumerRecord<String, String> record : records)
//                {
//                    log.debug("topic = %s, partition = %d, offset = %d,
//                            customer = %s, country = %s\n",
//                    record.topic(), record.partition(), record.offset(),
//                            record.key(), record.value());
//                    int updatedCount = 1;
//                    if (custCountryMap.countainsValue(record.value())) {
//                        updatedCount = custCountryMap.get(record.value()) + 1;
//                    }
//                    custCountryMap.put(record.value(), updatedCount)
//                    JSONObject json = new JSONObject(custCountryMap);
//                    System.out.println(json.toString(4));
//                }
//            }
//        } finally {
//            consumer.close();
//        }

//        private Map<TopicPartition, OffsetAndMetadata> currentOffsets =
//                new HashMap<>();
//        int count = 0;
//        //фиксация заданного смещения
//        while (true) {
//            ConsumerRecords<String, String> records = consumer.poll(100);
//            for (ConsumerRecord<String, String> record : records)
//            {
//                System.out.printf("topic = %s, partition = %s, offset = %d,
//                        customer = %s, country = %s\n",
//                record.topic(), record.partition(), record.offset(),
//                        record.key(), record.value());
//                currentOffsets.put(new TopicPartition(record.topic(),
//                        record.partition()), new
//                        OffsetAndMetadata(record.offset()+1, "no metadata"));
//                if (count % 1000 == 0)
//                    consumer.commitAsync(currentOffsets, null);
//                count++;
//            }
//        }

//        public class SaveOffsetsOnRebalance implements
//                ConsumerRebalanceListener {
//            public void onPartitionsRevoked(Collection<TopicPartition>
//                                                    partitions) {
//                commitDBTransaction();
//            }
//            public void onPartitionsAssigned(Collection<TopicPartition>
//                                                     partitions) {
//                for(TopicPartition partition: partitions)
//                    consumer.seek(partition, getOffsetFromDB(partition));
//            }
//        }
//    }
//consumer.subscribe(topics, new SaveOffsetOnRebalance(consumer));
//consumer.poll(0);
//for (TopicPartition partition: consumer.assignment())
//            consumer.seek(partition, getOffsetFromDB(partition));
//while (true) {
//        ConsumerRecords<String, String> records =
//                consumer.poll(100);
//        for (ConsumerRecord<String, String> record : records)
//        {
//            processRecord(record);
//            storeRecordInDB(record);
//            storeOffsetInDB(record.topic(), record.partition(),
//                    record.offset());
//        }
//        commitDBTransaction();
//    }

    // выход из цикла
//    Runtime.getRuntime().addShutdownHook(new Thread() {
//        public void run() {
//            System.out.println("Starting exit...");
//            consumer.wakeup();
//            try {
//                mainThread.join();
//            } catch (InterruptedException e) {
//
//                Десериализаторы 113
//
//                e.printStackTrace();
//            }
//        }
//    });
//...
//        try {
//// Выполняем цикл вплоть до нажатия ctrl+c, об очистке
//// при завершении выполнения позаботится ShutdownHook
//        while (true) {
//            ConsumerRecords<String, String> records =
//                    movingAvg.consumer.poll(1000);
//            System.out.println(System.currentTimeMillis() + "
//                    -- waiting for data...");
//            for (ConsumerRecord<String, String> record :
//                    records) {
//                System.out.printf("offset = %d, key = %s,
//                        value = %s\n",
//                record.offset(), record.key(),
//                        record.value());
//            }
//            for (TopicPartition tp: consumer.assignment())
//                System.out.println("Committing offset at
//                        position:" +
//            consumer.position(tp));
//            movingAvg.consumer.commitSync();
//        }
//    } catch (WakeupException e) {
//// Игнорируем
//    } finally {
//        consumer.close();
//        System.out.println("Closed consumer and we are done");
//    }
//}

}
