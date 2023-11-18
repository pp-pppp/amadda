package com.pppppp.amadda.alarm.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmFriendAccept;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmFriendRequest;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmScheduleAssigned;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmScheduleNotification;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmScheduleUpdate;
import com.pppppp.amadda.alarm.entity.KafkaTopic;
import com.pppppp.amadda.alarm.repository.FriendRequestAlarmRepository;
import com.pppppp.amadda.alarm.repository.ScheduleAlarmRepository;
import com.pppppp.amadda.friend.entity.FriendRequest;
import com.pppppp.amadda.friend.repository.FriendRequestRepository;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.schedule.entity.AlarmTime;
import com.pppppp.amadda.schedule.entity.Schedule;
import com.pppppp.amadda.schedule.repository.ScheduleRepository;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;

@EmbeddedKafka(
    partitions = 1,
    brokerProperties = {
        "listeners=PLAINTEXT://localhost:9092",
        "port=9092"
    }
)
class KafkaConsumerTest extends IntegrationTestSupport {

    @Autowired
    private KafkaTopic kafkaTopic;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @MockBean
    private ScheduleAlarmRepository scheduleAlarmRepository;

    @MockBean
    private FriendRequestAlarmRepository friendRequestAlarmRepository;

    private KafkaConsumer kafkaConsumer;

    @BeforeEach
    void setUp() {
        kafkaConsumer = new KafkaConsumer(userRepository, scheduleAlarmRepository,
            friendRequestAlarmRepository, scheduleRepository, friendRequestRepository);
    }

