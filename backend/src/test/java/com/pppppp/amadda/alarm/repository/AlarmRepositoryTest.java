package com.pppppp.amadda.alarm.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.alarm.entity.Alarm;
import com.pppppp.amadda.alarm.entity.AlarmType;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AlarmRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlarmRepository alarmRepository;

    @AfterEach
    void tearDown() {
        alarmRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("사용자의 읽지 않은 알람을 조회한다.")
    @Test
    void getAlarms() {
        // given
        User u1 = User.create(1L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(2L, "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User user1 = users.get(0);
        User user2 = users.get(1);

        Alarm a1 = Alarm.create(user1, "알람 테스트", AlarmType.FRIEND_REQUEST);
        Alarm a2 = Alarm.create(user1, "알람 테스트", AlarmType.FRIEND_ACCEPT);
        Alarm a3 = Alarm.create(user1, "알람 테스트", AlarmType.SCHEDULE_ASSIGNED);
        Alarm a4 = Alarm.create(user2, "알람 테스트", AlarmType.MENTIONED);
        Alarm a5 = Alarm.create(user2, "알람 테스트", AlarmType.SCHEDULE_UPDATE);
        Alarm a6 = Alarm.create(user2, "알람 테스트", AlarmType.SCHEDULE_NOTIFICATION);
        List<Alarm> alarms = alarmRepository.saveAll(List.of(a1, a2, a3, a4, a5, a6));

        alarms.get(2).markAsRead();
        alarms.get(5).markAsRead();
        alarmRepository.saveAll(alarms);

        // when
        List<Alarm> result = alarmRepository.findAllByUserAndIsReadFalseAndIsDeletedFalse(user1);

        // then
        assertThat(result).hasSize(2)
            .extracting("user.userSeq", "alarmType", "isRead")
            .containsExactlyInAnyOrder(
                tuple(user1.getUserSeq(), AlarmType.FRIEND_REQUEST, false),
                tuple(user1.getUserSeq(), AlarmType.FRIEND_ACCEPT, false)
            );
    }
}