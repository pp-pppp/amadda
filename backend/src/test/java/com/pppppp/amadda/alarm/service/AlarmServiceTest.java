package com.pppppp.amadda.alarm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.alarm.dto.request.AlarmConfigRequest;
import com.pppppp.amadda.alarm.dto.topic.BaseTopicValue;
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
import com.pppppp.amadda.schedule.entity.Participation;
import com.pppppp.amadda.schedule.entity.Schedule;
import com.pppppp.amadda.schedule.repository.ParticipationRepository;
import com.pppppp.amadda.schedule.repository.ScheduleRepository;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

    @Autowired
    private AlarmConfigRepository alarmConfigRepository;

    @Autowired
    private AlarmRepository alarmRepository;

    @MockBean
    KafkaTemplate<Long, BaseTopicValue> kafkaTemplate;

    @AfterEach
    void tearDown() {
        alarmRepository.deleteAllInBatch();
        alarmConfigRepository.deleteAllInBatch();
        participationRepository.deleteAllInBatch();
        scheduleRepository.deleteAllInBatch();
        friendRequestRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("알림 읽음 처리")
    @Test
    void readAlarm() {
        // given
        User u1 = User.create(1L, "유저1", "id1", "imageUrl1");
        User user = userRepository.save(u1);

        Alarm a = Alarm.create(user, "알람 테스트");
        Alarm alarm = alarmRepository.save(a);

        // when
        alarmService.readAlarm(alarm.getAlarmSeq(), user.getUserSeq());

        // then
        Alarm result = alarmRepository.findById(alarm.getAlarmSeq()).get();
        assertTrue(result.isRead());
    }

    @DisplayName("알림 읽음 처리 - 존재하지 않는 유저로 요청")
    @Test
    void readAlarm_unauthorized() {
        // given
        User u1 = User.create(1L, "유저1", "id1", "imageUrl1");
        User user = userRepository.save(u1);

        Alarm a = Alarm.create(user, "알람 테스트");
        Alarm alarm = alarmRepository.save(a);

        // when + then
        assertThatThrownBy(() -> alarmService.readAlarm(alarm.getAlarmSeq(), 2L))
            .isInstanceOf(RestApiException.class)
            .hasMessage("USER_NOT_FOUND");
    }

    @DisplayName("알림 읽음 처리 - 본인 알림이 아닌 경우")
    @Test
    void readAlarm_forbidden() {
        // given
        List<User> users = create2users();
        User user1 = users.get(0);
        User user2 = users.get(1);

        Alarm a = Alarm.create(user1, "알람 테스트");
        Alarm alarm = alarmRepository.save(a);

        // when + then
        assertThatThrownBy(() -> alarmService.readAlarm(alarm.getAlarmSeq(), user2.getUserSeq()))
            .isInstanceOf(RestApiException.class)
            .hasMessage("ALARM_FORBIDDEN");
    }

    @DisplayName("글로벌 설정이 가능한 알림에 대해 AlarmConfig 생성 후 On")
    @ParameterizedTest
    @ValueSource(strings = {"FRIEND_REQUEST", "FRIEND_ACCEPT", "SCHEDULE_ASSIGNED", "MENTIONED",
        "SCHEDULE_UPDATE"})
    void forGlobalSettingCreateAlarmConfigAndOn(String type) {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User user = userRepository.save(u1);

        AlarmType alarmType = AlarmType.of(type);
        AlarmConfigRequest request = AlarmConfigRequest.builder()
            .userSeq(user.getUserSeq()).alarmType(alarmType).build();

        // when
        AlarmConfig alarmConfig = alarmService.setGlobalAlarm(request, true);

        // then
        assertThat(alarmConfig.getUser().getUserSeq()).isEqualTo(user.getUserSeq());
        assertTrue(alarmConfig.isEnabled());
        assertThat(alarmConfig.getAlarmType()).isEqualTo(alarmType);
    }

    @DisplayName("글로벌 설정이 가능한 알림에 대해 AlarmConfig 확인 후 On")
    @ParameterizedTest
    @ValueSource(strings = {"FRIEND_REQUEST", "FRIEND_ACCEPT", "SCHEDULE_ASSIGNED", "MENTIONED",
        "SCHEDULE_UPDATE"})
    void forGlobalSettingFindAlarmConfigAndOn(String type) {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User user = userRepository.save(u1);

        AlarmType alarmType = AlarmType.of(type);
        AlarmConfig alarmConfig = AlarmConfig.create(user, alarmType, false);
        alarmConfigRepository.save(alarmConfig);

        AlarmConfigRequest request = AlarmConfigRequest.builder()
            .userSeq(user.getUserSeq()).alarmType(alarmType).build();

        // when
        AlarmConfig savedAlarmConfig = alarmService.setGlobalAlarm(request, true);

        // then
        assertThat(savedAlarmConfig.getUser().getUserSeq()).isEqualTo(user.getUserSeq());
        assertTrue(savedAlarmConfig.isEnabled());
        assertThat(savedAlarmConfig.getAlarmType()).isEqualTo(alarmType);
    }

    @DisplayName("글로벌 설정이 가능한 알림에 대해 AlarmConfig 생성 후 Off")
    @ParameterizedTest
    @ValueSource(strings = {"FRIEND_REQUEST", "FRIEND_ACCEPT", "SCHEDULE_ASSIGNED", "MENTIONED",
        "SCHEDULE_UPDATE"})
    void forGlobalSettingCreateAlarmConfigAndOff(String type) {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User user = userRepository.save(u1);
        AlarmType alarmType = AlarmType.of(type);

        AlarmConfigRequest request = AlarmConfigRequest.builder()
            .userSeq(user.getUserSeq()).alarmType(alarmType).build();

        // when
        AlarmConfig alarmConfig = alarmService.setGlobalAlarm(request, false);

        // then
        assertThat(alarmConfig.getUser().getUserSeq()).isEqualTo(user.getUserSeq());
        assertFalse(alarmConfig.isEnabled());
        assertThat(alarmConfig.getAlarmType()).isEqualTo(alarmType);
    }

    @DisplayName("글로벌 설정이 가능한 알림에 대해 AlarmConfig 확인 후 Off")
    @ParameterizedTest
    @ValueSource(strings = {"FRIEND_REQUEST", "FRIEND_ACCEPT", "SCHEDULE_ASSIGNED", "MENTIONED",
        "SCHEDULE_UPDATE"})
    void forGlobalSettingFindAlarmConfigAndOff(String type) {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User user = userRepository.save(u1);

        AlarmType alarmType = AlarmType.of(type);
        AlarmConfig alarmConfig = AlarmConfig.create(user, alarmType, true);
        alarmConfigRepository.save(alarmConfig);

        AlarmConfigRequest request = AlarmConfigRequest.builder()
            .userSeq(user.getUserSeq()).alarmType(alarmType).build();

        // when
        AlarmConfig savedAlarmConfig = alarmService.setGlobalAlarm(request, false);

        // then
        assertThat(savedAlarmConfig.getUser().getUserSeq()).isEqualTo(user.getUserSeq());
        assertFalse(savedAlarmConfig.isEnabled());
        assertThat(savedAlarmConfig.getAlarmType()).isEqualTo(alarmType);
    }

    @DisplayName("일정 예정 알림은 설정을 변경할 수 없음")
    @Test
    void cannotChangeScheduleNotificationAlarmSetting() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User user = userRepository.save(u1);

        AlarmType alarmType = AlarmType.SCHEDULE_NOTIFICATION;
        AlarmConfigRequest request = AlarmConfigRequest.builder()
            .userSeq(user.getUserSeq()).alarmType(alarmType).build();

        // when + then
        assertThatThrownBy(() -> alarmService.setGlobalAlarm(request, true))
            .isInstanceOf(RestApiException.class)
            .hasMessage(AlarmErrorCode.CANNOT_SET_GLOBAL_CONFIG.name());
    }

    @DisplayName("친구 신청 알람")
    @Test
    void friend_request() {
        // given
        List<User> users = create2users();
        User owner = users.get(0);
        User friend = users.get(1);

        FriendRequest friendRequest = FriendRequest.create(owner, friend);
        friendRequestRepository.save(friendRequest);

        // when
        alarmService.sendFriendRequest(owner.getUserSeq(), friend.getUserSeq());

        // then
        String topic = KafkaTopic.ALARM_FRIEND_REQUEST;
        Long key = friend.getUserSeq();
        verify(kafkaTemplate, times(1)).send(eq(topic), eq(key), any());
    }

    @DisplayName("친구 수락 알람")
    @Test
    void friend_accept() {
        // given
        List<User> users = create2users();
        User owner = users.get(0);
        User friend = users.get(1);

        FriendRequest friendRequest = FriendRequest.create(owner, friend);
        friendRequestRepository.save(friendRequest);

        // when
        alarmService.sendFriendAccept(owner.getUserSeq(), friend.getUserSeq());

        // then
        String topic = KafkaTopic.ALARM_FRIEND_ACCEPT;
        Long key = owner.getUserSeq();
        verify(kafkaTemplate, times(1)).send(eq(topic), eq(key), any());
    }

    @DisplayName("일정 할당 알람")
    @Test
    void schedule_assigned() {
        // given
        List<User> users = create2users();
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
        participationRepository.saveAll(List.of(participation1, participation2));

        // when
        alarmService.sendScheduleAssigned(savedSchedule.getScheduleSeq());

        // then
        String topic = KafkaTopic.ALARM_SCHEDULE_ASSIGNED;
        Long key = user2.getUserSeq();
        verify(kafkaTemplate, times(1)).send(eq(topic), eq(key), any());
    }

    @NotNull
    private List<User> create2users() {
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        return userRepository.saveAll(List.of(u1, u2));
    }
}
