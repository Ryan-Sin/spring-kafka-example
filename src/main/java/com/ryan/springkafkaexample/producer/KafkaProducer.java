package com.ryan.springkafkaexample.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaTemplate<String, Object> exactlyOnceKafkaTemplate;

    /**
     * @author Ryan
     * @description 동기 전송
     */
    public void sendSynchronousMessage(String topic, String key, Object message) {
        try {
            kafkaTemplate.send(topic, key, message).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * @author Ryan
     * @description 비동기 전송
     */
    public void sendAsynchronousMessage(String topic, String key, Object message) {
        kafkaTemplate.send(topic, key, message);
    }

    /**
     * @author Ryan
     * @description 콜백 전송
     */
    public void sendMessageWithCallback(String topic, String key, Object value) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, value);
        future.thenAccept(result -> {
            RecordMetadata metadata = result.getRecordMetadata();
            log.info("Message sent to topic: {} partition: {} offset: {}", metadata.topic(), metadata.partition(), metadata.offset());
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
    }

    /**
     * @author Ryan
     * @description 정확히 한번 발송을 위한 트랜잭션 API 사용
     */
    public void exactlyOnceMessage(String topic, String key, Object value) {
        exactlyOnceKafkaTemplate.executeInTransaction(operations -> {
            try {
                operations.send(topic, key, value).get();
                log.info("Message sent successfully to topic: {}", topic);
            } catch (Exception e) {
                log.error("Error sending message: {}", e.getMessage());
            }
            return null;
        });
    }
}
