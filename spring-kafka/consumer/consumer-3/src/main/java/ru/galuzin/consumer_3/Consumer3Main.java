package ru.galuzin.consumer_3;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.galuzin.dto.CompressType;
import ru.galuzin.utils.CompressHelper;
import ru.galuzin.utils.JsonConverter;
import ru.galuzin.dto.Loot;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Consumer3Main {

    public static void main(String[] args) {

        consumerContainerLoot();
    }

//    public void buildListenerContainer(final KafkaTopicBuilder topicBuilder,
//                                       final MessageListener<String, Change> messageListener) {
//        final ContainerProperties containerProperties = new ContainerProperties(topicBuilder.buildTopicName());
//
//        containerProperties.setMessageListener(messageListener);
//        containerProperties.setAckMode(AbstractMessageListenerContainer.AckMode.RECORD);
//
//        ConsumerFactory<String, Change> consumerFactory = new DefaultKafkaConsumerFactory<>(
//                consumerProperties(),
//                new StringDeserializer(),
//                new ErrorHandlingDeserializer<>(new JsonDeserializer<>(Change.class))
//        );
//        ConcurrentMessageListenerContainer<String, Change> listenerContainer = new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);
//        listenerContainer.setConcurrency(kafkaConfiguration.getConcurrency());
//
//        return listenerContainer;
//    }

    public static ConcurrentMessageListenerContainer<String, byte[]> consumerContainer() {
        final CompressHelper compressHelper = new CompressHelper();
        final ContainerProperties containerProperties = new ContainerProperties("test");
        containerProperties.setMessageListener(new MessageListener<String,byte[]>() {
            @Override
            public void onMessage(ConsumerRecord<String, byte[]> data) {
                System.out.println("data.value() = " + data.key()+" ; "+ data.value().length);
                //todo data.headers().headers();
                final byte[] bytes = compressHelper.gzipUncompress(data.value());
                System.out.println("bytes = " + bytes.length);//new String(bytes));
            }
        });
        containerProperties.setAckMode(AbstractMessageListenerContainer.AckMode.RECORD);

        ConsumerFactory<String, byte[]> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProperties());

        ConcurrentMessageListenerContainer<String, byte[]> listenerContainer = new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);
        listenerContainer.setConcurrency(2);

        listenerContainer.start();
        return listenerContainer;
    }

    public static Map<String, Object> consumerProperties() {
        Map<String, Object> consumer = new HashMap<>();
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, "test_galuzin_consumer");
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);//push every n ms
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1000);
        return consumer;
    }

    public static ConcurrentMessageListenerContainer<String, Loot> consumerContainerLoot() {
        final CompressHelper compressHelper = new CompressHelper();
        final JsonConverter<Loot> jsonConverter = new JsonConverter<>(Loot.class);
        final ContainerProperties containerProperties = new ContainerProperties("test_loot");
        containerProperties.setMessageListener(new MessageListener<String, Loot>() {
            public void onMessage(ConsumerRecord<String, Loot> data) {
                System.out.println("gal data incoming");
                Loot lootW = data.value();
                Loot lootIn = null;
                if (lootW.getCompressType() == CompressType.GZIP){
                    final byte[] bytes = compressHelper.gzipUncompress(lootW.getData());
                    try {
                        lootIn = jsonConverter.fromJson(bytes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("gal data.value() = " + data.key()+" ; "+ lootIn.getName());
            }
        });
        containerProperties.setAckMode(AbstractMessageListenerContainer.AckMode.RECORD);

        ConsumerFactory<String, Loot> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerPropertiesLoot(),
                new StringDeserializer(), new JsonDeserializer<>(Loot.class));

        ConcurrentMessageListenerContainer<String, Loot> listenerContainer =
                new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);
        listenerContainer.setConcurrency(2);

        listenerContainer.start();
        return listenerContainer;
    }

    public static Map<String, Object> consumerPropertiesLoot() {
        Map<String, Object> consumer = new HashMap<>();
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        //consumer.put(JsonDeserializer.TRUSTED_PACKAGES, "ru.vtb.smartreplication.api.dto");
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, "test_loot");
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);//push every n ms
        consumer.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1000);
        return consumer;
    }
}
