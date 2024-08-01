package com.ryan.springkafkaexample.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ryan.springkafkaexample.consumer.KafkaConsumer;
import com.ryan.springkafkaexample.consumer.dto.ConsumerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 3,
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9092"
        },
        ports = {9092}
)
class KafkaProducerTest {

    @Autowired
    private KafkaProducer producer;

    @Autowired
    private KafkaConsumer consumer;

    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void 동기식_메시지_전송_검증() throws JsonProcessingException, InterruptedException {
        // given(준비): 어떠한 데이터가 준비되었을 때
        String topic = "ryan";
        String key = null;
        ConsumerDto.RootConsumerDto message = ConsumerDto.RootConsumerDto.builder()
                .uuid("123e4567-e89b-12d3-a456-426614174000")
                .sendDomain("sendDomain.com")
                .receiveDomain("receiveDomain.com")
                .targetTopic("target-topic")
                .createdAt(Instant.now())
                .data("Sample Data")
                .build();

        String messageAsString = objectMapper.writeValueAsString(message);

        // when(실행): 어떠한 함수를 실행하면
        this.producer.sendSynchronousMessage(topic, key, messageAsString);
        Thread.sleep(2000);

        // then(검증): 어떠한 결과가 나와야 한다.
        assertThat(this.consumer.list).hasSize(1);
    }

    @Test
    public void 비동기식_메시지_전송_검증() throws JsonProcessingException, InterruptedException {
        // given(준비): 어떠한 데이터가 준비되었을 때
        String topic = "ryan";
        String key = null;
        ConsumerDto.RootConsumerDto message = ConsumerDto.RootConsumerDto.builder()
                .uuid("123e4567-e89b-12d3-a456-426614174000")
                .sendDomain("sendDomain.com")
                .receiveDomain("receiveDomain.com")
                .targetTopic("target-topic")
                .createdAt(Instant.now())
                .data("Sample Data")
                .build();

        String messageAsString = objectMapper.writeValueAsString(message);

        // when(실행): 어떠한 함수를 실행하면
        this.producer.sendAsynchronousMessage(topic, key, messageAsString);
        Thread.sleep(2000);

        // then(검증): 어떠한 결과가 나와야 한다.
        assertThat(this.consumer.list).hasSize(1);
    }

    @Test
    public void 콜백_메시지_전송_검증() throws JsonProcessingException, InterruptedException {
        // given(준비): 어떠한 데이터가 준비되었을 때
        String topic = "ryan";
        String key = null;
        ConsumerDto.RootConsumerDto message = ConsumerDto.RootConsumerDto.builder()
                .uuid("123e4567-e89b-12d3-a456-426614174000")
                .sendDomain("sendDomain.com")
                .receiveDomain("receiveDomain.com")
                .targetTopic("target-topic")
                .createdAt(Instant.now())
                .data("Sample Data")
                .build();

        String messageAsString = objectMapper.writeValueAsString(message);

        // when(실행): 어떠한 함수를 실행하면
        this.producer.sendMessageWithCallback(topic, key, messageAsString);
        Thread.sleep(2000);

        // then(검증): 어떠한 결과가 나와야 한다.
        assertThat(this.consumer.list).hasSize(1);
    }
}