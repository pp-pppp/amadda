package com.pppppp.amadda.alarm.service;

import com.pppppp.amadda.alarm.dto.request.AlarmConfigRequest;
import com.pppppp.amadda.alarm.dto.response.AlarmReadResponse;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmFriendAccept;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmFriendRequest;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmScheduleAssigned;
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
import com.pppppp.amadda.schedule.entity.Participation;
import com.pppppp.amadda.schedule.entity.Schedule;
import com.pppppp.amadda.schedule.repository.ParticipationRepository;
import com.pppppp.amadda.schedule.repository.ScheduleRepository;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final AlarmConfigRepository alarmConfigRepository;
    private final AlarmRepository alarmRepository;
    private final KafkaProducer kafkaProducer;

    public List<AlarmReadResponse> readAlarms(Long userSeq) {
        User user = getUser(userSeq);

        List<AlarmConfig> alarmConfigs = alarmConfigRepository.findAllByUser_UserSeqAndIsDeletedFalse(
            user.getUserSeq());
        Map<AlarmType, AlarmConfig> config = alarmConfigs.stream()
            .collect(Collectors.toMap(AlarmConfig::getAlarmType, ac -> ac));

        List<Alarm> alarms = alarmRepository.findAllByUserAndIsReadFalseAndIsDeletedFalse(user);
        return alarms.stream()
            .map(alarm -> {
                AlarmConfig alarmConfig = config.get(alarm.getAlarmType());
                if (alarmConfig == null) {
                    return AlarmReadResponse.of(alarm, true);
                }
                return AlarmReadResponse.of(alarm, alarmConfig.isEnabled());
            })
            .toList();
    }

    @Transactional
    public void readAlarm(Long alarmSeq, Long userSeq) {
        Alarm alarm = alarmRepository.findById(alarmSeq)
            .orElseThrow(() -> new RestApiException(AlarmErrorCode.ALARM_NOT_EXIST));

        User user = getUser(userSeq);
        if (!user.equals(alarm.getUser())) {
            throw new RestApiException(AlarmErrorCode.ALARM_FORBIDDEN);
        }

        alarm.markAsRead();
        alarmRepository.save(alarm);
    }

    @Transactional
    public AlarmConfig setGlobalAlarm(AlarmConfigRequest request, boolean isEnabled) {
        if (request.alarmType().equals(AlarmType.SCHEDULE_NOTIFICATION)) {
            throw new RestApiException(AlarmErrorCode.CANNOT_SET_GLOBAL_CONFIG);
        }

        User user = getUser(request.userSeq());

        Optional<AlarmConfig> config = alarmConfigRepository.findByUser_UserSeqAndAlarmTypeAndIsDeletedFalse(
            user.getUserSeq(), request.alarmType());

        config.ifPresent(alarmConfig -> alarmConfig.updateIsEnabled(isEnabled));
        if (config.isEmpty()) {
            config = Optional.of(AlarmConfig.create(user, request.alarmType(), isEnabled));
        }

        return alarmConfigRepository.save(config.get());
    }

    @Transactional
    public void sendFriendRequest(Long ownerSeq, Long friendSeq) {
        User friend = getUser(friendSeq);

        if (checkGlobalAlarmSetting(friend.getUserSeq(), AlarmType.FRIEND_REQUEST)) {
            User owner = getUser(ownerSeq);
            FriendRequest friendRequest = getFriendRequest(owner, friend);

            AlarmFriendRequest value = AlarmFriendRequest.create(friendRequest.getRequestSeq(),
                owner.getUserSeq(), owner.getUserName());

            kafkaProducer.sendAlarm(KafkaTopic.ALARM_FRIEND_REQUEST, friend.getUserSeq(), value);
        }
    }

    @Transactional
    public void sendFriendAccept(Long ownerSeq, Long friendSeq) {
        User owner = getUser(ownerSeq);

        if (checkGlobalAlarmSetting(owner.getUserSeq(), AlarmType.FRIEND_ACCEPT)) {
            User friend = getUser(friendSeq);

            AlarmFriendAccept value = AlarmFriendAccept.create(friend.getUserSeq(),
                friend.getUserName());

            kafkaProducer.sendAlarm(KafkaTopic.ALARM_FRIEND_ACCEPT, owner.getUserSeq(), value);
        }

    }

    @Transactional
    public void sendScheduleAssigned(Long scheduleSeq) {
        Schedule schedule = getSchedule(scheduleSeq);
        User owner = schedule.getAuthorizedUser();

        List<Participation> participations = participationRepository.findBySchedule_ScheduleSeqAndIsDeletedFalse(
            scheduleSeq);

        participations.stream()
            .filter(participation -> {
                User user = participation.getUser();
                return !owner.equals(user) && checkGlobalAlarmSetting(user.getUserSeq(),
                    AlarmType.SCHEDULE_ASSIGNED);
            })
            .forEach(participation -> {
                AlarmScheduleAssigned value = AlarmScheduleAssigned.create(
                    schedule.getScheduleSeq(), participation.getScheduleName(),
                    owner.getUserSeq(), owner.getUserName());
                kafkaProducer.sendAlarm(KafkaTopic.ALARM_SCHEDULE_ASSIGNED,
                    participation.getUser().getUserSeq(), value);
            });
    }

    @Transactional
    public void sendScheduleUpdate(Long scheduleSeq, Long userSeq) {
        Schedule schedule = getSchedule(scheduleSeq);
        User modifier = getUser(userSeq);

        List<Participation> participations = participationRepository.findBySchedule_ScheduleSeqAndIsDeletedFalse(
            scheduleSeq);

        participations.stream()
            .filter(participation -> {
                User user = participation.getUser();
                return !modifier.equals(user) &&
                    checkUpdateAlarmSetting(schedule.getScheduleSeq(), user.getUserSeq());
            })
            .forEach(participation -> {
                AlarmScheduleUpdate value = AlarmScheduleUpdate.create(schedule.getScheduleSeq(),
                    participation.getScheduleName());
                kafkaProducer.sendAlarm(KafkaTopic.ALARM_SCHEDULE_UPDATE,
                    participation.getUser().getUserSeq(), value);
            });
    }

    private User getUser(Long ownerSeq) {
        return userRepository.findByUserSeq(ownerSeq)
            .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
    }

    private Schedule getSchedule(Long scheduleSeq) {
        return scheduleRepository.findByScheduleSeqAndIsDeletedFalse(scheduleSeq)
            .orElseThrow(() -> new RestApiException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
    }

    private FriendRequest getFriendRequest(User owner, User friend) {
        return friendRequestRepository.findByOwnerAndFriend(owner, friend)
            .orElseThrow(() -> new RestApiException(
                FriendRequestErrorCode.FRIEND_REQUEST_NOT_FOUND));
    }

    public boolean checkUpdateAlarmSetting(Long scheduleSeq, Long userSeq) {
        return checkGlobalAlarmSetting(userSeq, AlarmType.SCHEDULE_UPDATE)
            && checkLocalUpdateAlarmSetting(scheduleSeq, userSeq);
    }

    public boolean checkGlobalAlarmSetting(Long userSeq, AlarmType alarmType) {
        return alarmConfigRepository
            .findByUser_UserSeqAndAlarmTypeAndIsEnabledFalseAndIsDeletedFalse(userSeq, alarmType)
            .map(AlarmConfig::isEnabled)
            .orElse(true);
    }

    public boolean checkLocalUpdateAlarmSetting(Long scheduleSeq, Long userSeq) {
        return participationRepository
            .findBySchedule_ScheduleSeqAndUser_UserSeqAndIsDeletedFalse(scheduleSeq, userSeq)
            .map(Participation::isUpdateAlarmOn)
            .orElse(true);
    }
}
