package com.pppppp.amadda.alarm.service;

import com.pppppp.amadda.alarm.dto.topic.TestValue;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmFriendAccept;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmFriendRequest;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmMentioned;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmScheduleAssigned;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmScheduleNotification;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmScheduleUpdate;
import com.pppppp.amadda.alarm.entity.Alarm;
import com.pppppp.amadda.alarm.entity.AlarmContent;
import com.pppppp.amadda.alarm.entity.AlarmType;
import com.pppppp.amadda.alarm.entity.KafkaTopic;
import com.pppppp.amadda.alarm.repository.AlarmRepository;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.UserErrorCode;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;

    @KafkaListener(topics = KafkaTopic.ALARM_FRIEND_REQUEST, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeFriendRequest(ConsumerRecord<String, AlarmFriendRequest> record)
        throws IOException {
        Long userSeq = Long.valueOf(String.valueOf(record.key()));
        String requestedUserName = record.value().getRequestedUserName();
        saveAlarm(userSeq, AlarmContent.FRIEND_REQUEST.getMessage(requestedUserName),
            AlarmType.FRIEND_REQUEST);
    }

    @KafkaListener(topics = KafkaTopic.ALARM_FRIEND_ACCEPT, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeFriendAccept(ConsumerRecord<String, AlarmFriendAccept> record)
        throws IOException {
        Long userSeq = Long.valueOf(String.valueOf(record.key()));
        String friendUserName = record.value().getFriendUserName();
        saveAlarm(userSeq, AlarmContent.FRIEND_ACCEPT.getMessage(friendUserName),
            AlarmType.FRIEND_ACCEPT);
    }

    @KafkaListener(topics = KafkaTopic.ALARM_SCHEDULE_ASSIGNED, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeScheduleAssigned(ConsumerRecord<String, AlarmScheduleAssigned> record)
        throws IOException {
        Long userSeq = Long.valueOf(String.valueOf(record.key()));
        String scheduleOwnerUserName = record.value().getScheduleOwnerUserName();
        String scheduleName = record.value().getScheduleName();
        saveAlarm(userSeq,
            AlarmContent.SCHEDULE_ASSIGNED.getMessage(scheduleOwnerUserName, scheduleName),
            AlarmType.SCHEDULE_ASSIGNED);
    }

    @KafkaListener(topics = KafkaTopic.ALARM_MENTIONED, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeMentioned(ConsumerRecord<String, AlarmMentioned> record) throws IOException {
    }

    @KafkaListener(topics = KafkaTopic.ALARM_SCHEDULE_UPDATE, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeScheduleUpdate(ConsumerRecord<String, AlarmScheduleUpdate> record)
        throws IOException {
        Long userSeq = Long.valueOf(String.valueOf(record.key()));
        String scheduleName = record.value().getScheduleName();
        saveAlarm(userSeq, AlarmContent.SCHEDULE_UPDATE.getMessage(scheduleName), AlarmType.SCHEDULE_UPDATE);
    }

    @KafkaListener(topics = KafkaTopic.ALARM_SCHEDULE_NOTIFICATION, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeScheduleNotification(
        ConsumerRecord<String, AlarmScheduleNotification> record)
        throws IOException {
    }

    @KafkaListener(topics = "test", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeTest(
        ConsumerRecord<String, TestValue> record)
        throws IOException {
        log.info("================================================");
        log.info("Kafka Consume Topic <<test>>");
        log.info("key : {}", record.key());
        log.info("value : {}", record.value().getValue());
        log.info("================================================");
    }

    public void saveAlarm(Long userSeq, String message, AlarmType alarmType) {
        User user = userRepository.findByUserSeq(userSeq)
            .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        Alarm alarm = Alarm.create(user, message, alarmType);
        alarmRepository.save(alarm);
    }

}
