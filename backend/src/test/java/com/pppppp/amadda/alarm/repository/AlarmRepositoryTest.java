package com.pppppp.amadda.alarm.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.alarm.entity.Alarm;
import com.pppppp.amadda.alarm.entity.AlarmContent;
import com.pppppp.amadda.alarm.entity.AlarmType;
import com.pppppp.amadda.alarm.entity.FriendRequestAlarm;
import com.pppppp.amadda.alarm.entity.ScheduleAlarm;
import com.pppppp.amadda.friend.entity.FriendRequest;
import com.pppppp.amadda.friend.repository.FriendRequestRepository;
import com.pppppp.amadda.schedule.entity.Schedule;
import com.pppppp.amadda.schedule.repository.ScheduleRepository;
import com.pppppp.amadda.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AlarmRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private FriendRequestAlarmRepository friendRequestAlarmRepository;

    @Autowired
    private ScheduleAlarmRepository scheduleAlarmRepository;

    @AfterEach
    void tearDown() {
        scheduleAlarmRepository.deleteAllInBatch();
        friendRequestAlarmRepository.deleteAllInBatch();
        scheduleRepository.deleteAllInBatch();
        friendRequestRepository.deleteAllInBatch();
        alarmRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("유저의 읽지 않은 알람을 조회한다.")
    @Test
    void getAlarms() {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User u2 = User.create("1234", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User user1 = users.get(0);
        User user2 = users.get(1);

        FriendRequest fr = FriendRequest.create(user1, user2);
        friendRequestRepository.save(fr);

        Schedule s = Schedule.create(user1, "일정");
        Schedule schedule = scheduleRepository.save(s);

        FriendRequestAlarm a1 = FriendRequestAlarm.create(user1, "알람 테스트", AlarmType.FRIEND_REQUEST,
            fr);
        FriendRequestAlarm a2 = FriendRequestAlarm.create(user1, "알람 테스트", AlarmType.FRIEND_ACCEPT,
            fr);
        ScheduleAlarm a3 = ScheduleAlarm.create(user1, "알람 테스트", AlarmType.SCHEDULE_ASSIGNED,
            schedule);
        ScheduleAlarm a4 = ScheduleAlarm.create(user1, "알람 테스트", AlarmType.MENTIONED, schedule);
        ScheduleAlarm a5 = ScheduleAlarm.create(user1, "알람 테스트", AlarmType.SCHEDULE_UPDATE,
            schedule);
        ScheduleAlarm a6 = ScheduleAlarm.create(user1, "알람 테스트", AlarmType.SCHEDULE_NOTIFICATION,
            schedule);
        List<FriendRequestAlarm> friendRequestAlarms = friendRequestAlarmRepository.saveAll(
            List.of(a1, a2));
        List<ScheduleAlarm> scheduleAlarms = scheduleAlarmRepository.saveAll(
            List.of(a3, a4, a5, a6));
        List<Alarm> alarms = new ArrayList<>();
        alarms.addAll(friendRequestAlarms);
        alarms.addAll(scheduleAlarms);

        alarms.get(2).markAsRead();
        alarms.get(5).markAsRead();
        alarmRepository.saveAll(alarms);

        // when
        List<Alarm> result = alarmRepository.findAllByUserAndIsReadFalse(user1);

        // then
        assertThat(result).hasSize(4)
            .extracting("user.userSeq", "alarmType", "isRead")
            .containsExactlyInAnyOrder(
                tuple(user1.getUserSeq(), AlarmType.FRIEND_REQUEST, false),
                tuple(user1.getUserSeq(), AlarmType.FRIEND_ACCEPT, false),
                tuple(user1.getUserSeq(), AlarmType.MENTIONED, false),
                tuple(user1.getUserSeq(), AlarmType.SCHEDULE_UPDATE, false)
            );
    }

    @DisplayName("유저가 읽지 않은 알림 중 친구 신청 알림을 조회한다.")
    @Test
    void findFriendRequestAlarm() {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User u2 = User.create("2222", "유저2", "id2", "imageUrl2");
        User u3 = User.create("3333", "유저3", "id3", "imageUrl3");
        List<User> users = userRepository.saveAll(List.of(u1, u2, u3));
        User user1 = users.get(0);
        User user2 = users.get(1);
        User user3 = users.get(2);

        FriendRequest fr1 = FriendRequest.create(user1, user2);
        FriendRequest fr2 = FriendRequest.create(user1, user3);
        List<FriendRequest> friendRequests = friendRequestRepository.saveAll(List.of(fr1, fr2));
        FriendRequest friendRequest1 = friendRequests.get(0);
        FriendRequest friendRequest2 = friendRequests.get(1);

        String content1 = AlarmContent.FRIEND_REQUEST.getMessage(user2.getUserName());
        String content2 = AlarmContent.FRIEND_REQUEST.getMessage(user3.getUserName());

        FriendRequestAlarm a1 = FriendRequestAlarm.create(user1, content1, AlarmType.FRIEND_REQUEST,
            friendRequest1);
        FriendRequestAlarm a2 = FriendRequestAlarm.create(user1, content2, AlarmType.FRIEND_REQUEST,
            friendRequest2);
        friendRequestAlarmRepository.saveAll(List.of(a1, a2));

        Alarm alarm1 = alarmRepository.findAll().get(0);
        alarm1.markAsRead();
        alarmRepository.save(alarm1);

        // when
        Optional<Alarm> result = alarmRepository.findFriendRequestAlarm(user1,
            AlarmType.FRIEND_REQUEST, u3.getUserName());

        // then
        assertTrue(result.isPresent());
        assertThat(result.get())
            .extracting("user.userSeq", "content", "isRead", "alarmType")
            .containsExactly(user1.getUserSeq(), content2, false, AlarmType.FRIEND_REQUEST);
    }
}
