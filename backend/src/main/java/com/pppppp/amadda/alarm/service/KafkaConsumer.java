package com.pppppp.amadda.alarm.service;

import com.pppppp.amadda.alarm.dto.topic.BaseTopicValue;
import com.pppppp.amadda.alarm.entity.Alarm;
import com.pppppp.amadda.alarm.entity.KafkaTopic;
import com.pppppp.amadda.alarm.repository.AlarmRepository;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;

    // TODO: consume 받으면 save alarm

    @KafkaListener(topics = KafkaTopic.ALARM_FRIEND_REQUEST, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeFriendRequest(ConsumerRecord<String, BaseTopicValue> record) throws IOException {
        printRecoreInfo(record);
    }

    @KafkaListener(topics = KafkaTopic.ALARM_FRIEND_ACCEPT, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeFriendAccept(ConsumerRecord<String, BaseTopicValue> record) throws IOException {
        printRecoreInfo(record);
    }

    @KafkaListener(topics = KafkaTopic.ALARM_SCHEDULE_ASSIGNED, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeScheduleAssigned(ConsumerRecord<String, BaseTopicValue> record) throws IOException {
        printRecoreInfo(record);
    }

    @KafkaListener(topics = KafkaTopic.ALARM_MENTIONED, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeMentioned(ConsumerRecord<String, BaseTopicValue> record) throws IOException {
        printRecoreInfo(record);
    }

    @KafkaListener(topics = KafkaTopic.ALARM_SCHEDULE_UPDATE, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeScheduleUpdate(ConsumerRecord<String, BaseTopicValue> record) throws IOException {
        printRecoreInfo(record);
    }

    @KafkaListener(topics = KafkaTopic.ALARM_SCHEDULE_NOTIFICATION, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeScheduleNotification(ConsumerRecord<String, BaseTopicValue> record) throws IOException {
        printRecoreInfo(record);
    }


    private static void printRecoreInfo(ConsumerRecord<String, BaseTopicValue> record) {
        log.info("Kafka Consume ===============");
        log.info("topic : {}", record.topic());
        log.info("partition : {}", record.partition());
        log.info("offset : {}", record.offset());
        log.info("key : {}", record.key());
        log.info("value : {}", record.value());
    }

    public void saveAlarm(Long userSeq, String message) {
        User user = userRepository.findByUserSeq(userSeq)
            .orElseThrow(IllegalArgumentException::new); // TODO: exception custom
        Alarm alarm = Alarm.create(user, message);
        alarmRepository.save(alarm);
    }
}
