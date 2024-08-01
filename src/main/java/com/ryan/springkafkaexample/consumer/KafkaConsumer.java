package com.ryan.springkafkaexample.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryan.springkafkaexample.consumer.dto.ConsumerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ObjectMapper objectMapper;

    public List<ConsumerDto.RootConsumerDto> list = new ArrayList<>();

    @KafkaListener(topics = "ryan", groupId = "ryan")
    public void consume(ConsumerRecord<String, String> record) throws Exception {
        String message = record.value();
        var result = objectMapper.readValue(message, ConsumerDto.RootConsumerDto.class);

        System.out.println("컨슈머 결과 = " + result);
        this.list.add(result);
    }
}
