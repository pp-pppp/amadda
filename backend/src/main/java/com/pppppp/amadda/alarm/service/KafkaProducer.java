package com.pppppp.amadda.alarm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendAlarm(String topic, String key, String value) {
        log.info("Kafka Producer {} - {} : {}", topic, key, value);
        send(topic, key, value);
    }

    private void send(String topic, String key, String value) {
        kafkaTemplate.send(topic, key, value);
    }

}
