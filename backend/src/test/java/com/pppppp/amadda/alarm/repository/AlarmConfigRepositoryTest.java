package com.pppppp.amadda.alarm.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.alarm.entity.AlarmConfig;
import com.pppppp.amadda.alarm.entity.AlarmType;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private AlarmConfigRepository alarmConfigRepository;

    @AfterEach
    void tearDown() {
        alarmConfigRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("유저의 알림 글로벌 설정값 확인 - true")
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

    @DisplayName("유저의 알림 글로벌 설정값 확인 - false")
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

    @DisplayName("유저의 알림 글로벌 설정값 확인 - null")
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

    @DisplayName("설정이 off인지 확인하기")
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

    @DisplayName("설정이 on인지 확인하기")
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

    @DisplayName("설정값이 없는지 확인하기")
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

    @DisplayName("유저의 전체 설정값 가져오기")
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
