package com.pppppp.amadda.alarm.service;

import com.pppppp.amadda.alarm.dto.topic.BaseTopicValue;
import com.pppppp.amadda.alarm.dto.topic.setting.GlobalSettingValue;
import com.pppppp.amadda.alarm.entity.AlarmType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {
    /*
     * TODO: turn on/off 시 AlarmConfigRepository 수정
     * TODO: 유저 닉네임 등록 시 produce + AlarmConfigRepository
     * TODO: 일정 이름 변경 시 produce + AlarmConfigRepository
     * */

    private final KafkaTemplate<String, BaseTopicValue> kafkaTemplate;

    public void sendAlarm(String topic, String key, BaseTopicValue value) {
        log.info("Kafka Producer {} - {} : {}", topic, key, value);
        send(topic, key, value);
    }

    public void turnOnGlobalAlarm(AlarmType type, Long userSeq) {
        String topic = getSettingGlobalTopic(type);
        GlobalSettingValue value = GlobalSettingValue.builder().isOn(true).build();
        send(topic, String.valueOf(userSeq), value);
    }

    public void turnOffGlobalAlarm(AlarmType type, Long userSeq) {
        String topic = getSettingGlobalTopic(type);
        GlobalSettingValue value = GlobalSettingValue.builder().isOn(false).build();
        send(topic, String.valueOf(userSeq), value);
    }

    private static String getSettingGlobalTopic(AlarmType type) {
        return String.format("SETTING_GLOBAL_%s", type.getCode());
    }

    private void send(String topic, String key, BaseTopicValue value) {
        kafkaTemplate.send(topic, key, value);
    }

}
