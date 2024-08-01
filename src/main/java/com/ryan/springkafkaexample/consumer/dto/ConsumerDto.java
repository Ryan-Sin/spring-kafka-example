package com.ryan.springkafkaexample.consumer.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

public class ConsumerDto {

    @Builder
    @Getter
    @ToString
    public static class RootConsumerDto {
        private String uuid;
        private String sendDomain;
        private String receiveDomain;
        private String targetTopic;
        private Instant createdAt;
        private Object data;
    }

}
