package com.pppppp.amadda.alarm.controller;

import com.pppppp.amadda.alarm.dto.topic.BaseTopicValue;
import com.pppppp.amadda.alarm.dto.topic.TestValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kafka")
@RequiredArgsConstructor
@Slf4j
public class KafkaTestController {

    private final KafkaTemplate<Long, BaseTopicValue> kafkaTemplate;

    @GetMapping("/send")
    public String readAlarms(@RequestParam Long key, @RequestParam String message) {
        String topic = "test";
        TestValue value = TestValue.builder().value(message).build();
        kafkaTemplate.send(topic, key, value);
        return String.format("topic : %s // key : %d // value : %s", topic, key, message);
    }

}
