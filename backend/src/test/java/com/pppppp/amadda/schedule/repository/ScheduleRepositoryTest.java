package com.pppppp.amadda.schedule.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.schedule.entity.Schedule;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ScheduleRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = User.create("1L", "박동건", "icebearrrr", "url1");
        User user2 = User.create("2L", "정민영", "minyoung", "url2");
        userRepository.saveAll(List.of(user, user2));
    }

    @AfterEach
    void tearDown() {
        scheduleRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    // ====================================== Create Test ====================================== //

    @DisplayName("시간, 일자 미확정인 새로운 일정을 생성한다.")
    @Test
    void createNewScheduleUnfixed() {
        // given
        User user = userRepository.findAll().get(0);
        Schedule schedule = Schedule.builder()
            .authorizedUser(user)
            .isTimeSelected(false)
            .isDateSelected(false)
            .build();

        // when
        scheduleRepository.save(schedule);
        Schedule savedSchedule = scheduleRepository.findAll().get(0);

        // then
        assertThat(savedSchedule.isTimeSelected()).isFalse();
        assertThat(savedSchedule.isDateSelected()).isFalse();
    }

    @DisplayName("시간만 미확정인 새로운 일정을 생성한다.")
    @Test
    void createNewScheduleTimeUnfixed() {
        // given
        User user = userRepository.findAll().get(0);
        LocalDateTime startAt = LocalDateTime.of(1998, 7, 27, 14, 0, 0);
        Schedule schedule = Schedule.builder()
            .authorizedUser(user)
            .isTimeSelected(false)
            .isDateSelected(true)
            .scheduleStartAt(startAt)
            .build();

        // when
        scheduleRepository.save(schedule);
        Schedule savedSchedule = scheduleRepository.findAll().get(0);

        // then
        assertThat(savedSchedule.isTimeSelected()).isFalse();
        assertThat(savedSchedule.isDateSelected()).isTrue();
        assertEquals(startAt, savedSchedule.getScheduleStartAt());
        assertThat(savedSchedule.getScheduleEndAt()).isNull();
    }

    @DisplayName("시간이 확정된 새로운 일정을 생성한다.")
    @Test
    void createNewScheduleFixed() {
        // given
        User user = userRepository.findAll().get(0);
        LocalDateTime startAt = LocalDateTime.of(1998, 7, 27, 14, 0, 0);
        LocalDateTime endAt = startAt.plusHours(2L);
        Schedule schedule = Schedule.builder()
            .authorizedUser(user)
            .isTimeSelected(true)
            .isDateSelected(true)
            .scheduleStartAt(startAt)
            .scheduleEndAt(endAt)
            .build();

        // when
        scheduleRepository.save(schedule);
        Schedule savedSchedule = scheduleRepository.findAll().get(0);

        // then
        assertThat(savedSchedule)
            .extracting("isTimeSelected", "isDateSelected")
            .contains(true, true);
        assertThat(savedSchedule.getScheduleStartAt()).isEqualTo(startAt);
        assertThat(savedSchedule.getScheduleEndAt()).isEqualTo(endAt);
    }

    @DisplayName("'하루종일'로 설정된 일정을 생성한다.")
    @Test
    void createNewAllDaySchedule() {
        // given
        User user = userRepository.findAll().get(0);
        Schedule schedule = Schedule.builder()
            .authorizedUser(user)
            .isTimeSelected(false)
            .isDateSelected(true)
            .isAllDay(true)
            .build();

        // when
        scheduleRepository.save(schedule);
        Schedule savedSchedule = scheduleRepository.findAll().get(0);

        // then
        assertThat(savedSchedule)
            .extracting("isTimeSelected", "isDateSelected", "isAllDay")
            .contains(false, true, true);
        assertThat(savedSchedule.getScheduleStartAt()).isNull();
        assertThat(savedSchedule.getScheduleEndAt()).isNull();
    }

    // ====================================== Read Test ====================================== //

    @DisplayName("해당 사용자의 삭제되지 않은 모든 일정을 가져온다.")
    @Transactional
    @Test
    void getAllSchedule() {
        // given
        User user = userRepository.findAll().get(0);
        User user2 = userRepository.findAll().get(1);

        Schedule schedule1 = Schedule.builder()
            .authorizedUser(user)
            .isTimeSelected(false)
            .isDateSelected(false)
            .build();
        Schedule schedule2 = Schedule.builder()
            .authorizedUser(user2)
            .isTimeSelected(false)
            .isDateSelected(false)
            .build();
        Schedule schedule3 = Schedule.builder()
            .authorizedUser(user)
            .isTimeSelected(false)
            .isDateSelected(false)
            .build();
        scheduleRepository.saveAll(List.of(schedule1, schedule2, schedule3));

        // when
        List<Schedule> schedules = scheduleRepository.findAllByAuthorizedUser_UserSeqAndIsDeletedFalse(
            user.getUserSeq());

        // then
        assertThat(schedules).hasSize(2)
            .extracting("authorizedUser")
            .contains(user);
    }

    @DisplayName("특정 시점 이전의 모든 일정을 가져온다.")
    @Transactional
    @Test
    void getAllScheduleBeforeDate() {
        // given
        User user = userRepository.findAll().get(0);

        // when
        Schedule schedule1 = Schedule.builder()
            .authorizedUser(user)
            .isTimeSelected(false)
            .isDateSelected(false)
            .scheduleStartAt(LocalDateTime.of(2022, 7, 27, 14, 0, 0))
            .build();
        Schedule schedule2 = Schedule.builder()
            .authorizedUser(user)
            .isTimeSelected(false)
            .isDateSelected(false)
            .scheduleStartAt(LocalDateTime.of(2023, 7, 28, 14, 0, 0))
            .build();
        Schedule schedule3 = Schedule.builder()
            .authorizedUser(user)
            .isTimeSelected(false)
            .isDateSelected(false)
            .scheduleStartAt(LocalDateTime.of(2023, 10, 29, 14, 0, 0))
            .build();
        scheduleRepository.saveAll(List.of(schedule1, schedule2, schedule3));

        LocalDateTime pivot = LocalDateTime.of(2023, 7, 28, 14, 0, 0);
        int pivotYear = pivot.getYear();
        int pivotMonth = pivot.getMonthValue();
        int pivotDay = pivot.getDayOfMonth();
        int pivotHour = pivot.getHour();
        int pivotMinute = pivot.getMinute();

        List<Schedule> schedules = scheduleRepository.findAllByAuthorizedUser_UserSeqAndScheduleStartAtBeforeAndIsDeletedFalse(
            user.getUserSeq(), pivot);

        // then
        assertThat(schedules).hasSize(1)
            .extracting("authorizedUser").contains(user);
        schedules.forEach(schedule -> {
            LocalDateTime startAt = schedule.getScheduleStartAt();

            // java의 조건문 단축 평가 방식을 활용해서 검증
            assertTrue(startAt.getYear() < pivotYear
                || startAt.getMonthValue() < pivotMonth
                || startAt.getDayOfMonth() < pivotDay
                || startAt.getHour() < pivotHour
                || startAt.getMinute() < pivotMinute);
        });
    }

    @DisplayName("특정 시점 이후의 모든 일정을 가져온다.")
    @Transactional
    @Test
    void getAllScheduleAfterDate() {
        // given
        User user = userRepository.findAll().get(0);

        // when
        Schedule schedule1 = Schedule.builder()
            .authorizedUser(user)
            .isTimeSelected(false)
            .isDateSelected(false)
            .scheduleStartAt(LocalDateTime.of(2022, 7, 27, 14, 0, 0))
            .build();
        Schedule schedule2 = Schedule.builder()
            .authorizedUser(user)
            .isTimeSelected(false)
            .isDateSelected(false)
            .scheduleStartAt(LocalDateTime.of(2023, 7, 28, 14, 0, 0))
            .build();
        Schedule schedule3 = Schedule.builder()
            .authorizedUser(user)
            .isTimeSelected(false)
            .isDateSelected(false)
            .scheduleStartAt(LocalDateTime.of(2023, 10, 29, 14, 0, 0))
            .build();
        scheduleRepository.saveAll(List.of(schedule1, schedule2, schedule3));

        LocalDateTime pivot = LocalDateTime.of(2023, 7, 28, 14, 0, 0);
        int pivotYear = pivot.getYear();
        int pivotMonth = pivot.getMonthValue();
        int pivotDay = pivot.getDayOfMonth();
        int pivotHour = pivot.getHour();
        int pivotMinute = pivot.getMinute();

        List<Schedule> schedules = scheduleRepository.findAllByAuthorizedUser_UserSeqAndScheduleStartAtAfterAndIsDeletedFalse(
            user.getUserSeq(), pivot);

        // then
        assertThat(schedules).hasSize(1)
            .extracting("authorizedUser")
            .contains(user);

        schedules.forEach(schedule -> {
            LocalDateTime startAt = schedule.getScheduleStartAt();

            // java의 조건문 단축 평가 방식을 활용해서 검증
            assertTrue(startAt.getYear() > pivotYear
                || startAt.getMonthValue() > pivotMonth
                || startAt.getDayOfMonth() > pivotDay
                || startAt.getHour() > pivotHour
                || startAt.getMinute() > pivotMinute);
        });
    }
}
