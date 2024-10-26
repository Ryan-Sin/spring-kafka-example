package com.ryan.springkafkaexample.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.producer.enable-idempotence_config}")
    private String enableIdempotenceConfig;

    @Value("${spring.kafka.producer.acks-config}")
    private String acksConfig;

    @Value("${spring.kafka.producer.max-in-flight-requests-per-connection}")
    private String maxInFlightRequestsPerConnection;

    @Value("${spring.kafka.producer.retries-config}")
    private String retriesConfig;


    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, Object> exactlyOnceProducerFactory() {
        Map<String, Object> exactlyOnceConfigProps = new HashMap<>();
        exactlyOnceConfigProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        exactlyOnceConfigProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        exactlyOnceConfigProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        exactlyOnceConfigProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, enableIdempotenceConfig);
        exactlyOnceConfigProps.put(ProducerConfig.ACKS_CONFIG, acksConfig);
        exactlyOnceConfigProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, maxInFlightRequestsPerConnection);
        exactlyOnceConfigProps.put(ProducerConfig.RETRIES_CONFIG, retriesConfig);
        exactlyOnceConfigProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "peter-transaction-01");

        return new DefaultKafkaProducerFactory<>(exactlyOnceConfigProps);
    }

    @Bean
    public KafkaTemplate<String, Object>  exactlyOnceKafkaTemplate() {
        return new KafkaTemplate<>(exactlyOnceProducerFactory());
    }
}
