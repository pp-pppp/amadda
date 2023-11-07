package com.pppppp.amadda.alarm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.alarm.dto.request.AlarmConfigRequest;
import com.pppppp.amadda.alarm.dto.response.AlarmReadResponse;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    @MockBean
    KafkaTemplate<Long, BaseTopicValue> kafkaTemplate;
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

    @AfterEach
    void tearDown() {
        alarmRepository.deleteAllInBatch();
        alarmConfigRepository.deleteAllInBatch();
        participationRepository.deleteAllInBatch();
        scheduleRepository.deleteAllInBatch();
        friendRequestRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("글로벌 알람 설정 테스트 - table에 값이 있고 on으로 설정된 경우")
    @ParameterizedTest
    @CsvSource(value = {"FRIEND_REQUEST", "FRIEND_ACCEPT", "SCHEDULE_ASSIGNED", "MENTIONED",
        "SCHEDULE_UPDATE"})
    void checkAlarmConfigOn(String type) {
        // given
        User u1 = User.create(1L, "유저1", "id1", "imageUrl1");
        User user = userRepository.save(u1);

        AlarmType alarmType = AlarmType.of(type);
        AlarmConfig ac = AlarmConfig.create(user, alarmType, true);
        alarmConfigRepository.save(ac);

        // when
        boolean actual = alarmService.checkGlobalAlarmSetting(user.getUserSeq(), alarmType);

        // then
        assertTrue(actual);
    }

    @DisplayName("글로벌 알람 설정 테스트 - table에 값이 있고 off으로 설정된 경우")
    @ParameterizedTest
    @CsvSource(value = {"FRIEND_REQUEST", "FRIEND_ACCEPT", "SCHEDULE_ASSIGNED", "MENTIONED",
        "SCHEDULE_UPDATE"})
    void checkAlarmConfigOff(String type) {
        // given
        User u1 = User.create(1L, "유저1", "id1", "imageUrl1");
        User user = userRepository.save(u1);

        AlarmType alarmType = AlarmType.of(type);
        AlarmConfig ac = AlarmConfig.create(user, alarmType, false);
        alarmConfigRepository.save(ac);

        // when
        boolean actual = alarmService.checkGlobalAlarmSetting(user.getUserSeq(), alarmType);

        // then
        assertFalse(actual);
    }

    @DisplayName("글로벌 알람 설정 테스트 - table에 값이 없는 경우")
    @ParameterizedTest
    @CsvSource(value = {"FRIEND_REQUEST", "FRIEND_ACCEPT", "SCHEDULE_ASSIGNED", "MENTIONED",
        "SCHEDULE_UPDATE"})
    void checkAlarmConfig(String type) {
        // given
        User u1 = User.create(1L, "유저1", "id1", "imageUrl1");
        User user = userRepository.save(u1);

        AlarmType alarmType = AlarmType.of(type);

        // when
        boolean actual = alarmService.checkGlobalAlarmSetting(user.getUserSeq(), alarmType);

        // then
        assertTrue(actual);
    }

    @DisplayName("알림 목록 가져오기")
    @Test
    void getAlarms() {
        // given
        User u1 = User.create(1L, "유저1", "id1", "imageUrl1");
        User user = userRepository.save(u1);

        Alarm a1 = Alarm.create(user, "알람 테스트", AlarmType.FRIEND_REQUEST);
        Alarm a2 = Alarm.create(user, "알람 테스트", AlarmType.FRIEND_ACCEPT);
        Alarm a3 = Alarm.create(user, "알람 테스트", AlarmType.SCHEDULE_ASSIGNED);
        Alarm a4 = Alarm.create(user, "알람 테스트", AlarmType.MENTIONED);
        Alarm a5 = Alarm.create(user, "알람 테스트", AlarmType.SCHEDULE_UPDATE);
        Alarm a6 = Alarm.create(user, "알람 테스트", AlarmType.SCHEDULE_NOTIFICATION);
        List<Alarm> alarms = alarmRepository.saveAll(List.of(a1, a2, a3, a4, a5, a6));

        alarms.get(0).markAsRead();
        alarms.get(1).markAsRead();
        alarms.get(2).markAsRead();
        alarmRepository.saveAll(alarms);

        // when
        List<AlarmReadResponse> result = alarmService.readAlarms(user.getUserSeq());

        // then
        assertThat(result).hasSize(3)
            .extracting("alarmType")
            .containsExactlyInAnyOrder(AlarmType.MENTIONED, AlarmType.SCHEDULE_UPDATE,
                AlarmType.SCHEDULE_NOTIFICATION);
    }

    @DisplayName("모든 알림을 읽은 경우 길이가 0인 리스트를 반환한다.")
    @Test
    void getAlarms_allRead() {
        // given
        User u1 = User.create(1L, "유저1", "id1", "imageUrl1");
        User user = userRepository.save(u1);

        Alarm a1 = Alarm.create(user, "알람 테스트", AlarmType.FRIEND_REQUEST);
        Alarm a2 = Alarm.create(user, "알람 테스트", AlarmType.FRIEND_ACCEPT);
        Alarm a3 = Alarm.create(user, "알람 테스트", AlarmType.SCHEDULE_ASSIGNED);
        List<Alarm> alarms = alarmRepository.saveAll(List.of(a1, a2, a3));

        alarms.get(0).markAsRead();
        alarms.get(1).markAsRead();
        alarms.get(2).markAsRead();
        alarmRepository.saveAll(alarms);

        // when
        List<AlarmReadResponse> result = alarmService.readAlarms(user.getUserSeq());

        // then
        assertThat(result).hasSize(0);
    }

    @DisplayName("존재하지 않는 유저의 알림을 읽으려고 하는 경우 에러가 발생한다.")
    @Test
    void getAlarms_wrongUser() {
        // when + then
        assertThatThrownBy(() -> alarmService.readAlarms(2L))
            .isInstanceOf(RestApiException.class)
            .hasMessage("USER_NOT_FOUND");
    }

    @DisplayName("알림 읽음 처리")
    @Test
    void readAlarm() {
        // given
        User u1 = User.create(1L, "유저1", "id1", "imageUrl1");
        User user = userRepository.save(u1);

        Alarm a = Alarm.create(user, "알람 테스트", AlarmType.FRIEND_ACCEPT);
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

        Alarm a = Alarm.create(user, "알람 테스트", AlarmType.SCHEDULE_ASSIGNED);
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

        Alarm a = Alarm.create(user1, "알람 테스트", AlarmType.FRIEND_REQUEST);
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

    @DisplayName("친구 신청 알람 - 설정 값이 없는 경우")
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

    @DisplayName("친구 신청 알람 - 설정 값이 On인 경우")
    @Test
    void friend_request_on() {
        // given
        List<User> users = create2users();
        User owner = users.get(0);
        User friend = users.get(1);

        FriendRequest friendRequest = FriendRequest.create(owner, friend);
        friendRequestRepository.save(friendRequest);

        AlarmConfig ac = AlarmConfig.create(friend, AlarmType.FRIEND_REQUEST, true);
        alarmConfigRepository.save(ac);

        // when
        alarmService.sendFriendRequest(owner.getUserSeq(), friend.getUserSeq());

        // then
        String topic = KafkaTopic.ALARM_FRIEND_REQUEST;
        Long key = friend.getUserSeq();
        verify(kafkaTemplate, times(1)).send(eq(topic), eq(key), any());
    }

    @DisplayName("친구 신청 알람 - 설정 값이 Off인 경우")
    @Test
    void friend_request_off() {
        // given
        List<User> users = create2users();
        User owner = users.get(0);
        User friend = users.get(1);

        FriendRequest friendRequest = FriendRequest.create(owner, friend);
        friendRequestRepository.save(friendRequest);

        AlarmConfig ac = AlarmConfig.create(friend, AlarmType.FRIEND_REQUEST, false);
        alarmConfigRepository.save(ac);

        // when
        alarmService.sendFriendRequest(owner.getUserSeq(), friend.getUserSeq());

        // then
        String topic = KafkaTopic.ALARM_FRIEND_REQUEST;
        Long key = friend.getUserSeq();
        verify(kafkaTemplate, never()).send(eq(topic), eq(key), any());
    }

    @DisplayName("친구 수락 알람 - 설정 값이 없는 경우")
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

    @DisplayName("친구 수락 알람 - 설정 값이 On인 경우")
    @Test
    void friend_accept_on() {
        // given
        List<User> users = create2users();
        User owner = users.get(0);
        User friend = users.get(1);

        FriendRequest friendRequest = FriendRequest.create(owner, friend);
        friendRequestRepository.save(friendRequest);

        AlarmConfig ac = AlarmConfig.create(owner, AlarmType.FRIEND_ACCEPT, true);
        alarmConfigRepository.save(ac);

        // when
        alarmService.sendFriendAccept(owner.getUserSeq(), friend.getUserSeq());

        // then
        String topic = KafkaTopic.ALARM_FRIEND_ACCEPT;
        Long key = owner.getUserSeq();
        verify(kafkaTemplate, times(1)).send(eq(topic), eq(key), any());
    }

    @DisplayName("친구 수락 알람 - 설정 값이 Off인 경우")
    @Test
    void friend_accept_off() {
        // given
        List<User> users = create2users();
        User owner = users.get(0);
        User friend = users.get(1);

        FriendRequest friendRequest = FriendRequest.create(owner, friend);
        friendRequestRepository.save(friendRequest);

        AlarmConfig ac = AlarmConfig.create(owner, AlarmType.FRIEND_ACCEPT, false);
        alarmConfigRepository.save(ac);

        // when
        alarmService.sendFriendAccept(owner.getUserSeq(), friend.getUserSeq());

        // then
        String topic = KafkaTopic.ALARM_FRIEND_ACCEPT;
        Long key = owner.getUserSeq();
        verify(kafkaTemplate, never()).send(eq(topic), eq(key), any());
    }

    @DisplayName("일정 할당 알람 - 설정 값이 없는 경우")
    @Test
    void schedule_assigned() {
        // given
        List<User> users = create2users();
        User user1 = users.get(0);
        User user2 = users.get(1);

        Schedule schedule = Schedule.builder().authorizedUser(user1).build();
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
        verify(kafkaTemplate, times(1)).send(eq(topic), eq(user2.getUserSeq()), any());
    }

    @DisplayName("일정 할당 알람 - 설정 값이 On인 경우")
    @Test
    void schedule_assigned_on() {
        // given
        List<User> users = create2users();
        User user1 = users.get(0);
        User user2 = users.get(1);

        Schedule schedule = Schedule.builder().authorizedUser(user1).build();
        Schedule savedSchedule = scheduleRepository.save(schedule);

        AlarmConfig ac = AlarmConfig.create(user2, AlarmType.SCHEDULE_ASSIGNED, true);
        alarmConfigRepository.save(ac);

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
        verify(kafkaTemplate, times(1)).send(eq(topic), eq(user2.getUserSeq()), any());
    }

    @DisplayName("일정 할당 알람 - 설정 값이 Off인 경우")
    @Test
    void schedule_assigned_off() {
        // given
        List<User> users = create2users();
        User user1 = users.get(0);
        User user2 = users.get(1);

        Schedule schedule = Schedule.builder().authorizedUser(user1).build();
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

        AlarmConfig ac = AlarmConfig.create(user2, AlarmType.SCHEDULE_ASSIGNED, false);
        alarmConfigRepository.save(ac);

        // when
        alarmService.sendScheduleAssigned(savedSchedule.getScheduleSeq());

        // then
        String topic = KafkaTopic.ALARM_SCHEDULE_ASSIGNED;
        Long key = user2.getUserSeq();
        verify(kafkaTemplate, never()).send(eq(topic), eq(user2.getUserSeq()), any());
    }

    @DisplayName("일정 수정 알림 - Global은 없고, Local은 On인 경우")
    @Test
    void schedule_update_null_on() {
        // given
        create3UsersAndSchedule();

        List<User> users = userRepository.findAll();
        User user1 = users.get(0); // 생성+수정한 사람
        User user2 = users.get(1); // 테스트 타깃
        User user3 = users.get(2); // 일정에 할당되지 않은 사람
        Schedule schedule = scheduleRepository.findAll().get(0);

        Participation participation = participationRepository.findBySchedule_ScheduleSeqAndUser_UserSeqAndIsDeletedFalse(
            schedule.getScheduleSeq(), user2.getUserSeq()).get();
        participation.updateIsUpdateAlarmOn(true);
        participationRepository.save(participation);

        // when
        alarmService.sendScheduleUpdate(schedule.getScheduleSeq(), user1.getUserSeq());

        // then
        String topic = KafkaTopic.ALARM_SCHEDULE_UPDATE;
        verify(kafkaTemplate, never()).send(eq(topic), eq(user1.getUserSeq()), any());
        verify(kafkaTemplate, times(1)).send(eq(topic), eq(user2.getUserSeq()), any());
        verify(kafkaTemplate, never()).send(eq(topic), eq(user3.getUserSeq()), any());
    }

    @DisplayName("일정 수정 알림 - Global은 없고, Local은 Off인 경우")
    @Test
    void schedule_update_null_off() {
        // given
        create3UsersAndSchedule();

        List<User> users = userRepository.findAll();
        User user1 = users.get(0); // 생성+수정한 사람
        User user2 = users.get(1); // 테스트 타깃
        User user3 = users.get(2); // 일정에 할당되지 않은 사람
        Schedule schedule = scheduleRepository.findAll().get(0);

        Participation participation = participationRepository.findBySchedule_ScheduleSeqAndUser_UserSeqAndIsDeletedFalse(
            schedule.getScheduleSeq(), user2.getUserSeq()).get();
        participation.updateIsUpdateAlarmOn(false);
        participationRepository.save(participation);

        // when
        alarmService.sendScheduleUpdate(schedule.getScheduleSeq(), user1.getUserSeq());

        // then
        String topic = KafkaTopic.ALARM_SCHEDULE_UPDATE;
        verify(kafkaTemplate, never()).send(eq(topic), eq(user1.getUserSeq()), any());
        verify(kafkaTemplate, never()).send(eq(topic), eq(user2.getUserSeq()), any());
        verify(kafkaTemplate, never()).send(eq(topic), eq(user3.getUserSeq()), any());
    }

    @DisplayName("일정 수정 알림 - Global은 On, Local은 On인 경우")
    @Test
    void schedule_update_on_on() {
        // given
        create3UsersAndSchedule();

        List<User> users = userRepository.findAll();
        User user1 = users.get(0); // 생성+수정한 사람
        User user2 = users.get(1); // 테스트 타깃
        User user3 = users.get(2); // 일정에 할당되지 않은 사람
        Schedule schedule = scheduleRepository.findAll().get(0);

        AlarmConfig config = AlarmConfig.create(user2, AlarmType.SCHEDULE_UPDATE, true);
        alarmConfigRepository.save(config);

        Participation participation = participationRepository.findBySchedule_ScheduleSeqAndUser_UserSeqAndIsDeletedFalse(
            schedule.getScheduleSeq(), user2.getUserSeq()).get();
        participation.updateIsUpdateAlarmOn(true);
        participationRepository.save(participation);

        // when
        alarmService.sendScheduleUpdate(schedule.getScheduleSeq(), user1.getUserSeq());

        // then
        String topic = KafkaTopic.ALARM_SCHEDULE_UPDATE;
        verify(kafkaTemplate, never()).send(eq(topic), eq(user1.getUserSeq()), any());
        verify(kafkaTemplate, times(1)).send(eq(topic), eq(user2.getUserSeq()), any());
        verify(kafkaTemplate, never()).send(eq(topic), eq(user3.getUserSeq()), any());
    }

    @DisplayName("일정 수정 알림 - Global은 On, Local은 Off인 경우")
    @Test
    void schedule_update_on_off() {
        // given
        create3UsersAndSchedule();

        List<User> users = userRepository.findAll();
        User user1 = users.get(0); // 생성+수정한 사람
        User user2 = users.get(1); // 테스트 타깃
        User user3 = users.get(2); // 일정에 할당되지 않은 사람
        Schedule schedule = scheduleRepository.findAll().get(0);

        AlarmConfig config = AlarmConfig.create(user2, AlarmType.SCHEDULE_UPDATE, true);
        alarmConfigRepository.save(config);

        Participation participation = participationRepository.findBySchedule_ScheduleSeqAndUser_UserSeqAndIsDeletedFalse(
            schedule.getScheduleSeq(), user2.getUserSeq()).get();
        participation.updateIsUpdateAlarmOn(false);
        participationRepository.save(participation);

        // when
        alarmService.sendScheduleUpdate(schedule.getScheduleSeq(), user1.getUserSeq());

        // then
        String topic = KafkaTopic.ALARM_SCHEDULE_UPDATE;
        verify(kafkaTemplate, never()).send(eq(topic), eq(user1.getUserSeq()), any());
        verify(kafkaTemplate, never()).send(eq(topic), eq(user2.getUserSeq()), any());
        verify(kafkaTemplate, never()).send(eq(topic), eq(user3.getUserSeq()), any());
    }

    @DisplayName("일정 수정 알림 - Global은 Off, Local은 On인 경우")
    @Test
    void schedule_update_off_on() {
        // given
        create3UsersAndSchedule();

        List<User> users = userRepository.findAll();
        User user1 = users.get(0); // 생성+수정한 사람
        User user2 = users.get(1); // 테스트 타깃
        User user3 = users.get(2); // 일정에 할당되지 않은 사람
        Schedule schedule = scheduleRepository.findAll().get(0);

        AlarmConfig config = AlarmConfig.create(user2, AlarmType.SCHEDULE_UPDATE, false);
        alarmConfigRepository.save(config);

        Participation participation = participationRepository.findBySchedule_ScheduleSeqAndUser_UserSeqAndIsDeletedFalse(
            schedule.getScheduleSeq(), user2.getUserSeq()).get();
        participation.updateIsUpdateAlarmOn(true);
        participationRepository.save(participation);

        // when
        alarmService.sendScheduleUpdate(schedule.getScheduleSeq(), user1.getUserSeq());

        // then
        String topic = KafkaTopic.ALARM_SCHEDULE_UPDATE;
        verify(kafkaTemplate, never()).send(eq(topic), eq(user1.getUserSeq()), any());
        verify(kafkaTemplate, never()).send(eq(topic), eq(user2.getUserSeq()), any());
        verify(kafkaTemplate, never()).send(eq(topic), eq(user3.getUserSeq()), any());
    }

    @DisplayName("일정 수정 알림 - Global은 Off, Local은 Off인 경우")
    @Test
    void schedule_update_off_off() {
        // given
        create3UsersAndSchedule();

        List<User> users = userRepository.findAll();
        User user1 = users.get(0); // 생성+수정한 사람
        User user2 = users.get(1); // 테스트 타깃
        User user3 = users.get(2); // 일정에 할당되지 않은 사람
        Schedule schedule = scheduleRepository.findAll().get(0);

        AlarmConfig config = AlarmConfig.create(user2, AlarmType.SCHEDULE_UPDATE, false);
        alarmConfigRepository.save(config);

        Participation participation = participationRepository.findBySchedule_ScheduleSeqAndUser_UserSeqAndIsDeletedFalse(
            schedule.getScheduleSeq(), user2.getUserSeq()).get();
        participation.updateIsUpdateAlarmOn(false);
        participationRepository.save(participation);

        // when
        alarmService.sendScheduleUpdate(schedule.getScheduleSeq(), user1.getUserSeq());

        // then
        String topic = KafkaTopic.ALARM_SCHEDULE_UPDATE;
        verify(kafkaTemplate, never()).send(eq(topic), eq(user1.getUserSeq()), any());
        verify(kafkaTemplate, never()).send(eq(topic), eq(user2.getUserSeq()), any());
        verify(kafkaTemplate, never()).send(eq(topic), eq(user3.getUserSeq()), any());
    }

    private List<User> create2users() {
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        return userRepository.saveAll(List.of(u1, u2));
    }

    private void create3UsersAndSchedule() {
        User u1 = User.create(1L, "유저1", "user1", "image1");
        User u2 = User.create(2L, "유저2", "user2", "image2");
        User u3 = User.create(3L, "유저3", "user3", "image3");
        List<User> users = userRepository.saveAll(List.of(u1, u2, u3));
        User user1 = users.get(0);
        User user2 = users.get(1);

        Schedule schedule = Schedule.builder().authorizedUser(user1).build();
        Schedule savedSchedule = scheduleRepository.save(schedule);

        Participation participation1 = Participation.builder()
            .user(user1)
            .schedule(savedSchedule)
            .scheduleName("민들레")
            .isUpdateAlarmOn(true)
            .isMentionAlarmOn(true)
            .build();
        Participation participation2 = Participation.builder()
            .user(user2)
            .schedule(savedSchedule)
            .scheduleName("떡볶이")
            .isUpdateAlarmOn(true)
            .isMentionAlarmOn(true)
            .build();
        participationRepository.saveAll(List.of(participation1, participation2));
    }
}
