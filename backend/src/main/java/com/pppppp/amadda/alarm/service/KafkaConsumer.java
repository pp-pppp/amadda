package com.pppppp.amadda.alarm.service;

import com.pppppp.amadda.alarm.dto.topic.TestValue;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmFriendAccept;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmFriendRequest;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmScheduleAssigned;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmScheduleNotification;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmScheduleUpdate;
import com.pppppp.amadda.alarm.entity.AlarmContent;
import com.pppppp.amadda.alarm.entity.AlarmType;
import com.pppppp.amadda.alarm.entity.FriendRequestAlarm;
import com.pppppp.amadda.alarm.entity.ScheduleAlarm;
import com.pppppp.amadda.alarm.repository.FriendRequestAlarmRepository;
import com.pppppp.amadda.alarm.repository.ScheduleAlarmRepository;
import com.pppppp.amadda.friend.entity.FriendRequest;
import com.pppppp.amadda.friend.repository.FriendRequestRepository;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.AlarmErrorCode;
import com.pppppp.amadda.global.entity.exception.errorcode.FriendRequestErrorCode;
import com.pppppp.amadda.global.entity.exception.errorcode.ScheduleErrorCode;
import com.pppppp.amadda.global.entity.exception.errorcode.UserErrorCode;
import com.pppppp.amadda.schedule.entity.Schedule;
import com.pppppp.amadda.schedule.repository.ScheduleRepository;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final UserRepository userRepository;
    private final ScheduleAlarmRepository scheduleAlarmRepository;
    private final FriendRequestAlarmRepository friendRequestAlarmRepository;
    private final ScheduleRepository scheduleRepository;
    private final FriendRequestRepository friendRequestRepository;

    @KafkaListener(topics = "${spring.kafka.topic.alarm.friend-request}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeFriendRequest(ConsumerRecord<String, AlarmFriendRequest> record) {
        Long userSeq = Long.valueOf(String.valueOf(record.key()));
        String requestedUserName = record.value().getRequestedUserName();
        Long friendRequestSeq = record.value().getFriendRequestSeq();
        saveFriendRequestAlarm(userSeq, AlarmContent.FRIEND_REQUEST.getMessage(requestedUserName),
            AlarmType.FRIEND_REQUEST, friendRequestSeq);
    }

    @KafkaListener(topics = "${spring.kafka.topic.alarm.friend-accept}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeFriendAccept(ConsumerRecord<String, AlarmFriendAccept> record) {
        Long userSeq = Long.valueOf(String.valueOf(record.key()));
        String friendUserName = record.value().getFriendUserName();
        Long friendUserSeq = record.value().getFriendUserSeq();
        User owner = getUser(userSeq);
        User friend = getUser(friendUserSeq);
        FriendRequest friendRequest = getFriendRequest(owner, friend);
        saveFriendRequestAlarm(userSeq, AlarmContent.FRIEND_ACCEPT.getMessage(friendUserName),
            AlarmType.FRIEND_ACCEPT, friendRequest.getRequestSeq());
    }

    private FriendRequest getFriendRequest(User owner, User friend) {
        return friendRequestRepository.findByOwnerAndFriend(owner, friend)
            .orElseThrow(
                () -> new RestApiException(FriendRequestErrorCode.FRIEND_REQUEST_NOT_FOUND));
    }

    @KafkaListener(topics = "${spring.kafka.topic.alarm.schedule-assigned}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeScheduleAssigned(ConsumerRecord<String, AlarmScheduleAssigned> record) {
        Long userSeq = Long.valueOf(String.valueOf(record.key()));
        String message = getScheduleAssignedMessage(record.value());
        Long scheduleSeq = record.value().getScheduleSeq();
        saveScheduleAlarm(userSeq, message, AlarmType.SCHEDULE_ASSIGNED, scheduleSeq);
    }

    @KafkaListener(topics = "${spring.kafka.topic.alarm.schedule-update}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeScheduleUpdate(ConsumerRecord<String, AlarmScheduleUpdate> record) {
        Long userSeq = Long.valueOf(String.valueOf(record.key()));
        String scheduleName = record.value().getScheduleName();
        Long scheduleSeq = record.value().getScheduleSeq();
        saveScheduleAlarm(userSeq, AlarmContent.SCHEDULE_UPDATE.getMessage(scheduleName),
            AlarmType.SCHEDULE_UPDATE, scheduleSeq);
    }

    @KafkaListener(topics = "${spring.kafka.topic.alarm.schedule-notification}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeScheduleNotification(
        ConsumerRecord<String, AlarmScheduleNotification> record) {
        Long userSeq = Long.valueOf(String.valueOf(record.key()));
        String message = getScheduleNotificationMessage(record.value());
        Long scheduleSeq = record.value().getScheduleSeq();
        saveScheduleAlarm(userSeq, message, AlarmType.SCHEDULE_NOTIFICATION, scheduleSeq);
    }

    @KafkaListener(topics = "test", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeTest(ConsumerRecord<String, TestValue> record) {
        log.info("================================================");
        log.info("Kafka Consume Topic <<test>>");
        log.info("key : {}", record.key());
        log.info("value : {}", record.value().getValue());
        log.info("================================================");
    }

    public void saveScheduleAlarm(Long userSeq, String message, AlarmType alarmType,
        Long scheduleSeq) {
        User user = getUser(userSeq);
        Schedule schedule = getSchedule(scheduleSeq);
        ScheduleAlarm alarm = ScheduleAlarm.create(user, message, alarmType, schedule);
        scheduleAlarmRepository.save(alarm);
    }

    private Schedule getSchedule(Long scheduleSeq) {
        return scheduleRepository.findById(scheduleSeq)
            .orElseThrow(() -> new RestApiException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
    }

    public void saveFriendRequestAlarm(Long userSeq, String message, AlarmType alarmType,
        Long friendRequestSeq) {
        User user = getUser(userSeq);
        FriendRequest friendRequest = getFriendRequest(friendRequestSeq);
        FriendRequestAlarm alarm = FriendRequestAlarm.create(user, message, alarmType,
            friendRequest);
        friendRequestAlarmRepository.save(alarm);
    }

    private FriendRequest getFriendRequest(Long friendRequestSeq) {
        return friendRequestRepository.findById(friendRequestSeq)
            .orElseThrow(
                () -> new RestApiException(FriendRequestErrorCode.FRIEND_REQUEST_NOT_FOUND));
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
