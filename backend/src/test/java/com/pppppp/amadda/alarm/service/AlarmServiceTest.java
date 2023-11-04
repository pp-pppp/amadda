package com.pppppp.amadda.alarm.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.alarm.dto.topic.BaseTopicValue;
import com.pppppp.amadda.alarm.entity.KafkaTopic;
import com.pppppp.amadda.friend.entity.FriendRequest;
import com.pppppp.amadda.friend.repository.FriendRequestRepository;
import com.pppppp.amadda.schedule.entity.Participation;
import com.pppppp.amadda.schedule.entity.Schedule;
import com.pppppp.amadda.schedule.repository.ParticipationRepository;
import com.pppppp.amadda.schedule.repository.ScheduleRepository;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
@EmbeddedKafka(
    partitions = 1,
    brokerProperties = {
        "listeners=PLAINTEXT://localhost:9091",
        "port=9091"
    }
)
class AlarmServiceTest extends IntegrationTestSupport {

    @Autowired
    private AlarmService alarmService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ParticipationRepository participationRepository;

    @MockBean
    KafkaTemplate<Long, BaseTopicValue> kafkaTemplate;

    @AfterEach
    void tearDown() {
        participationRepository.deleteAllInBatch();
        scheduleRepository.deleteAllInBatch();
        friendRequestRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("친구 신청 알람")
    @Test
    void friend_request() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User owner = users.get(0);
        User friend = users.get(1);

        FriendRequest friendRequest = FriendRequest.create(owner, friend);
        FriendRequest savedFriendRequest = friendRequestRepository.save(friendRequest);

        // when
        alarmService.sendFriendRequest(owner.getUserSeq(), friend.getUserSeq());

        // then
        String topic = KafkaTopic.ALARM_FRIEND_REQUEST;
        Long key = 1234L;
        verify(kafkaTemplate, times(1)).send(eq(topic), eq(key), any());
    }

    @DisplayName("친구 수락 알람")
    @Test
    void friend_accept() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User owner = users.get(0);
        User friend = users.get(1);

        FriendRequest friendRequest = FriendRequest.create(owner, friend);
        FriendRequest savedFriendRequest = friendRequestRepository.save(friendRequest);

        // when
        alarmService.sendFriendAccept(owner.getUserSeq(), friend.getUserSeq());

        // then
        String topic = KafkaTopic.ALARM_FRIEND_ACCEPT;
        Long key = 1111L;
        verify(kafkaTemplate, times(1)).send(eq(topic), eq(key), any());
    }

    @DisplayName("일정 할당 알람")
    @Test
    void schedule_assigned() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User user1 = users.get(0);
        User user2 = users.get(1);

        Schedule schedule = Schedule.builder().user(user1).build();
        Schedule savedSchedule = scheduleRepository.save(schedule);
        Participation participation1 = Participation.builder()
            .user(user1)
            .schedule(savedSchedule)
            .scheduleName("밥")
            .build();
        Participation participation2 = Participation.builder()
            .user(user2)
            .schedule(savedSchedule)
            .scheduleName("밥밥")
            .build();
        List<Participation> participations = participationRepository.saveAll(
            List.of(participation1, participation2));

        // when
        alarmService.sendScheduleAssigned(savedSchedule.getScheduleSeq());

        // then
        String topic = KafkaTopic.ALARM_SCHEDULE_ASSIGNED;
        Long key = user2.getUserSeq();
        verify(kafkaTemplate, times(1)).send(eq(topic), eq(key), any());
    }
}
