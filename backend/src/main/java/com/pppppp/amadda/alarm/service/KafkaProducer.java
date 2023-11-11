package com.pppppp.amadda.alarm.service;

import com.pppppp.amadda.alarm.dto.topic.BaseTopicValue;
import com.pppppp.amadda.alarm.dto.topic.setting.GlobalSettingValue;
import com.pppppp.amadda.alarm.entity.AlarmType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<Long, BaseTopicValue> kafkaTemplate;

    public void sendAlarm(String topic, Long key, BaseTopicValue value) {
        log.info("Kafka Producer {} - {} : {}", topic, key, value);
        send(topic, key, value);
    }

    public void turnOnGlobalAlarm(AlarmType type, Long userSeq) {
        String topic = getSettingGlobalTopic(type);
        GlobalSettingValue value = GlobalSettingValue.builder().isOn(true).build();
        send(topic, userSeq, value);
    }

    public void turnOffGlobalAlarm(AlarmType type, Long userSeq) {
        String topic = getSettingGlobalTopic(type);
        GlobalSettingValue value = GlobalSettingValue.builder().isOn(false).build();
        send(topic, userSeq, value);
    }

    private String getSettingGlobalTopic(AlarmType type) {
        return String.format("SETTING_GLOBAL_%s", type.getCode());
    }

    private void send(String topic, Long key, BaseTopicValue value) {
        kafkaTemplate.send(topic, key, value);
    }

}
