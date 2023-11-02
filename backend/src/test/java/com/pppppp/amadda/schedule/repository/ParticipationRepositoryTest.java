package com.pppppp.amadda.schedule.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.schedule.entity.Participation;
import com.pppppp.amadda.schedule.entity.Schedule;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ParticipationRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ParticipationRepository participationRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user1 = User.create(1L, "박동건", "icebearrrr", "url1");
        User user2 = User.create(2L, "정민영", "minyoung", "url2");
        userRepository.saveAll(List.of(user1, user2));

        Schedule schedule1 = Schedule.builder()
            .user(user1)
            .isTimeSelected(false)
            .isDateSelected(false)
            .build();
        Schedule schedule2 = Schedule.builder()
            .user(user2)
            .isTimeSelected(false)
            .isDateSelected(false)
            .build();
        Schedule schedule3 = Schedule.builder()
            .user(user1)
            .isTimeSelected(false)
            .isDateSelected(false)
            .build();
        scheduleRepository.saveAll(List.of(schedule1, schedule2, schedule3));
    }

    @AfterEach
    void tearDown() {
        participationRepository.deleteAllInBatch();
        scheduleRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    // ====================================== Create Test ====================================== //

    @DisplayName("새로운 참석 정보를 생성한다.")
    @Transactional
    @Test
    void createParticipationInfo() {
        // given
        User user1 = userRepository.findAll().get(0);
        User user2 = userRepository.findAll().get(1);
        Schedule schedule = scheduleRepository.findAll().get(0);

        Participation participation1 = Participation.builder()
            .user(user1)
            .schedule(schedule)
            .scheduleName("싸피")
            .scheduleMemo("가기 싫다")
            .build();
        Participation participation2 = Participation.builder()
            .user(user2)
            .schedule(schedule)
            .scheduleName("싸피")
            .scheduleMemo("좋아")
            .build();

        // when
        participationRepository.saveAll(List.of(participation1, participation2));
        List<Participation> participations = participationRepository.findAll();

        // then
        assertThat(participations).hasSize(2)
            .extracting("user", "schedule", "scheduleName", "scheduleMemo")
            .containsExactlyInAnyOrder(
                tuple(user1, schedule, "싸피", "가기 싫다"),
                tuple(user2, schedule, "싸피", "좋아")
            );
    }

    // ====================================== Read Test ====================================== //

    @DisplayName("해당하는 유저의 일정에 대한 참석정보를 가져온다.")
    @Transactional
    @Test
    void findParticipationInfo() {
        // given
        User user1 = userRepository.findAll().get(0);
        User user2 = userRepository.findAll().get(1);

        Schedule schedule1 = scheduleRepository.findAll().get(0);
        Schedule schedule2 = scheduleRepository.findAll().get(1);

        Participation p1 = Participation.builder()
            .user(user1)
            .schedule(schedule1)
            .scheduleName("싸피")
            .scheduleMemo("가기 싫다")
            .build();
        Participation p2 = Participation.builder()
            .user(user2)
            .schedule(schedule2)
            .scheduleName("싸피")
            .scheduleMemo("좋아")
            .build();
        participationRepository.saveAll(List.of(p1, p2));

        // when
        Optional<Participation> participation1 = participationRepository.findBySchedule_ScheduleSeqAndUser_UserSeqAndIsDeletedFalse(
            schedule1.getScheduleSeq(), user1.getUserSeq());
        Optional<Participation> participation2 = participationRepository.findBySchedule_ScheduleSeqAndUser_UserSeqAndIsDeletedFalse(
            schedule2.getScheduleSeq(), user2.getUserSeq());

        // then
        assertThat(participation1).isPresent()
            .get()
            .extracting("user", "schedule", "scheduleName", "scheduleMemo")
            .containsExactly(user1, schedule1, "싸피", "가기 싫다");
        assertThat(participation2).isPresent()
            .get()
            .extracting("user", "schedule", "scheduleName", "scheduleMemo")
            .containsExactly(user2, schedule2, "싸피", "좋아");
    }
}