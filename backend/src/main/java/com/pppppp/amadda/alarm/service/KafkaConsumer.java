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
import com.pppppp.amadda.alarm.repository.AlarmRepository;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.AlarmErrorCode;
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

    @KafkaListener(topics = "${spring.kafka.topic.alarm.friend-request}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeFriendRequest(ConsumerRecord<String, AlarmFriendRequest> record)
        throws IOException {
        Long userSeq = Long.valueOf(String.valueOf(record.key()));
        String requestedUserName = record.value().getRequestedUserName();
        saveAlarm(userSeq, AlarmContent.FRIEND_REQUEST.getMessage(requestedUserName),
            AlarmType.FRIEND_REQUEST);
    }

    @KafkaListener(topics = "${spring.kafka.topic.alarm.friend-accept}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeFriendAccept(ConsumerRecord<String, AlarmFriendAccept> record)
        throws IOException {
        Long userSeq = Long.valueOf(String.valueOf(record.key()));
        String friendUserName = record.value().getFriendUserName();
        saveAlarm(userSeq, AlarmContent.FRIEND_ACCEPT.getMessage(friendUserName),
            AlarmType.FRIEND_ACCEPT);
    }

    @KafkaListener(topics = "${spring.kafka.topic.alarm.schedule-assigned}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeScheduleAssigned(ConsumerRecord<String, AlarmScheduleAssigned> record)
        throws IOException {
        Long userSeq = Long.valueOf(String.valueOf(record.key()));
        String message = getScheduleAssignedMessage(record.value());
        saveAlarm(userSeq, message, AlarmType.SCHEDULE_ASSIGNED);
    }

    @KafkaListener(topics = "${spring.kafka.topic.alarm.mentioned}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeMentioned(ConsumerRecord<String, AlarmMentioned> record) throws IOException {
    }

    @KafkaListener(topics = "${spring.kafka.topic.alarm.schedule-update}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeScheduleUpdate(ConsumerRecord<String, AlarmScheduleUpdate> record)
        throws IOException {
        Long userSeq = Long.valueOf(String.valueOf(record.key()));
        String scheduleName = record.value().getScheduleName();
        saveAlarm(userSeq, AlarmContent.SCHEDULE_UPDATE.getMessage(scheduleName),
            AlarmType.SCHEDULE_UPDATE);
    }

    @KafkaListener(topics = "${spring.kafka.topic.alarm.schedule-notification}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeScheduleNotification(
        ConsumerRecord<String, AlarmScheduleNotification> record) throws IOException {
        Long userSeq = Long.valueOf(String.valueOf(record.key()));
        String message = getScheduleNotificationMessage(record.value());
        saveAlarm(userSeq, message, AlarmType.SCHEDULE_NOTIFICATION);
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
        User user = getUser(userSeq);
        Alarm alarm = Alarm.create(user, message, alarmType);
        alarmRepository.save(alarm);
    }

    private String getScheduleAssignedMessage(AlarmScheduleAssigned value) {
        String scheduleOwnerUserName = value.getScheduleOwnerUserName();
        String scheduleName = value.getScheduleName();
        return AlarmContent.SCHEDULE_ASSIGNED.getMessage(scheduleOwnerUserName, scheduleName);
    }

    private String getScheduleNotificationMessage(AlarmScheduleNotification value) {
        String scheduleName = value.getScheduleName();
        int minute = value.getAlarmTime().getMinute();
        String time = minuteToString(minute);
        return AlarmContent.SCHEDULE_NOTIFICATION.getMessage(scheduleName, time);
    }

    private String minuteToString(int minute) {
        if (minute == 1440) {
            return "하루 뒤에";
        }
        if (minute == 60) {
            return "한 시간 뒤에";
        }
        if (minute == 30 || minute == 15) {
            return String.format("%d분 뒤에", minute);
        }
        if (minute == 0) {
            return "곧";
        }
        throw new RestApiException(AlarmErrorCode.ALARM_NOT_EXIST);
    }

    private User getUser(Long userSeq) {
        return userRepository.findById(userSeq)
            .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
    }

}
