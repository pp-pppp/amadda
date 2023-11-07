package com.pppppp.amadda.alarm.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.alarm.entity.AlarmConfig;
import com.pppppp.amadda.alarm.entity.AlarmType;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    @DisplayName("설정이 off인지 확인하기")
    @ParameterizedTest
    @CsvSource(value = {"FRIEND_REQUEST", "FRIEND_ACCEPT", "SCHEDULE_ASSIGNED", "MENTIONED",
        "SCHEDULE_UPDATE"})
    void existAndOff(String type) {
        // given
        User u1 = User.create(1L, "유저1", "id1", "imageUrl1");
        User user = userRepository.save(u1);

        AlarmType alarmType = AlarmType.of(type);
        AlarmConfig ac = AlarmConfig.create(user, alarmType, false);
        alarmConfigRepository.save(ac);

        // when
        Optional<AlarmConfig> config = alarmConfigRepository.findByUser_UserSeqAndAlarmTypeAndIsEnabledFalseAndIsDeletedFalse(
            user.getUserSeq(), alarmType);

        // then
        assertThat(config).isNotEmpty();
        assertThat(config.get()).extracting("user.userSeq", "alarmType", "isEnabled")
            .containsExactly(user.getUserSeq(), alarmType, false);
    }

    @DisplayName("설정이 on인지 확인하기")
    @ParameterizedTest
    @CsvSource(value = {"FRIEND_REQUEST", "FRIEND_ACCEPT", "SCHEDULE_ASSIGNED", "MENTIONED",
        "SCHEDULE_UPDATE"})
    void notExistAndOn(String type) {
        // given
        User u1 = User.create(1L, "유저1", "id1", "imageUrl1");
        User user = userRepository.save(u1);

        AlarmType alarmType = AlarmType.of(type);
        AlarmConfig ac = AlarmConfig.create(user, alarmType, true);
        alarmConfigRepository.save(ac);

        // when
        Optional<AlarmConfig> config = alarmConfigRepository.findByUser_UserSeqAndAlarmTypeAndIsEnabledFalseAndIsDeletedFalse(
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
        User u1 = User.create(1L, "유저1", "id1", "imageUrl1");
        User user = userRepository.save(u1);

        AlarmType alarmType = AlarmType.of(type);

        // when
        Optional<AlarmConfig> config = alarmConfigRepository.findByUser_UserSeqAndAlarmTypeAndIsEnabledFalseAndIsDeletedFalse(
            user.getUserSeq(), alarmType);

        // then
        assertThat(config).isEmpty();
    }

}
