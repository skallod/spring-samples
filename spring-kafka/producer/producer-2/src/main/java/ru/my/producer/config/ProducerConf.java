package ru.my.producer.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class ProducerConf {

    @Value("${my.producer.bootstrapServers}")
    String bootstrapServers;

    @Autowired
    SslConfigProperties sslConfigProperties;

    @Bean
    public ProducerFactory<String, String> createProducerFactoryString() {
        final Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.putAll(sslConfigProperties.toMap());
        return new DefaultKafkaProducerFactory<>(properties);//, new StringSerializer(), new StringSerializer()
    }

    @Bean
    public KafkaTemplate<String, String> templateString(){
        return new KafkaTemplate<>(createProducerFactoryString());
    }
}
