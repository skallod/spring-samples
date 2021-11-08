package ru.galuzin.producer_3;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.galuzin.dto.CompressInfo;
import ru.galuzin.dto.CompressType;
import ru.galuzin.dto.JsonConverter;
import ru.galuzin.dto.Loot;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootApplication
public class Producer3_2Main {

    static final Logger log = LoggerFactory.getLogger(Producer3_2Main.class);
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Producer3_2Main.class);
        sendLoot();
    }

    private static void sendLoot() throws Exception {
        final Producer<String, byte[]> producer = createChangeProducer();
//        for (int i = 6 ; i < 15 ; i ++) {
        final Loot lootW = new Loot(null);
        Loot lootInn = new Loot("bullet " + UUID.randomUUID().toString());
        final JsonConverter jsonConverter = new JsonConverter();
        final byte[] bytes = jsonConverter.toJson(lootInn);
        final long time = System.currentTimeMillis();
        final byte[] compress = Helper.snappyCompress(bytes);
        log.info("compress timing {}", System.currentTimeMillis() - time);
        lootW.setData(compress);
        lootW.setCompressType(CompressType.SNAPPY);
        final CompressInfo compressInfo = new CompressInfo();
        compressInfo.setData("hey".getBytes(StandardCharsets.UTF_8));
        final ProducerRecord<String, byte[]> producerRecord = new ProducerRecord<String, byte[]>("test_loot",
                UUID.randomUUID().toString(), jsonConverter.toJson(lootW));
        final RecordMetadata metadata = producer.send(producerRecord).get();
//        }
    }

//    private static void sendCompressed() throws IOException, URISyntaxException, InterruptedException, java.util.concurrent.ExecutionException {
//        final byte[] bytes = Helper.getBeforeBytes();
//        final byte[] compress = Helper.gzipCompress(bytes);
////        byte[] compress = "hello".getBytes(StandardCharsets.UTF_8);
////        final RecordHeaders headers = new RecordHeaders();
////        headers.add(new RecordHeader("COMPRESS_TYPE", "gzip".getBytes(StandardCharsets.UTF_8)));
//        final ProducerRecord<String, byte[]> producerRecord = new ProducerRecord<String, byte[]>("test",
//                UUID.randomUUID().toString(), compress);
////        final ProducerRecord<String, byte[]> producerRecord = new ProducerRecord<String, byte[]>("test_loot",
////                UUID.randomUUID().toString(), compress);
////        producerRecord.headers().add(new RecordHeader("COMPRESS_TYPE", "gzip".getBytes(StandardCharsets.UTF_8)));
//        final RecordMetadata metadata = createChangeProducer().send(producerRecord).get();
//    }

    public static Producer<String, byte[]> createChangeProducer() {
        final Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        return new KafkaProducer<>(properties);
    }

    public static Producer<String, Loot> createChangeProducerLoot() {
        final Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new KafkaProducer<>(properties);
    }
}
