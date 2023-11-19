package com.pppppp.amadda.alarm.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.alarm.entity.AlarmConfig;
import com.pppppp.amadda.alarm.entity.AlarmType;
import com.pppppp.amadda.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class AlarmConfigRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private AlarmConfigRepository alarmConfigRepository;

    @AfterEach
    void tearDown() {
        alarmConfigRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("글로벌 알림 설정이 가능한 항목에 대해 유저의 글로벌 설정이 true로 되어있음을 확인한다.")
    @ParameterizedTest
    @ValueSource(strings = {"FRIEND_REQUEST", "FRIEND_ACCEPT", "SCHEDULE_ASSIGNED", "MENTIONED",
        "SCHEDULE_UPDATE"})
    void findByUser_UserSeqAndAlarmTypeAnd_True(String type) {
        // given
        AlarmType alarmType = AlarmType.valueOf(type);

        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User user = userRepository.save(u1);

        AlarmConfig ac = AlarmConfig.create(user, alarmType, true);
        alarmConfigRepository.save(ac);

        // when
        Optional<AlarmConfig> config = alarmConfigRepository.findByUser_UserSeqAndAlarmType(
            user.getUserSeq(), alarmType);

        // then
        assertTrue(config.isPresent());
        assertTrue(config.get().isEnabled());
    }

    @DisplayName("글로벌 알림 설정이 가능한 항목에 대해 유저의 글로벌 설정이 false로 되어있음을 확인한다.")
    @ParameterizedTest
    @ValueSource(strings = {"FRIEND_REQUEST", "FRIEND_ACCEPT", "SCHEDULE_ASSIGNED", "MENTIONED",
        "SCHEDULE_UPDATE"})
    void findByUser_UserSeqAndAlarmType_False(String type) {
        // given
        AlarmType alarmType = AlarmType.valueOf(type);

        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User user = userRepository.save(u1);

        AlarmConfig ac = AlarmConfig.create(user, alarmType, false);
        alarmConfigRepository.save(ac);

        // when
        Optional<AlarmConfig> config = alarmConfigRepository.findByUser_UserSeqAndAlarmType(
            user.getUserSeq(), alarmType);

        // then
        assertTrue(config.isPresent());
        assertFalse(config.get().isEnabled());
    }

    @DisplayName("글로벌 알림 설정이 가능한 항목에 대해 최초 설정을 하지 않는 경우 알림 설정값은 존재하지 않는다.")
    @ParameterizedTest
    @ValueSource(strings = {"FRIEND_REQUEST", "FRIEND_ACCEPT", "SCHEDULE_ASSIGNED", "MENTIONED",
        "SCHEDULE_UPDATE"})
    void findByUser_UserSeqAndAlarmType_Null(String type) {
        // given
        AlarmType alarmType = AlarmType.valueOf(type);

        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User user = userRepository.save(u1);

        // when
        Optional<AlarmConfig> config = alarmConfigRepository.findByUser_UserSeqAndAlarmType(
            user.getUserSeq(), alarmType);

        // then
        assertTrue(config.isEmpty());
    }

    @DisplayName("글로벌 알림 설정이 가능한 항목에 대해 off로 설정된 항목을 찾아온다.")
    @ParameterizedTest
    @CsvSource(value = {"FRIEND_REQUEST", "FRIEND_ACCEPT", "SCHEDULE_ASSIGNED", "MENTIONED",
        "SCHEDULE_UPDATE"})
    void existAndOff(String type) {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User user = userRepository.save(u1);

        AlarmType alarmType = AlarmType.valueOf(type);
        AlarmConfig ac = AlarmConfig.create(user, alarmType, false);
        alarmConfigRepository.save(ac);

        // when
        Optional<AlarmConfig> config = alarmConfigRepository.findByUser_UserSeqAndAlarmTypeAndIsEnabledFalse(
            user.getUserSeq(), alarmType);

        // then
        assertThat(config).isNotEmpty();
        assertThat(config.get())
            .extracting("user.userSeq", "alarmType", "isEnabled")
            .containsExactly(user.getUserSeq(), alarmType, false);
    }

    @DisplayName("글로벌 알림 설정이 가능한 항목에 대해 off로 설정된 항목을 찾아올 때 on인 항목은 제외된다.")
    @ParameterizedTest
    @CsvSource(value = {"FRIEND_REQUEST", "FRIEND_ACCEPT", "SCHEDULE_ASSIGNED", "MENTIONED",
        "SCHEDULE_UPDATE"})
    void notExistAndOn(String type) {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User user = userRepository.save(u1);

        AlarmType alarmType = AlarmType.valueOf(type);
        AlarmConfig ac = AlarmConfig.create(user, alarmType, true);
        alarmConfigRepository.save(ac);

        // when
        Optional<AlarmConfig> config = alarmConfigRepository.findByUser_UserSeqAndAlarmTypeAndIsEnabledFalse(
            user.getUserSeq(), alarmType);

        // then
        assertThat(config).isEmpty();
    }

    @DisplayName("글로벌 알림 설정이 가능한 항목에 대해 off로 설정된 항목을 찾아올 때 설정값이 없는 항목은 제외된다.")
    @ParameterizedTest
    @CsvSource(value = {"FRIEND_REQUEST", "FRIEND_ACCEPT", "SCHEDULE_ASSIGNED", "MENTIONED",
        "SCHEDULE_UPDATE"})
    void notExist(String type) {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User user = userRepository.save(u1);

        AlarmType alarmType = AlarmType.valueOf(type);

        // when
        Optional<AlarmConfig> config = alarmConfigRepository.findByUser_UserSeqAndAlarmTypeAndIsEnabledFalse(
            user.getUserSeq(), alarmType);

        // then
        assertThat(config).isEmpty();
    }

    @DisplayName("유저의 글로벌 알림 설정 값을 가져온다.")
    @Test
    void findAllByUser_UserSeq() {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User u2 = User.create("2222", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User user1 = users.get(0);
        User user2 = users.get(1);

        AlarmConfig ac1 = AlarmConfig.create(user1, AlarmType.FRIEND_REQUEST, true);
        AlarmConfig ac2 = AlarmConfig.create(user1, AlarmType.FRIEND_ACCEPT, true);
        AlarmConfig ac3 = AlarmConfig.create(user1, AlarmType.SCHEDULE_ASSIGNED, false);
        AlarmConfig ac4 = AlarmConfig.create(user1, AlarmType.MENTIONED, false);
        AlarmConfig ac5 = AlarmConfig.create(user2, AlarmType.FRIEND_REQUEST, true);
        AlarmConfig ac6 = AlarmConfig.create(user2, AlarmType.FRIEND_ACCEPT, false);
        alarmConfigRepository.saveAll(List.of(ac1, ac2, ac3, ac4, ac5, ac6));

        // when
        List<AlarmConfig> configs = alarmConfigRepository.findAllByUser_UserSeq(
            user1.getUserSeq());

        // then
        assertThat(configs).hasSize(4)
            .extracting("alarmType", "isEnabled")
            .containsExactlyInAnyOrder(
                tuple(AlarmType.FRIEND_REQUEST, true),
                tuple(AlarmType.FRIEND_ACCEPT, true),
                tuple(AlarmType.SCHEDULE_ASSIGNED, false),
                tuple(AlarmType.MENTIONED, false)
            );
    }

}
