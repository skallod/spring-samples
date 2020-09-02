package ru.galuzin.consumer.batch;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.galuzin.consumer.batch.config.BatchConsumerConf;

public class BatchConsumerMain {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BatchConsumerConf.class);
    }
}
//-create -zookeeper zookeeper-service-sample:2181 --replication-factor 1 --partitions 10 --topic batch_topic  --config "cleanup.policy=compact"
//--broker-list localhost:9092 --topic batch_topic --property "parse.key=true" --property "key.separator=,"