    @AfterEach
    void tearDown() {
        friendRequestAlarmRepository.deleteAllInBatch();
        scheduleAlarmRepository.deleteAllInBatch();
        scheduleRepository.deleteAllInBatch();
        friendRequestRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("alarm.friend-request 토픽의 consume을 검증한다.")
    @Test
    public void alarm_friend_request() {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User u2 = User.create("2222", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User owner = users.get(0);
        User friend = users.get(1);

        FriendRequest friendRequest = FriendRequest.create(owner, friend);
        FriendRequest savedFriendRequest = friendRequestRepository.save(friendRequest);

        String topic = kafkaTopic.ALARM_FRIEND_REQUEST;
        String key = String.valueOf(friend.getUserSeq());
        AlarmFriendRequest value = AlarmFriendRequest.create(
            savedFriendRequest.getRequestSeq(), friend.getUserSeq(),
            friend.getUserName(), savedFriendRequest.getRequestSeq());
        ConsumerRecord<String, AlarmFriendRequest> consumerRecord = new ConsumerRecord<>(topic, 0,
            0, key, value);

        // when
        kafkaConsumer.consumeFriendRequest(consumerRecord);

        // then
        verify(friendRequestAlarmRepository, times(1)).save(any());

    }

    @DisplayName("alarm.friend-accept 토픽의 consume을 검증한다.")
    @Test
    public void alarm_friend_accept() {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User u2 = User.create("2222", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User owner = users.get(0);
        User friend = users.get(1);

        FriendRequest friendRequest = FriendRequest.create(owner, friend);
        FriendRequest saved = friendRequestRepository.save(friendRequest);

        String topic = kafkaTopic.ALARM_FRIEND_ACCEPT;
        String key = String.valueOf(owner.getUserSeq());
        AlarmFriendAccept value = AlarmFriendAccept.create(friend.getUserSeq(),
            friend.getUserName(), saved.getRequestSeq());
        ConsumerRecord<String, AlarmFriendAccept> consumerRecord = new ConsumerRecord<>(topic, 0,
            0, key, value);

        // when
        kafkaConsumer.consumeFriendAccept(consumerRecord);

        // then
        verify(friendRequestAlarmRepository, times(1)).save(any());

    }

    @DisplayName("alarm.schedule-assigned 토픽의 consume을 검증한다.")
    @Test
    public void alarm_schedule_assigned() {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User u2 = User.create("2222", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User user1 = users.get(0);
        User user2 = users.get(1);

        Schedule s = Schedule.create(user1, "집가야돼");
        Schedule schedule = scheduleRepository.save(s);

        String topic = kafkaTopic.ALARM_SCHEDULE_ASSIGNED;
        String key = String.valueOf(user2.getUserSeq());
        AlarmScheduleAssigned value = AlarmScheduleAssigned.create(schedule.getScheduleSeq(),
            schedule.getScheduleContent(), user1.getUserSeq(), user2.getUserName(),
            schedule.getScheduleSeq());
        ConsumerRecord<String, AlarmScheduleAssigned> consumerRecord = new ConsumerRecord<>(topic,
            0,
            0, key, value);

        // when
        kafkaConsumer.consumeScheduleAssigned(consumerRecord);

        // then
        verify(scheduleAlarmRepository, times(1)).save(any());
    }

    @DisplayName("alarm.schedule-update 토픽의 consume을 검증한다.")
    @Test
    public void alarm_schedule_update() {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User u2 = User.create("2222", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User user1 = users.get(0);
        User user2 = users.get(1);

        Schedule s = Schedule.create(user1, "집가야돼");
        Schedule schedule = scheduleRepository.save(s);

        String topic = kafkaTopic.ALARM_SCHEDULE_UPDATE;
        String key = String.valueOf(user2.getUserSeq());
        AlarmScheduleUpdate value = AlarmScheduleUpdate.create(schedule.getScheduleSeq(),
            schedule.getScheduleContent(), schedule.getScheduleSeq());
        ConsumerRecord<String, AlarmScheduleUpdate> consumerRecord = new ConsumerRecord<>(topic, 0,
            0, key, value);

        // when
        kafkaConsumer.consumeScheduleUpdate(consumerRecord);

        // then
        verify(scheduleAlarmRepository, times(1)).save(any());
    }

    @DisplayName("alarm.schedule-notification 토픽의 consume을 검증한다.")
    @ParameterizedTest
    @ValueSource(strings = {"ONE_DAY_BEFORE", "ONE_HOUR_BEFORE", "THIRTY_MINUTES_BEFORE",
        "FIFTEEN_MINUTES_BEFORE", "ON_TIME"})
    public void alarm_schedule_notification(String type) {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User user = userRepository.save(u1);

        Schedule s = Schedule.create(user, "집가야돼");
        Schedule schedule = scheduleRepository.save(s);

        AlarmTime alarmTime = AlarmTime.valueOf(type);

        String topic = kafkaTopic.ALARM_SCHEDULE_UPDATE;
        String key = String.valueOf(user.getUserSeq());
        AlarmScheduleNotification value = AlarmScheduleNotification.create(
            schedule.getScheduleSeq(), "집가야돼", alarmTime, LocalDateTime.now(),
            schedule.getScheduleSeq());
        ConsumerRecord<String, AlarmScheduleNotification> consumerRecord
            = new ConsumerRecord<>(topic, 0, 0, key, value);

        // when
        kafkaConsumer.consumeScheduleNotification(consumerRecord);

        // then
        verify(scheduleAlarmRepository, times(1)).save(any());
    }

    @DisplayName("alarm.schedule-notification consume 시 AlarmTime이 None인 항목은 예외가 발생한다.")
    @Test
    public void alarm_schedule_notification() {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User user = userRepository.save(u1);

        Schedule s = Schedule.create(user, "집가야돼");
        Schedule schedule = scheduleRepository.save(s);

        AlarmTime alarmTime = AlarmTime.NONE;

        String topic = kafkaTopic.ALARM_SCHEDULE_UPDATE;
        String key = String.valueOf(user.getUserSeq());
        AlarmScheduleNotification value = AlarmScheduleNotification.create(
            schedule.getScheduleSeq(), "집가야돼", alarmTime, LocalDateTime.now(),
            schedule.getScheduleSeq());
        ConsumerRecord<String, AlarmScheduleNotification> consumerRecord
            = new ConsumerRecord<>(topic, 0, 0, key, value);

        // when + then
        assertThatThrownBy(() -> kafkaConsumer.consumeScheduleNotification(consumerRecord))
            .isInstanceOf(RestApiException.class)
            .hasMessage("ALARM_NOT_EXIST");
    }
}
