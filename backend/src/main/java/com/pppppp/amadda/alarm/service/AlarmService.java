package com.pppppp.amadda.alarm.service;

import com.pppppp.amadda.alarm.dto.response.AlarmReadResponse;
import com.pppppp.amadda.alarm.dto.topic.BaseTopicValue;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmFriendAccept;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmFriendRequest;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmScheduleAssigned;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmScheduleNotification;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmScheduleUpdate;
import com.pppppp.amadda.alarm.entity.Alarm;
import com.pppppp.amadda.alarm.entity.AlarmConfig;
import com.pppppp.amadda.alarm.entity.AlarmType;
import com.pppppp.amadda.alarm.entity.KafkaTopic;
import com.pppppp.amadda.alarm.repository.AlarmConfigRepository;
import com.pppppp.amadda.alarm.repository.AlarmRepository;
import com.pppppp.amadda.friend.entity.FriendRequest;
import com.pppppp.amadda.friend.repository.FriendRequestRepository;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.AlarmErrorCode;
import com.pppppp.amadda.global.entity.exception.errorcode.FriendRequestErrorCode;
import com.pppppp.amadda.global.entity.exception.errorcode.ScheduleErrorCode;
import com.pppppp.amadda.global.entity.exception.errorcode.UserErrorCode;
import com.pppppp.amadda.schedule.entity.AlarmTime;
import com.pppppp.amadda.schedule.entity.Participation;
import com.pppppp.amadda.schedule.entity.Schedule;
import com.pppppp.amadda.schedule.repository.ParticipationRepository;
import com.pppppp.amadda.schedule.repository.ScheduleRepository;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AlarmService {

    private final UserRepository userRepository;

    private final FriendRequestRepository friendRequestRepository;

    private final ScheduleRepository scheduleRepository;
    private final ParticipationRepository participationRepository;

    private final AlarmRepository alarmRepository;
    private final AlarmConfigRepository alarmConfigRepository;

    private final KafkaProducer kafkaProducer;
    private final KafkaTopic kafkaTopic;

    /* 알림 목록 가져오기 */

    public List<AlarmReadResponse> getAlarms(Long userSeq) {
        User user = getUser(userSeq);
        Map<AlarmType, AlarmConfig> config = createAlarmConfigMap(userSeq);
        List<Alarm> alarms = alarmRepository.findAllByUserAndIsReadFalse(user);
        return alarms.stream()
            .map(alarm -> createAlarmReadResponse(config, alarm))
            .toList();
    }

    /* 알림 읽기 */

    @Transactional
    public void readAlarm(Long alarmSeq, Long userSeq) {
        Alarm alarm = getAlarm(alarmSeq);
        User user = getUser(userSeq);
        validateUser(alarm, user);
        markAlarmAsRead(alarm);
    }

    @Transactional
    public void readFriendRequestAlarm(Long friendRequestSeq) {
        FriendRequest friendRequest = getFriendRequestBySeq(friendRequestSeq);
        User owner = friendRequest.getOwner();
        User friend = friendRequest.getFriend();

        Alarm alarm = alarmRepository.findFriendRequestAlarm(friend, AlarmType.FRIEND_REQUEST,
                owner.getUserName())
            .orElseThrow(() -> new RestApiException(AlarmErrorCode.ALARM_NOT_EXIST));
        markAlarmAsRead(alarm);
    }

    /* 알림 전송 */

    @Transactional
    public AlarmConfig setGlobalAlarm(Long userSeq, String type, boolean isEnabled) {
        AlarmType alarmType = AlarmType.valueOf(type);
        validateGlobalAlarm(alarmType);
        User user = getUser(userSeq);
        AlarmConfig alarmConfig = updateOrCreateAlarmConfig(alarmType, isEnabled, user);
        return alarmConfigRepository.save(alarmConfig);
    }

    public void sendFriendRequest(Long ownerSeq, Long targetSeq) {
        User target = getUser(targetSeq);
        if (checkGlobalAlarmSetting(target.getUserSeq(), AlarmType.FRIEND_REQUEST)) {
            User owner = getUser(ownerSeq);
            FriendRequest friendRequest = getFriendRequest(owner, target);

            AlarmFriendRequest value = AlarmFriendRequest.create(friendRequest.getRequestSeq(),
                ownerSeq, owner.getUserName(), friendRequest.getRequestSeq());
            kafkaProducer.sendAlarm(kafkaTopic.ALARM_FRIEND_REQUEST, targetSeq, value);
        }
    }

    public void sendFriendAccept(Long ownerSeq, Long targetSeq) {
        User owner = getUser(ownerSeq);
        if (checkGlobalAlarmSetting(owner.getUserSeq(), AlarmType.FRIEND_ACCEPT)) {
            User target = getUser(targetSeq);

            AlarmFriendAccept value = AlarmFriendAccept.create(targetSeq, target.getUserName(),
                null);
            kafkaProducer.sendAlarm(kafkaTopic.ALARM_FRIEND_ACCEPT, ownerSeq, value);
        }

    }

    public void sendScheduleAssigned(Long scheduleSeq, Long creatorSeq, Long userSeq) {
        User user = getUser(userSeq);
        Schedule schedule = getSchedule(scheduleSeq);

        sendReloadSchedule(scheduleSeq, userSeq);
        if (checkGlobalAlarmSetting(userSeq, AlarmType.SCHEDULE_ASSIGNED)) {
            User creator = getUser(creatorSeq);
            Participation participation = getParticipation(scheduleSeq, userSeq);

            AlarmScheduleAssigned value = AlarmScheduleAssigned.create(
                scheduleSeq, participation.getScheduleName(),
                creatorSeq, creator.getUserName(), scheduleSeq);
            kafkaProducer.sendAlarm(kafkaTopic.ALARM_SCHEDULE_ASSIGNED, userSeq, value);
        }
    }

    public void sendScheduleUpdate(Long scheduleSeq, Long userSeq) {
        User user = getUser(userSeq);
        Schedule schedule = getSchedule(scheduleSeq);

        sendReloadSchedule(scheduleSeq, userSeq);
        if (checkUpdateAlarmSetting(scheduleSeq, userSeq)) {
            Participation participation = getParticipation(scheduleSeq, userSeq);

            AlarmScheduleUpdate value = AlarmScheduleUpdate.create(scheduleSeq,
                participation.getScheduleName(), scheduleSeq);
            kafkaProducer.sendAlarm(kafkaTopic.ALARM_SCHEDULE_UPDATE, userSeq, value);
        }
    }

    private void sendReloadSchedule(Long scheduleSeq, Long userSeq) {
        BaseTopicValue baseTopicValue = new BaseTopicValue(scheduleSeq);
        kafkaProducer.sendAlarm(kafkaTopic.RELOAD_SCHEDULE, userSeq, baseTopicValue);
    }

    @Scheduled(cron = "${schedules.cron.schedule-notification}", zone = "Asia/Seoul")
    public void sendScheduleNotification() {
        LocalDateTime now = printLog();
        List<Participation> participations = getParticipations(now);
        participations.forEach(participation -> {
            Long userSeq = participation.getUser().getUserSeq();
            AlarmScheduleNotification value = getAlarmScheduleNotificationMessage(participation);
            kafkaProducer.sendAlarm(kafkaTopic.ALARM_SCHEDULE_NOTIFICATION, userSeq, value);
        });
    }

    private LocalDateTime printLog() {
        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        LocalDateTime now = LocalDateTime.now(zoneId);
        log.info("schedule tasks using cron jobs - {}", now);
        return now;
    }

    private List<Participation> getParticipations(LocalDateTime now) {
        LocalDateTime start = now.minusMinutes(5);
        LocalDateTime end = now.plusMinutes(5);
        return participationRepository
            .findAllByIsAlarmedFalseAndAlarmTimeNotAndAlarmAtBetween(AlarmTime.NONE, start, end);
    }

    private AlarmScheduleNotification getAlarmScheduleNotificationMessage(
        Participation participation) {
        Long scheduleSeq = participation.getSchedule().getScheduleSeq();
        String scheduleName = participation.getScheduleName();
        AlarmTime alarmTime = participation.getAlarmTime();
        LocalDateTime alarmDateTime = participation.getAlarmAt();
        return AlarmScheduleNotification.create(scheduleSeq, scheduleName, alarmTime,
            alarmDateTime, scheduleSeq);
    }

    /* 클래스 내부에서 사용하는 메소드 */

    private Map<AlarmType, AlarmConfig> createAlarmConfigMap(Long userSeq) {
        List<AlarmConfig> alarmConfigs = alarmConfigRepository
            .findAllByUser_UserSeq(userSeq);
        return alarmConfigs.stream()
            .collect(Collectors.toMap(AlarmConfig::getAlarmType, ac -> ac));
    }

    private AlarmReadResponse createAlarmReadResponse(Map<AlarmType, AlarmConfig> config,
        Alarm alarm) {
        AlarmConfig alarmConfig = config.getOrDefault(alarm.getAlarmType(), AlarmConfig.DEFAULT);
        return AlarmReadResponse.of(alarm, alarmConfig.isEnabled());
    }

    private void validateUser(Alarm alarm, User user) {
        if (!Objects.equals(alarm.getUser(), user)) {
            throw new RestApiException(AlarmErrorCode.ALARM_FORBIDDEN);
        }
    }

    private void markAlarmAsRead(Alarm alarm) {
        alarm.markAsRead();
        alarmRepository.save(alarm);
    }

    private void validateGlobalAlarm(AlarmType alarmType) {
        if (alarmType.equals(AlarmType.SCHEDULE_NOTIFICATION)) {
            throw new RestApiException(AlarmErrorCode.CANNOT_SET_GLOBAL_CONFIG);
        }
    }

    private AlarmConfig updateOrCreateAlarmConfig(AlarmType alarmType, boolean isEnabled,
        User user) {
        Optional<AlarmConfig> config = alarmConfigRepository
            .findByUser_UserSeqAndAlarmType(user.getUserSeq(), alarmType);
        config.ifPresent(alarmConfig -> alarmConfig.updateIsEnabled(isEnabled));
        return config.orElseGet(() -> AlarmConfig.create(user, alarmType, isEnabled));
    }

    public boolean checkGlobalAlarmSetting(Long userSeq, AlarmType alarmType) {
        return alarmConfigRepository
            .findByUser_UserSeqAndAlarmTypeAndIsEnabledFalse(userSeq, alarmType)
            .map(AlarmConfig::isEnabled)
            .orElse(true);
    }

    private boolean checkUpdateAlarmSetting(Long scheduleSeq, Long userSeq) {
        return checkGlobalAlarmSetting(userSeq, AlarmType.SCHEDULE_UPDATE)
            && checkLocalUpdateAlarmSetting(scheduleSeq, userSeq);
    }

    private boolean checkLocalUpdateAlarmSetting(Long scheduleSeq, Long userSeq) {
        return participationRepository
            .findBySchedule_ScheduleSeqAndUser_UserSeq(scheduleSeq, userSeq)
            .map(Participation::isUpdateAlarmOn)
            .orElse(true);
    }

    private User getUser(Long userSeq) {
        return userRepository.findById(userSeq)
            .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
    }

    private FriendRequest getFriendRequestBySeq(Long requestSeq) {
        return friendRequestRepository.findById(requestSeq)
            .orElseThrow(() -> new RestApiException(
                FriendRequestErrorCode.FRIEND_REQUEST_NOT_FOUND));
    }

    private FriendRequest getFriendRequest(User owner, User friend) {
        return friendRequestRepository.findByOwnerAndFriend(owner, friend)
            .orElseThrow(() -> new RestApiException(
                FriendRequestErrorCode.FRIEND_REQUEST_NOT_FOUND));
    }

    private Schedule getSchedule(Long scheduleSeq) {
        return scheduleRepository.findById(scheduleSeq)
            .orElseThrow(() -> new RestApiException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
    }

    private Participation getParticipation(Long scheduleSeq, Long userSeq) {
        return participationRepository.findBySchedule_ScheduleSeqAndUser_UserSeq(
                scheduleSeq, userSeq)
            .orElseThrow(() -> new RestApiException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
    }

    private Alarm getAlarm(Long alarmSeq) {
        return alarmRepository.findById(alarmSeq)
            .orElseThrow(() -> new RestApiException(AlarmErrorCode.ALARM_NOT_EXIST));
    }
}
