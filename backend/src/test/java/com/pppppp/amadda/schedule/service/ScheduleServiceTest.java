package com.pppppp.amadda.schedule.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.friend.entity.Friend;
import com.pppppp.amadda.friend.repository.FriendRepository;
import com.pppppp.amadda.alarm.repository.AlarmRepository;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.schedule.dto.request.CategoryCreateRequest;
import com.pppppp.amadda.schedule.dto.request.CategoryPatchRequest;
import com.pppppp.amadda.schedule.dto.request.CommentCreateRequest;
import com.pppppp.amadda.schedule.dto.request.ScheduleCreateRequest;
import com.pppppp.amadda.schedule.dto.request.SchedulePatchRequest;
import com.pppppp.amadda.schedule.dto.response.CategoryReadResponse;
import com.pppppp.amadda.schedule.dto.response.CommentReadResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleCreateResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleDetailReadResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleListReadResponse;
import com.pppppp.amadda.schedule.entity.AlarmTime;
import com.pppppp.amadda.schedule.entity.Category;
import com.pppppp.amadda.schedule.entity.CategoryColor;
import com.pppppp.amadda.schedule.entity.Comment;
import com.pppppp.amadda.schedule.entity.Participation;
import com.pppppp.amadda.schedule.entity.Schedule;
import com.pppppp.amadda.schedule.repository.CategoryRepository;
import com.pppppp.amadda.schedule.repository.CommentRepository;
import com.pppppp.amadda.schedule.repository.ParticipationRepository;
import com.pppppp.amadda.schedule.repository.ScheduleRepository;
import com.pppppp.amadda.user.dto.response.UserReadResponse;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ScheduleServiceTest extends IntegrationTestSupport {
    // TODO: 일정 수정 알림 service 테스트

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ParticipationRepository participationRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    private AlarmRepository alarmRepository;

    @BeforeEach
    void setUp() {
        User user1 = User.create(1L, "박동건", "icebearrrr", "url1");
        User user2 = User.create(2L, "정민영", "minyoung", "url2");
        User user3 = User.create(3L, "김민정", "mindy0414", "url3");
        userRepository.saveAll(List.of(user1, user2, user3));

        friendRepository.save(Friend.create(user1, user2));
        friendRepository.save(Friend.create(user2, user1));
        friendRepository.save(Friend.create(user2, user3));
        friendRepository.save(Friend.create(user3, user2));
    }

    @AfterEach
    void tearDown() {
        alarmRepository.deleteAllInBatch();
        participationRepository.deleteAllInBatch();
        categoryRepository.deleteAllInBatch();
        commentRepository.deleteAllInBatch();
        scheduleRepository.deleteAllInBatch();
        friendRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    // =================== 일정 생성 ===================

    @DisplayName("시간, 날짜가 모두 미확정된 새로운 일정을 생성하고, 사용자의 개인화 된 설정이 반영 되었는지 확인한다.")
    @Test
    void createUnfixedScheduleAndParticipation() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u2 = userRepository.findAll().get(1);
        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .scheduleContent("여기는 동기화 되는 메모야")
            .scheduleMemo("이거는 안되는 메모고")
            .isDateSelected(false)
            .isTimeSelected(false)
            .isAllDay(false)
            .alarmTime(AlarmTime.NONE)
            .isAuthorizedAll(false)
            .participants(List.of(
                UserReadResponse.of(u1), UserReadResponse.of(u2)))
            .build();

        // when
        ScheduleCreateResponse response = scheduleService.createSchedule(u1.getUserSeq(), request);

        // then
        assertThat(response).isNotNull()
            .extracting("scheduleName", "scheduleMemo", "scheduleContent",
                "isDateSelected", "isTimeSelected", "isAllDay", "scheduleStartAt", "scheduleEndAt",
                "alarmTime", "isAuthorizedAll")
            .containsExactly(
                "안녕 내가 일정 이름이야", "이거는 안되는 메모고", "여기는 동기화 되는 메모야", false, false, false,
                "null", "null", AlarmTime.NONE, false);

        List<UserReadResponse> participants = response.participants();
        assertThat(participants)
            .hasSize(2)
            .extracting("userId", "userName", "imageUrl")
            .containsExactlyInAnyOrder(
                tuple("icebearrrr", "박동건", "url1"),
                tuple("minyoung", "정민영", "url2"));
    }

    @DisplayName("시간이 미확정된 새로운 일정을 생성하고, 사용자의 개인화 된 설정이 반영 되었는지 확인한다.")
    @Test
    void createTimeUnfixedScheduleAndParticipation() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u2 = userRepository.findAll().get(1);
        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .scheduleContent("여기는 동기화 되는 메모야")
            .scheduleMemo("이거는 안되는 메모고")
            .isDateSelected(true)
            .isTimeSelected(false)
            .isAllDay(false)
            .scheduleStartAt("2023-11-02 00:00:00")
            .scheduleEndAt("")
            .alarmTime(AlarmTime.NONE)
            .isAuthorizedAll(false)
            .participants(List.of(
                UserReadResponse.of(u1), UserReadResponse.of(u2)))
            .build();

        // when
        ScheduleCreateResponse response = scheduleService.createSchedule(u1.getUserSeq(), request);

        // then
        assertThat(response).isNotNull()
            .extracting("scheduleName", "scheduleMemo", "scheduleContent",
                "isDateSelected", "isTimeSelected", "isAllDay", "scheduleStartAt", "scheduleEndAt",
                "alarmTime", "isAuthorizedAll")
            .containsExactly(
                "안녕 내가 일정 이름이야", "이거는 안되는 메모고", "여기는 동기화 되는 메모야", true, false, false,
                "2023-11-02T00:00", "null", AlarmTime.NONE, false);

        List<UserReadResponse> participants = response.participants();
        assertThat(participants)
            .hasSize(2)
            .extracting("userId", "userName", "imageUrl")
            .containsExactlyInAnyOrder(
                tuple("icebearrrr", "박동건", "url1"),
                tuple("minyoung", "정민영", "url2"));
    }


    @DisplayName("일정이 확정된 새로운 일정을 생성하고, 사용자의 개인화 된 설정이 반영 되었는지 확인한다.")
    @Test
    void createFixedScheduleAndParticipation() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u2 = userRepository.findAll().get(1);
        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .scheduleContent("여기는 동기화 되는 메모야")
            .scheduleMemo("이거는 안되는 메모고")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-11-01 09:00:00")
            .scheduleEndAt("2023-11-01 18:00:00")
            .alarmTime(AlarmTime.NONE)
            .isAuthorizedAll(false)
            .participants(List.of(
                UserReadResponse.of(u1), UserReadResponse.of(u2)))
            .build();

        // when
        ScheduleCreateResponse response = scheduleService.createSchedule(u1.getUserSeq(), request);

        // then
        assertThat(response).isNotNull()
            .extracting("scheduleName", "scheduleMemo", "scheduleContent",
                "isDateSelected", "isTimeSelected", "isAllDay", "scheduleStartAt", "scheduleEndAt",
                "alarmTime", "isAuthorizedAll")
            .containsExactly(
                "안녕 내가 일정 이름이야", "이거는 안되는 메모고", "여기는 동기화 되는 메모야", true, true, false,
                "2023-11-01T09:00", "2023-11-01T18:00", AlarmTime.NONE, false);

        List<UserReadResponse> participants = response.participants();
        assertThat(participants)
            .hasSize(2)
            .extracting("userId", "userName", "imageUrl")
            .containsExactlyInAnyOrder(
                tuple("icebearrrr", "박동건", "url1"),
                tuple("minyoung", "정민영", "url2"));
    }

    @DisplayName("'하루종일'로 체크된 새로운 일정을 생성하고, 사용자의 개인화 된 설정이 반영 되었는지 확인한다.")
    @Test
    void createAllDayScheduleAndParticipation() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u2 = userRepository.findAll().get(1);
        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .scheduleContent("여기는 동기화 되는 메모야")
            .scheduleMemo("이거는 안되는 메모고")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(true)
            .scheduleStartAt("2023-11-01 00:00:00")
            .scheduleEndAt("2023-11-01 23:59:59")
            .alarmTime(AlarmTime.NONE)
            .isAuthorizedAll(false)
            .participants(List.of(
                UserReadResponse.of(u1), UserReadResponse.of(u2)))
            .build();

        // when
        ScheduleCreateResponse response = scheduleService.createSchedule(u1.getUserSeq(), request);

        // then
        assertThat(response).isNotNull()
            .extracting("scheduleName", "scheduleMemo", "scheduleContent",
                "isDateSelected", "isTimeSelected", "isAllDay", "scheduleStartAt", "scheduleEndAt",
                "alarmTime", "isAuthorizedAll")
            .containsExactly(
                "안녕 내가 일정 이름이야", "이거는 안되는 메모고", "여기는 동기화 되는 메모야", true, true, true,
                "2023-11-01T00:00", "2023-11-01T23:59:59", AlarmTime.NONE, false);

        List<UserReadResponse> participants = response.participants();
        assertThat(participants)
            .hasSize(2)
            .extracting("userId", "userName", "imageUrl")
            .containsExactlyInAnyOrder(
                tuple("icebearrrr", "박동건", "url1"),
                tuple("minyoung", "정민영", "url2"));
    }

    // =================== 일정 조회 ===================

    @DisplayName("서버 시간을 조회한다.")
    @Test
    void getServerTime() {
        // given
        String expectedFormat = "yyyy-MM-dd HH:mm:ss";

        // when
        String serverTime = scheduleService.getServerTime();

        // then
        assertThat(serverTime).isNotNull();
        assertTrue(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat(expectedFormat);
            sdf.setLenient(false); // 엄격한 모드로 설정

            try {
                sdf.parse(serverTime);
                return true; // 예외가 발생하지 않으면 형식이 일치함
            } catch (ParseException e) {
                return false; // ParseException이 발생하면 형식이 일치하지 않음
            }
        });
    }

    @DisplayName("사용자의 일정 목록을 조회한다.")
    @Transactional
    @Test
    void getSchedules() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u2 = userRepository.findAll().get(1);
        ScheduleCreateRequest r1 = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .isTimeSelected(false)
            .isDateSelected(false)
            .isAllDay(false)
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.NONE)
            .participants(List.of(
                UserReadResponse.of(u1), UserReadResponse.of(u2)))
            .build();
        ScheduleCreateRequest r2 = ScheduleCreateRequest.builder()
            .scheduleName("나도 일정이야")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-11-01 08:59:30")
            .scheduleEndAt("2023-11-01 09:00:00")
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.ON_TIME)
            .participants(List.of(UserReadResponse.of(u2)))
            .build();
        scheduleService.createSchedule(u1.getUserSeq(), r1);
        scheduleService.createSchedule(u2.getUserSeq(), r2);

        // when
        Map<String, String> searchCondition = Map.of("categories", "", "searchKey", "",
            "unscheduled", "", "month", "", "day", "", "year", "");
        List<ScheduleListReadResponse> user1Schedules = scheduleService.getScheduleListBySearchCondition(
            u1.getUserSeq(), searchCondition);
        List<ScheduleListReadResponse> user2Schedules = scheduleService.getScheduleListBySearchCondition(
            u2.getUserSeq(), searchCondition);

        // then
        assertThat(user1Schedules)
            .hasSize(1)
            .extracting("scheduleName", "isAuthorizedAll",
                "alarmTime")
            .containsExactly(tuple("안녕 내가 일정 이름이야", false,
                AlarmTime.NONE.getContent()));
        assertThat(user2Schedules)
            .hasSize(2)
            .extracting("scheduleName",
                "isDateSelected", "isTimeSelected", "isAllDay", "scheduleStartAt", "scheduleEndAt",
                "alarmTime", "isAuthorizedAll")
            .containsExactlyInAnyOrder(
                tuple("안녕 내가 일정 이름이야", false, false, false,
                    "", "", AlarmTime.NONE.getContent(), false),
                tuple("나도 일정이야", true, true, false,
                    "2023-11-01 08:59:30", "2023-11-01 09:00:00", AlarmTime.ON_TIME.getContent(),
                    false));
    }

    @DisplayName("일정의 상세 정보를 불러온다.")
    @Transactional
    @Test
    void getScheduleDetail() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u2 = userRepository.findAll().get(1);
        ScheduleCreateRequest r1 = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .scheduleContent("여기는 동기화되는 메모야")
            .scheduleMemo("이거는 안되는 메모고")
            .isTimeSelected(false)
            .isDateSelected(false)
            .isAllDay(false)
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.NONE)
            .participants(List.of(
                UserReadResponse.of(u1), UserReadResponse.of(u2)))
            .build();
        scheduleService.createSchedule(u1.getUserSeq(), r1);
        Schedule schedule = scheduleRepository.findAll().get(0);

        // when
        ScheduleDetailReadResponse u1Response = scheduleService.getScheduleDetail(
            schedule.getScheduleSeq(), u1.getUserSeq());
        ScheduleDetailReadResponse u2Response = scheduleService.getScheduleDetail(
            schedule.getScheduleSeq(), u2.getUserSeq());

        // then
        assertThat(u1Response).isNotNull();
        assertThat(u2Response).isNotNull();
        assertEquals(u1Response.scheduleSeq(), u2Response.scheduleSeq());
    }

    @DisplayName("카테고리 seq로 해당하는 일정 목록을 조회한다.")
    @Transactional
    @Test
    void getSchedulesByCategories() {
        // given
        User u1 = userRepository.findAll().get(0);

        Category c1 = Category.builder()
            .categoryName("자율기절")
            .categoryColor(CategoryColor.valueOf("HOTPINK"))
            .user(u1)
            .build();
        Category c2 = Category.builder()
            .categoryName("합창단")
            .categoryColor(CategoryColor.valueOf("GREEN"))
            .user(u1)
            .build();
        List<Category> categories = categoryRepository.saveAll(List.of(c1, c2));

        ScheduleCreateRequest r1 = ScheduleCreateRequest.builder()
            .scheduleName("합창단 가을 공연")
            .scheduleContent("수원 시민회관")
            .scheduleMemo("미리 가서 꽃 사놓기")
            .isDateSelected(false)
            .isTimeSelected(false)
            .isAllDay(false)
            .alarmTime(AlarmTime.NONE)
            .isAuthorizedAll(false)
            .participants(List.of(
                UserReadResponse.of(u1)))
            .categorySeq(categories.get(0).getCategorySeq())
            .build();
        ScheduleCreateRequest r2 = ScheduleCreateRequest.builder()
            .scheduleName("프로젝트 발표")
            .scheduleContent("멀티캠퍼스 역삼")
            .scheduleMemo("미리 연습해보기")
            .isDateSelected(false)
            .isTimeSelected(false)
            .isAllDay(false)
            .alarmTime(AlarmTime.NONE)
            .isAuthorizedAll(false)
            .participants(List.of(
                UserReadResponse.of(u1)))
            .categorySeq(categories.get(1).getCategorySeq())
            .build();
        scheduleService.createSchedule(u1.getUserSeq(), r1);
        scheduleService.createSchedule(u1.getUserSeq(), r2);

        // when
        Map<String, String> searchCondition1 = Map.of("categories",
            categories.get(0).getCategorySeq() + "," + categories.get(1).getCategorySeq(),
            "searchKey", "", "unscheduled", "", "month", "", "day", "", "year", "");

        Map<String, String> searchCondition2 = Map.of("categories",
            String.valueOf(categories.get(0).getCategorySeq()), "searchKey", "", "unscheduled", "",
            "month", "", "day", "", "year", "");

        List<ScheduleListReadResponse> result1 = scheduleService.getScheduleListBySearchCondition(
            u1.getUserSeq(), searchCondition1);
        List<ScheduleListReadResponse> result2 = scheduleService.getScheduleListBySearchCondition(
            u1.getUserSeq(), searchCondition2);

        // then
        assertThat(result1)
            .hasSize(2)
            .extracting("scheduleName", "participants", "authorizedUser",
                "isDateSelected", "isTimeSelected", "isAllDay", "scheduleStartAt", "scheduleEndAt",
                "alarmTime", "isAuthorizedAll", "category")
            .containsExactlyInAnyOrder(
                tuple("합창단 가을 공연",
                    List.of(UserReadResponse.of(u1)), UserReadResponse.of(u1), false, false, false,
                    "", "", AlarmTime.NONE.getContent(), false, CategoryReadResponse.of(c1)),
                tuple("프로젝트 발표",
                    List.of(UserReadResponse.of(u1)), UserReadResponse.of(u1), false, false, false,
                    "", "", AlarmTime.NONE.getContent(), false, CategoryReadResponse.of(c2))
            );
        assertThat(result2)
            .hasSize(1)
            .extracting("scheduleName", "participants", "authorizedUser",
                "isDateSelected", "isTimeSelected", "isAllDay", "scheduleStartAt", "scheduleEndAt",
                "alarmTime", "isAuthorizedAll", "category")
            .containsExactlyInAnyOrder(
                tuple("합창단 가을 공연",
                    List.of(UserReadResponse.of(u1)), UserReadResponse.of(u1), false, false, false,
                    "", "", AlarmTime.NONE.getContent(), false, CategoryReadResponse.of(c1))
            );
    }

    @DisplayName("일정 이름으로 일정을 검색한다.")
    @Transactional
    @Test
    void getSchedulesByScheduleName() {
        // given
        User u1 = userRepository.findAll().get(0);

        ScheduleCreateRequest r1 = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .isTimeSelected(false)
            .isDateSelected(false)
            .isAllDay(false)
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.NONE)
            .participants(List.of(
                UserReadResponse.of(u1)))
            .build();
        ScheduleCreateRequest r2 = ScheduleCreateRequest.builder()
            .scheduleName("나도 일정이야")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-11-01 08:59:30")
            .scheduleEndAt("2023-11-01 09:00:00")
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.ON_TIME)
            .participants(List.of(UserReadResponse.of(u1)))
            .build();
        scheduleService.createSchedule(u1.getUserSeq(), r1);
        scheduleService.createSchedule(u1.getUserSeq(), r2);

        // when
        Map<String, String> searchCondition = Map.of("searchKey", "일정", "unscheduled", "",
            "categories", "", "month", "", "day", "", "year", "");
        List<ScheduleListReadResponse> schedules = scheduleService.getScheduleListBySearchCondition(
            u1.getUserSeq(), searchCondition);

        // then
        assertThat(schedules)
            .hasSize(2)
            .extracting("scheduleName", "participants", "authorizedUser",
                "isDateSelected", "isTimeSelected", "isAllDay", "scheduleStartAt", "scheduleEndAt",
                "alarmTime", "isAuthorizedAll", "category")
            .containsExactlyInAnyOrder(
                tuple("안녕 내가 일정 이름이야",
                    List.of(UserReadResponse.of(u1)), UserReadResponse.of(u1), false, false, false,
                    "", "", AlarmTime.NONE.getContent(), false, null),
                tuple("나도 일정이야",
                    List.of(UserReadResponse.of(u1)), UserReadResponse.of(u1), true, true, false,
                    "2023-11-01 08:59:30", "2023-11-01 09:00:00", AlarmTime.ON_TIME.getContent(),
                    false, null)
            );
    }

    @DisplayName("사용자의 미확정인 일정 목록을 조회한다.")
    @Transactional
    @Test
    void getUnscheduledScheduleList() {
        // given
        User u1 = userRepository.findAll().get(0);

        ScheduleCreateRequest r1 = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .isTimeSelected(false)
            .isDateSelected(false)
            .isAllDay(false)
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.NONE)
            .participants(List.of(
                UserReadResponse.of(u1)))
            .build();
        ScheduleCreateRequest r2 = ScheduleCreateRequest.builder()
            .scheduleName("나도 일정이야")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-11-01 08:59:30")
            .scheduleEndAt("2023-11-01 09:00:00")
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.ON_TIME)
            .participants(List.of(UserReadResponse.of(u1)))
            .build();
        scheduleService.createSchedule(u1.getUserSeq(), r1);
        scheduleService.createSchedule(u1.getUserSeq(), r2);

        // when
        Map<String, String> searchCondition = Map.of("categories", "", "searchKey", "",
            "unscheduled", "true", "month", "", "day", "", "year", "");
        List<ScheduleListReadResponse> result = scheduleService.getScheduleListBySearchCondition(
            u1.getUserSeq(), searchCondition);

        // then
        assertThat(result)
            .hasSize(1)
            .extracting("scheduleName", "participants", "authorizedUser",
                "isDateSelected", "isTimeSelected", "isAllDay", "scheduleStartAt", "scheduleEndAt",
                "alarmTime", "isAuthorizedAll", "category")
            .containsExactlyInAnyOrder(
                tuple("안녕 내가 일정 이름이야",
                    List.of(UserReadResponse.of(u1)), UserReadResponse.of(u1), false, false, false,
                    "", "", AlarmTime.NONE.getContent(), false, null)
            );
    }

    @DisplayName("사용자의 이번달 일정 목록을 조회한다.")
    @Transactional
    @Test
    void getScheduleListByMonth() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u2 = userRepository.findAll().get(1);

        ScheduleCreateRequest r1 = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-10-01 08:59:30")
            .scheduleEndAt("2023-11-01 09:00:00")
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.NONE)
            .participants(List.of(
                UserReadResponse.of(u1)))
            .build();
        ScheduleCreateRequest r2 = ScheduleCreateRequest.builder()
            .scheduleName("나도 일정이야")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-11-01 08:59:30")
            .scheduleEndAt("2023-11-01 09:00:00")
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.ON_TIME)
            .participants(List.of(UserReadResponse.of(u1), UserReadResponse.of(u2)))
            .build();
        ScheduleCreateRequest r3 = ScheduleCreateRequest.builder()
            .scheduleName("나도 일정이야")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-10-30 08:59:30")
            .scheduleEndAt("2023-10-30 09:00:00")
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.ON_TIME)
            .participants(List.of(UserReadResponse.of(u1), UserReadResponse.of(u2)))
            .build();
        ScheduleCreateRequest r4 = ScheduleCreateRequest.builder()
            .scheduleName("나도 일정이야")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-11-30 08:59:30")
            .scheduleEndAt("2023-12-01 09:00:00")
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.ON_TIME)
            .participants(List.of(UserReadResponse.of(u1)))
            .build();
        ScheduleCreateRequest r5 = ScheduleCreateRequest.builder()
            .scheduleName("나도 일정이야")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2022-11-30 08:59:30")
            .scheduleEndAt("2022-12-01 09:00:00")
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.ON_TIME)
            .participants(List.of(UserReadResponse.of(u2)))
            .build();
        ScheduleCreateRequest r6 = ScheduleCreateRequest.builder()
            .scheduleName("나도 일정이야")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-10-30 08:59:30")
            .scheduleEndAt("2023-12-01 09:00:00")
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.ON_TIME)
            .participants(List.of(UserReadResponse.of(u1)))
            .build();
        scheduleService.createSchedule(u1.getUserSeq(), r1);
        scheduleService.createSchedule(u1.getUserSeq(), r2);
        scheduleService.createSchedule(u1.getUserSeq(), r3);
        scheduleService.createSchedule(u1.getUserSeq(), r4);
        scheduleService.createSchedule(u2.getUserSeq(), r5);
        scheduleService.createSchedule(u1.getUserSeq(), r6);

        // when
        Map<String, String> searchCondition = Map.of("categories", "", "searchKey", "",
            "unscheduled", "", "month", "11", "day", "", "year", "2023");
        List<ScheduleListReadResponse> result1 = scheduleService.getScheduleListBySearchCondition(
            u1.getUserSeq(), searchCondition);
        List<ScheduleListReadResponse> result2 = scheduleService.getScheduleListBySearchCondition(
            u2.getUserSeq(), searchCondition);

        // then
        assertThat(result1)
            .hasSize(4)
            .extracting("scheduleStartAt", "scheduleEndAt")
            .containsExactlyInAnyOrder(
                tuple("2023-10-01 08:59:30", "2023-11-01 09:00:00"),
                tuple("2023-11-01 08:59:30", "2023-11-01 09:00:00"),
                tuple("2023-11-30 08:59:30", "2023-12-01 09:00:00"),
                tuple("2023-10-30 08:59:30", "2023-12-01 09:00:00")
            );
        assertThat(result2)
            .hasSize(1)
            .extracting("scheduleStartAt", "scheduleEndAt")
            .containsExactlyInAnyOrder(
                tuple("2023-11-01 08:59:30", "2023-11-01 09:00:00")
            );
    }

    @DisplayName("사용자의 오늘 일정 목록을 조회한다.")
    @Transactional
    @Test
    void getScheduleListByDay() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u2 = userRepository.findAll().get(1);

        ScheduleCreateRequest r1 = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-10-01 08:59:30")
            .scheduleEndAt("2023-11-01 09:00:00")
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.NONE)
            .participants(List.of(
                UserReadResponse.of(u1)))
            .build();
        ScheduleCreateRequest r2 = ScheduleCreateRequest.builder()
            .scheduleName("나도 일정이야")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-11-01 08:59:30")
            .scheduleEndAt("2023-11-01 09:00:00")
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.ON_TIME)
            .participants(List.of(UserReadResponse.of(u1), UserReadResponse.of(u2)))
            .build();
        ScheduleCreateRequest r3 = ScheduleCreateRequest.builder()
            .scheduleName("나도 일정이야")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-10-30 08:59:30")
            .scheduleEndAt("2023-10-30 09:00:00")
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.ON_TIME)
            .participants(List.of(UserReadResponse.of(u1), UserReadResponse.of(u2)))
            .build();
        ScheduleCreateRequest r4 = ScheduleCreateRequest.builder()
            .scheduleName("나도 일정이야")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-11-30 08:59:30")
            .scheduleEndAt("2023-12-01 09:00:00")
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.ON_TIME)
            .participants(List.of(UserReadResponse.of(u1)))
            .build();
        ScheduleCreateRequest r5 = ScheduleCreateRequest.builder()
            .scheduleName("나도 일정이야")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2022-11-01 08:59:30")
            .scheduleEndAt("2022-12-01 09:00:00")
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.ON_TIME)
            .participants(List.of(UserReadResponse.of(u2)))
            .build();
        scheduleService.createSchedule(u1.getUserSeq(), r1);
        scheduleService.createSchedule(u1.getUserSeq(), r2);
        scheduleService.createSchedule(u1.getUserSeq(), r3);
        scheduleService.createSchedule(u1.getUserSeq(), r4);
        scheduleService.createSchedule(u2.getUserSeq(), r5);

        // when
        Map<String, String> searchCondition = Map.of("categories", "", "searchKey", "",
            "unscheduled", "", "month", "11", "day", "1", "year", "2023");
        List<ScheduleListReadResponse> result1 = scheduleService.getScheduleListBySearchCondition(
            u1.getUserSeq(), searchCondition);
        List<ScheduleListReadResponse> result2 = scheduleService.getScheduleListBySearchCondition(
            u2.getUserSeq(), searchCondition);

        // then
        assertThat(result1)
            .hasSize(2)
            .extracting("scheduleStartAt", "scheduleEndAt")
            .containsExactlyInAnyOrder(
                tuple("2023-10-01 08:59:30", "2023-11-01 09:00:00"),
                tuple("2023-11-01 08:59:30", "2023-11-01 09:00:00")
            );
        assertThat(result2)
            .hasSize(1)
            .extracting("scheduleStartAt", "scheduleEndAt")
            .containsExactlyInAnyOrder(
                tuple("2023-11-01 08:59:30", "2023-11-01 09:00:00")
            );
    }

    @DisplayName("일정 참가자 명단에서 이름으로 유저를 검색한다.")
    @Transactional
    @Test
    void getParticipatingUserListBySearchKey() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u2 = userRepository.findAll().get(1);
        User u3 = userRepository.findAll().get(2);

        friendRepository.save(Friend.create(u1, u3));
        friendRepository.save(Friend.create(u3, u1));

        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .scheduleContent("여기는 동기화 되는 메모야")
            .scheduleMemo("이거는 안되는 메모고")
            .isDateSelected(false)
            .isTimeSelected(false)
            .isAllDay(false)
            .alarmTime(AlarmTime.NONE)
            .isAuthorizedAll(false)
            .participants(List.of(
                UserReadResponse.of(u1), UserReadResponse.of(u2), UserReadResponse.of(u3)))
            .build();

        Long scheduleSeq = scheduleService.createSchedule(u1.getUserSeq(), request).scheduleSeq();

        // when
        List<UserReadResponse> result = scheduleService.getParticipatingUserListBySearchKey(
            scheduleSeq, "민");

        // then
        assertThat(result)
            .hasSize(2)
            .extracting("userId", "userName", "imageUrl")
            .containsExactlyInAnyOrder(
                tuple("minyoung", "정민영", "url2"),
                tuple("mindy0414", "김민정", "url3")
            );
    }

    @DisplayName("일정 정보를 수정한다. 이때 동기화되지 않는 부분에 대해서는 다른 참가자는 변경되지 않는다.")
    @Transactional
    @Test
    void updateSchedule() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u2 = userRepository.findAll().get(1);
        ScheduleCreateRequest scheduleCreateRequest = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .scheduleContent("여기는 동기화 되는 메모야")
            .scheduleMemo("이거는 안되는 메모고")
            .isDateSelected(false)
            .isTimeSelected(false)
            .isAllDay(false)
            .alarmTime(AlarmTime.NONE)
            .isAuthorizedAll(false)
            .participants(List.of(
                UserReadResponse.of(u1), UserReadResponse.of(u2)))
            .build();
        scheduleService.createSchedule(u1.getUserSeq(),
            scheduleCreateRequest);
        Long scheduleSeq = scheduleRepository.findAll().get(0).getScheduleSeq();

        SchedulePatchRequest schedulePatchRequest = SchedulePatchRequest.builder()
            .scheduleName("안녕 나는 바뀐 일정 이름이야")
            .scheduleContent("여기는 동기화 되는 메모야")
            .scheduleMemo("이거는 안되는 메모고")
            .isDateSelected(false)
            .isTimeSelected(false)
            .isAllDay(false)
            .alarmTime(AlarmTime.NONE)
            .participants(List.of(
                UserReadResponse.of(u1), UserReadResponse.of(u2)))
            .build();

        // when
        ScheduleDetailReadResponse response1 = scheduleService.updateSchedule(
            u1.getUserSeq(), scheduleSeq, schedulePatchRequest);
        ScheduleDetailReadResponse response2 = scheduleService.getScheduleDetail(scheduleSeq,
            u2.getUserSeq());

        // then
        assertThat(response1.scheduleSeq()).isEqualTo(response2.scheduleSeq());
        assertThat(response1)
            .extracting("scheduleName", "scheduleMemo", "scheduleContent",
                "isDateSelected", "isTimeSelected", "isAllDay", "scheduleStartAt", "scheduleEndAt",
                "alarmTime", "isAuthorizedAll")
            .containsExactly(
                "안녕 나는 바뀐 일정 이름이야", "이거는 안되는 메모고", "여기는 동기화 되는 메모야", false, false, false,
                "null", "null", "알림 없음", false);
        assertThat(response2)
            .extracting("scheduleName")
            .isEqualTo("안녕 내가 일정 이름이야");
    }

    @DisplayName("기존 일정에 새로운 참가자를 할당한다. 이때 초기 정보는 할당한 참가자의 정보를 따른다.")
    @Transactional
    @Test
    void addNewParticipants() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u2 = userRepository.findAll().get(1);
        User u3 = userRepository.findAll().get(2);
        ScheduleCreateRequest scheduleCreateRequest = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .scheduleContent("여기는 동기화 되는 메모야")
            .scheduleMemo("이거는 안되는 메모고")
            .isDateSelected(false)
            .isTimeSelected(false)
            .isAllDay(false)
            .alarmTime(AlarmTime.NONE)
            .isAuthorizedAll(true)
            .participants(List.of(
                UserReadResponse.of(u1), UserReadResponse.of(u2)))
            .build();
        scheduleService.createSchedule(u1.getUserSeq(),
            scheduleCreateRequest);
        Long scheduleSeq = scheduleRepository.findAll().get(0).getScheduleSeq();

        SchedulePatchRequest schedulePatchRequest = SchedulePatchRequest.builder()
            .scheduleName("안녕 나는 바뀐 일정 이름이야")
            .scheduleContent("여기는 동기화 되는 메모야")
            .scheduleMemo("이거는 안되는 메모고")
            .isDateSelected(false)
            .isTimeSelected(false)
            .isAllDay(false)
            .alarmTime(AlarmTime.NONE)
            .participants(List.of(
                UserReadResponse.of(u1), UserReadResponse.of(u2), UserReadResponse.of(u3)))
            .build();

        // when
        ScheduleDetailReadResponse response1 = scheduleService.updateSchedule(
            u2.getUserSeq(), scheduleSeq, schedulePatchRequest);
        ScheduleDetailReadResponse response2 = scheduleService.getScheduleDetail(scheduleSeq,
            u3.getUserSeq());

        // then
        assertThat(response2).isNotNull();
        assertThat(response1.scheduleSeq()).isEqualTo(response2.scheduleSeq());
        assertThat(response1)
            .extracting("scheduleName", "scheduleMemo", "scheduleContent",
                "isDateSelected", "isTimeSelected", "isAllDay", "scheduleStartAt", "scheduleEndAt",
                "alarmTime", "isAuthorizedAll")
            .containsExactly(
                "안녕 나는 바뀐 일정 이름이야", "이거는 안되는 메모고", "여기는 동기화 되는 메모야", false, false, false,
                "null", "null", "알림 없음", true);
        assertThat(response2)
            .extracting("scheduleName", "alarmTime")
            .containsExactly("안녕 나는 바뀐 일정 이름이야", "알림 없음");
    }

    @DisplayName("일정에서 참가자를 할당 취소하고 제외된 사람의 참가 정보는 삭제한다.")
    @Transactional
    @Test
    void deleteParticipation() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u2 = userRepository.findAll().get(1);
        User u3 = userRepository.findAll().get(2);

        friendRepository.save(Friend.create(u1, u3));
        friendRepository.save(Friend.create(u3, u1));

        ScheduleCreateRequest createRequest = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .scheduleContent("여기는 동기화 되는 메모야")
            .scheduleMemo("이거는 안되는 메모고")
            .isDateSelected(false)
            .isTimeSelected(false)
            .isAllDay(false)
            .alarmTime(AlarmTime.NONE)
            .isAuthorizedAll(false)
            .participants(List.of(
                UserReadResponse.of(u1), UserReadResponse.of(u2), UserReadResponse.of(u3)))
            .build();
        scheduleService.createSchedule(u1.getUserSeq(), createRequest);
        Schedule s = scheduleRepository.findAll().get(0);

        SchedulePatchRequest request = SchedulePatchRequest.builder()
            .scheduleName("안녕 나는 바뀐 일정 이름이야")
            .scheduleContent("여기는 동기화 되는 메모야")
            .scheduleMemo("이거는 안되는 메모고")
            .isDateSelected(false)
            .isTimeSelected(false)
            .isAllDay(false)
            .alarmTime(AlarmTime.NONE)
            .participants(List.of(
                UserReadResponse.of(u1), UserReadResponse.of(u3)))
            .build();

        // when
        scheduleService.updateSchedule(u1.getUserSeq(), s.getScheduleSeq(), request);
        List<Participation> result1 = participationRepository.findBySchedule_ScheduleSeqAndIsDeletedFalse(
            s.getScheduleSeq());
        Optional<Participation> result2 = participationRepository.findBySchedule_ScheduleSeqAndUser_UserSeqAndIsDeletedFalse(
            s.getScheduleSeq(), u2.getUserSeq());

        // then
        assertThat(result1)
            .hasSize(2)
            .extracting("user", "schedule.scheduleSeq")
            .containsExactlyInAnyOrder(
                tuple(u1, s.getScheduleSeq()),
                tuple(u3, s.getScheduleSeq())
            );
        assertThat(result2).isEmpty();
    }


    @DisplayName("카테고리에 새로운 일정을 추가한다.")
    @Transactional
    @Test
    void addScheduleToCategory() {
        // given
        User u1 = userRepository.findAll().get(0);

        ScheduleCreateRequest sr1 = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .isTimeSelected(false)
            .isDateSelected(false)
            .isAllDay(false)
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.NONE)
            .participants(List.of(
                UserReadResponse.of(u1)))
            .build();
        scheduleService.createSchedule(u1.getUserSeq(), sr1);
        Schedule schedule = scheduleRepository.findAll().get(0);

        CategoryCreateRequest r1 = CategoryCreateRequest.of("합창단", "GREEN");
        scheduleService.createCategory(u1.getUserSeq(), r1);
        Category category = categoryRepository.findAll().get(0);

        // when
        scheduleService.addScheduleToCategory(u1.getUserSeq(),
            schedule.getScheduleSeq(),
            category.getCategorySeq());
        Optional<Participation> result1 = participationRepository.findBySchedule_ScheduleSeqAndUser_UserSeqAndIsDeletedFalse(
            schedule.getScheduleSeq(), u1.getUserSeq());
        List<ScheduleListReadResponse> result2 = scheduleService.getScheduleListBySearchCondition(
            u1.getUserSeq(), Map.of("categories", String.valueOf(category.getCategorySeq()),
                "searchKey", "", "unscheduled", "", "month", "", "day", "", "year", ""));

        // then
        assertThat(result1).isPresent();
        assertThat(result1.get().getCategory()).isEqualTo(category);
        assertThat(result2).hasSize(1)
            .extracting("scheduleSeq")
            .contains(schedule.getScheduleSeq());
    }

    @DisplayName("카테고리에서 일정을 삭제한다.")
    @Transactional
    @Test
    void deleteScheduleFromCategory() {
        // given
        User u1 = userRepository.findAll().get(0);

        CategoryCreateRequest r1 = CategoryCreateRequest.of("합창단", "GREEN");
        scheduleService.createCategory(u1.getUserSeq(), r1);
        Category category = categoryRepository.findAll().get(0);

        ScheduleCreateRequest sr1 = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .isTimeSelected(false)
            .isDateSelected(false)
            .isAllDay(false)
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.NONE)
            .participants(List.of(
                UserReadResponse.of(u1)))
            .categorySeq(category.getCategorySeq())
            .build();
        ScheduleCreateRequest sr2 = ScheduleCreateRequest.builder()
            .scheduleName("나도 일정이야")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-11-01 08:59:30")
            .scheduleEndAt("2023-11-01 09:00:00")
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.ON_TIME)
            .participants(List.of(UserReadResponse.of(u1)))
            .categorySeq(category.getCategorySeq())
            .build();
        scheduleService.createSchedule(u1.getUserSeq(), sr1);
        scheduleService.createSchedule(u1.getUserSeq(), sr2);
        Schedule s1 = scheduleRepository.findAll().get(0);
        Schedule s2 = scheduleRepository.findAll().get(1);

        // when
        scheduleService.deleteScheduleFromCategory(u1.getUserSeq(), s1.getScheduleSeq(),
            category.getCategorySeq());
        List<ScheduleListReadResponse> result = scheduleService.getScheduleListBySearchCondition(
            u1.getUserSeq(), Map.of("categories", String.valueOf(category.getCategorySeq()),
                "searchKey", "", "unscheduled", "", "month", "", "day", "", "year", ""));

        // then
        assertThat(result).hasSize(1)
            .extracting("scheduleSeq", "category")
            .containsExactly(
                tuple(s2.getScheduleSeq(), CategoryReadResponse.of(category)));
    }

    @DisplayName("참가 정보를 삭제한다. 이때 나머지 인원의 참가 정보는 유지된다.")
    @Transactional
    @Test
    void deleteSelfParticipation() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u2 = userRepository.findAll().get(1);

        ScheduleCreateRequest createRequest = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .scheduleContent("여기는 동기화 되는 메모야")
            .scheduleMemo("이거는 안되는 메모고")
            .isDateSelected(false)
            .isTimeSelected(false)
            .isAllDay(false)
            .alarmTime(AlarmTime.NONE)
            .isAuthorizedAll(false)
            .participants(List.of(
                UserReadResponse.of(u1), UserReadResponse.of(u2)))
            .build();
        scheduleService.createSchedule(u1.getUserSeq(), createRequest);
        Schedule s = scheduleRepository.findAll().get(0);

        // when
        scheduleService.deleteParticipation(u2.getUserSeq(), s);
        List<Participation> result1 = participationRepository.findBySchedule_ScheduleSeqAndIsDeletedFalse(
            s.getScheduleSeq());
        Optional<Participation> result2 = participationRepository.findBySchedule_ScheduleSeqAndUser_UserSeqAndIsDeletedFalse(
            s.getScheduleSeq(), u2.getUserSeq());

        // then
        assertThat(result1)
            .hasSize(1)
            .extracting("user", "schedule.scheduleSeq")
            .containsExactlyInAnyOrder(
                tuple(u1, s.getScheduleSeq())
            );
        assertThat(result2).isEmpty();
    }

    @DisplayName("일정 자체를 삭제한다. 이때 일정에 할당된 참가자들의 참가 정보도 같이 사라진다.")
    @Test
    void deleteSchedule() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u2 = userRepository.findAll().get(1);

        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .scheduleContent("여기는 동기화 되는 메모야")
            .scheduleMemo("이거는 안되는 메모고")
            .isDateSelected(false)
            .isTimeSelected(false)
            .isAllDay(false)
            .alarmTime(AlarmTime.NONE)
            .isAuthorizedAll(false)
            .participants(List.of(
                UserReadResponse.of(u1), UserReadResponse.of(u2)))
            .build();
        scheduleService.createSchedule(u1.getUserSeq(), request);
        Schedule s = scheduleRepository.findAll().get(0);

        // when
        scheduleService.deleteSchedule(u1.getUserSeq(), s.getScheduleSeq());
        List<Participation> result = participationRepository.findBySchedule_ScheduleSeqAndIsDeletedFalse(
            s.getScheduleSeq());

        // then
        assertThat(result).hasSize(0);
    }

    // =================== 댓글 ===================

    @DisplayName("해당 일정에 댓글을 단다.")
    @Transactional
    @Test
    void createCommentOnSchedule() {
        // given
        User user = userRepository.findAll().get(0);

        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .scheduleContent("여기는 동기화 되는 메모야")
            .scheduleMemo("이거는 안되는 메모고")
            .isDateSelected(false)
            .isTimeSelected(false)
            .isAllDay(false)
            .alarmTime(AlarmTime.NONE)
            .isAuthorizedAll(false)
            .participants(List.of(
                UserReadResponse.of(user)))
            .build();
        scheduleService.createSchedule(user.getUserSeq(), request);

        Schedule s = scheduleRepository.findAll().get(0);

        // when
        CommentReadResponse response = scheduleService.createCommentOnSchedule(
            s.getScheduleSeq(), user.getUserSeq(), CommentCreateRequest.of("세상에서 제일 불행한 사람임"));

        // then
        assertThat(response).isNotNull()
            .extracting("commentContent", "user")
            .containsExactly("세상에서 제일 불행한 사람임", UserReadResponse.of(user));
    }

    @DisplayName("댓글을 삭제한다.")
    @Test
    void deleteComment() {
        // given
        User user = userRepository.findAll().get(0);

        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .scheduleContent("여기는 동기화 되는 메모야")
            .scheduleMemo("이거는 안되는 메모고")
            .isDateSelected(false)
            .isTimeSelected(false)
            .isAllDay(false)
            .alarmTime(AlarmTime.NONE)
            .isAuthorizedAll(false)
            .participants(List.of(
                UserReadResponse.of(user)))
            .build();
        scheduleService.createSchedule(user.getUserSeq(), request);
        Schedule s = scheduleRepository.findAll().get(0);

        CommentReadResponse r1 = scheduleService.createCommentOnSchedule(
            s.getScheduleSeq(), user.getUserSeq(), CommentCreateRequest.of("세상에서 제일 불행한 사람임"));
        CommentReadResponse r2 = scheduleService.createCommentOnSchedule(s.getScheduleSeq(),
            user.getUserSeq(), CommentCreateRequest.of("얘는 삭제될거임"));

        // when
        scheduleService.deleteComment(r2.commentSeq(), user.getUserSeq());
        List<Comment> result = commentRepository.findBySchedule_ScheduleSeqAndIsDeletedFalse(
            s.getScheduleSeq());

        // then
        assertThat(result).hasSize(1)
            .extracting("commentSeq")
            .contains(r1.commentSeq());
    }

    // =================== 카테고리 ===================

    @DisplayName("새로운 카테고리를 생성한다.")
    @Test
    void createNewCategory() {
        // given
        User user = userRepository.findAll().get(0);
        CategoryCreateRequest request = CategoryCreateRequest.of("자율기절", "HOTPINK");

        // when
        CategoryReadResponse response = scheduleService.createCategory(user.getUserSeq(), request);

        // then
        assertThat(response).isNotNull()
            .extracting("categoryName", "categoryColor")
            .containsExactly("자율기절", "HOTPINK");
    }

    @DisplayName("사용자의 카테고리 목록을 조회한다.")
    @Test
    void getCategoriesForUser() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u2 = userRepository.findAll().get(1);
        CategoryCreateRequest r1 = CategoryCreateRequest.of("자율기절", "HOTPINK");
        CategoryCreateRequest r2 = CategoryCreateRequest.of("합창단", "GREEN");
        scheduleService.createCategory(u1.getUserSeq(), r1);
        scheduleService.createCategory(u1.getUserSeq(), r2);
        scheduleService.createCategory(u2.getUserSeq(), r1);

        // when
        List<CategoryReadResponse> u1Categories = scheduleService.getCategoryList(u1.getUserSeq());
        List<CategoryReadResponse> u2Categories = scheduleService.getCategoryList(u2.getUserSeq());

        // then
        assertThat(u1Categories)
            .hasSize(2)
            .extracting("categoryName", "categoryColor")
            .containsExactlyInAnyOrder(
                tuple("자율기절", "HOTPINK"),
                tuple("합창단", "GREEN"));
        assertThat(u2Categories)
            .hasSize(1)
            .extracting("categoryName", "categoryColor")
            .containsExactlyInAnyOrder(
                tuple("자율기절", "HOTPINK"));
    }

    @DisplayName("카테고리 정보를 수정한다. 변경되지 않는 사항에 대해서는 기존 값을 유지한다.")
    @Test
    void updateCategory() {
        // given
        User u1 = userRepository.findAll().get(0);

        CategoryCreateRequest r1 = CategoryCreateRequest.of("자율기절", "HOTPINK");
        scheduleService.createCategory(u1.getUserSeq(), r1);
        Category category = categoryRepository.findAll().get(0);

        // when
        CategoryPatchRequest request = CategoryPatchRequest.builder()
            .categoryName("자율기절")
            .categoryColor("GREEN")
            .build();
        CategoryReadResponse response = scheduleService.updateCategory(u1.getUserSeq(),
            category.getCategorySeq(), request);

        // then
        assertThat(response)
            .extracting("categorySeq", "categoryName", "categoryColor")
            .containsExactly(category.getCategorySeq(), "자율기절", "GREEN");
    }

    @DisplayName("카테고리를 삭제하고 해당 카테고리에 속한 일정들을 미분류로 변경한다.")
    @Test
    void deleteCategory() {
        // given
        User u1 = userRepository.findAll().get(0);

        CategoryCreateRequest r1 = CategoryCreateRequest.of("자율기절", "HOTPINK");
        scheduleService.createCategory(u1.getUserSeq(), r1);
        Category category = categoryRepository.findAll().get(0);

        ScheduleCreateRequest sr1 = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .isTimeSelected(false)
            .isDateSelected(false)
            .isAllDay(false)
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.NONE)
            .participants(List.of(
                UserReadResponse.of(u1)))
            .categorySeq(category.getCategorySeq())
            .build();
        scheduleService.createSchedule(u1.getUserSeq(), sr1);

        // when
        scheduleService.deleteCategory(u1.getUserSeq(), category.getCategorySeq());
        Schedule schedule = scheduleRepository.findAll().get(0);
        Optional<Participation> participation = participationRepository.findBySchedule_ScheduleSeqAndUser_UserSeqAndIsDeletedFalse(
            schedule.getScheduleSeq(), u1.getUserSeq());

        // then
        assertThat(participation).isPresent();
        assertThat(participation.get()).extracting("category").isNull();
    }

    // =================== 실패 테스트 ===================

    @DisplayName("날짜가 설정된 일정에서 날짜가 입력되지 않으면 일정 생성에 실패한다.")
    @Test
    void noStartTime() {
        // given
        User user = userRepository.findAll().get(0);

        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .scheduleContent("여기는 동기화 되는 메모야")
            .isDateSelected(true)
            .isTimeSelected(false)
            .isAllDay(false)
            .alarmTime(AlarmTime.NONE)
            .isAuthorizedAll(true)
            .participants(List.of(
                UserReadResponse.of(user)))
            .build();

        // when // then
        assertThatThrownBy(() -> scheduleService.createSchedule(user.getUserSeq(), request))
            .isInstanceOf(RestApiException.class)
            .hasMessage("SCHEDULE_DATE_NOT_SELECTED");

    }

    @DisplayName("시간이 설정된 일정에서 시간이 입력되지 않으면 일정 생성에 실패한다.")
    @Test
    void noEndTime() {
        // given
        User user = userRepository.findAll().get(0);

        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .scheduleContent("여기는 동기화 되는 메모야")
            .isDateSelected(true)
            .isTimeSelected(true)
            .scheduleStartAt("2023-11-01 08:59:30")
            .isAllDay(false)
            .alarmTime(AlarmTime.NONE)
            .isAuthorizedAll(true)
            .participants(List.of(
                UserReadResponse.of(user)))
            .build();

        // when // then
        assertThatThrownBy(() -> scheduleService.createSchedule(user.getUserSeq(), request))
            .isInstanceOf(RestApiException.class)
            .hasMessage("SCHEDULE_TIME_NOT_SELECTED");
    }

    @DisplayName("유효하지 않은 일정에 접근해 일정 조회에 실패한다.")
    @Test
    void noScheduleSeq() {
        // given
        Long scheduleSeq = 3L;
        User user = userRepository.findAll().get(0);

        assertThatThrownBy(() -> scheduleService.getScheduleDetail(scheduleSeq, user.getUserSeq()))
            .isInstanceOf(RestApiException.class)
            .hasMessage("SCHEDULE_NOT_FOUND");
    }

    @DisplayName("유효하지 않은 일정에 접근해 일정 수정에 실패한다.")
    @Test
    void invalidUpdateSchedule() {
        // given
        User user = userRepository.findAll().get(0);

        ScheduleCreateRequest createRequest = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .isTimeSelected(false)
            .isDateSelected(false)
            .isAllDay(false)
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.NONE)
            .participants(List.of(
                UserReadResponse.of(user)))
            .build();
        scheduleService.createSchedule(user.getUserSeq(), createRequest);
        Schedule schedule = scheduleRepository.findAll().get(0);

        SchedulePatchRequest patchRequest = SchedulePatchRequest.builder()
            .scheduleName("안녕 나는 바뀐 일정 이름이야")
            .build();

        // when // then
        assertThatThrownBy(
            () -> scheduleService.updateSchedule(user.getUserSeq(), schedule.getScheduleSeq() + 1,
                patchRequest))
            .isInstanceOf(RestApiException.class)
            .hasMessage("SCHEDULE_NOT_FOUND");
    }

    @DisplayName("친구관계가 아닌 사용자는 추가할 수 없다.")
    @Test
    void noFriendshipBetweenUsers() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u3 = userRepository.findAll().get(2);

        ScheduleCreateRequest createRequest = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .isTimeSelected(false)
            .isDateSelected(false)
            .isAllDay(false)
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.NONE)
            .participants(List.of(
                UserReadResponse.of(u1)))
            .build();
        scheduleService.createSchedule(u1.getUserSeq(), createRequest);
        Schedule schedule = scheduleRepository.findAll().get(0);

        // when
        SchedulePatchRequest schedulePatchRequest = SchedulePatchRequest.builder()
            .scheduleName("안녕 나는 바뀐 일정 이름이야")
            .isDateSelected(false)
            .isTimeSelected(false)
            .isAllDay(false)
            .alarmTime(AlarmTime.NONE)
            .participants(List.of(
                UserReadResponse.of(u1), UserReadResponse.of(u3)))
            .build();

        // then
        assertThatThrownBy(() -> scheduleService.updateSchedule(u1.getUserSeq(),
            schedule.getScheduleSeq(), schedulePatchRequest))
            .isInstanceOf(RestApiException.class)
            .hasMessage("SCHEDULE_FORBIDDEN");
    }

    @DisplayName("양방향으로 친구관계이지 않은 경우는 추가할 수 없다.")
    @Test
    void friendRelationshipDamaged() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u3 = userRepository.findAll().get(2);

        // 단방향으로만 친구관계 생성된 상황 가정
        friendRepository.save(Friend.create(u1, u3));

        ScheduleCreateRequest createRequest = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .isTimeSelected(false)
            .isDateSelected(false)
            .isAllDay(false)
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.NONE)
            .participants(List.of(
                UserReadResponse.of(u1)))
            .build();
        scheduleService.createSchedule(u1.getUserSeq(), createRequest);
        Schedule schedule = scheduleRepository.findAll().get(0);

        // when
        SchedulePatchRequest schedulePatchRequest = SchedulePatchRequest.builder()
            .scheduleName("안녕 나는 바뀐 일정 이름이야")
            .isDateSelected(false)
            .isTimeSelected(false)
            .isAllDay(false)
            .alarmTime(AlarmTime.NONE)
            .participants(List.of(
                UserReadResponse.of(u1), UserReadResponse.of(u3)))
            .build();

        // then
        assertThatThrownBy(() -> scheduleService.updateSchedule(u1.getUserSeq(),
            schedule.getScheduleSeq(), schedulePatchRequest))
            .isInstanceOf(RestApiException.class)
            .hasMessage("FRIEND_RELATION_DAMAGED");
    }

    @DisplayName("일정 수정 시 수정 권한이 없으면 수정할 수 없다.")
    @Test
    void forbiddenToUpdateSchedule() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u2 = userRepository.findAll().get(1);

        ScheduleCreateRequest createRequest = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .isTimeSelected(false)
            .isDateSelected(false)
            .isAllDay(false)
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.NONE)
            .participants(List.of(
                UserReadResponse.of(u1), UserReadResponse.of(u2)))
            .build();
        scheduleService.createSchedule(u1.getUserSeq(), createRequest);
        Schedule schedule = scheduleRepository.findAll().get(0);

        SchedulePatchRequest patchRequest = SchedulePatchRequest.builder()
            .scheduleName("안녕 나는 바뀐 일정 이름이야")
            .build();

        // when // then
        assertThatThrownBy(
            () -> scheduleService.updateSchedule(u2.getUserSeq(), schedule.getScheduleSeq(),
                patchRequest))
            .isInstanceOf(RestApiException.class)
            .hasMessage("SCHEDULE_FORBIDDEN");
    }

    @DisplayName("유효하지 않은 일정에 접근해 일정 삭제에 실패한다.")
    @Test
    void invalidDeleteSchedule() {
        // given
        User user = userRepository.findAll().get(0);
        ScheduleCreateRequest createRequest = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .isTimeSelected(false)
            .isDateSelected(false)
            .isAllDay(false)
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.NONE)
            .participants(List.of(
                UserReadResponse.of(user)))
            .build();
        scheduleService.createSchedule(user.getUserSeq(), createRequest);
        Schedule schedule = scheduleRepository.findAll().get(0);

        // when // then
        assertThatThrownBy(() -> scheduleService.deleteSchedule(user.getUserSeq(),
            schedule.getScheduleSeq() + 1))
            .isInstanceOf(RestApiException.class)
            .hasMessage("SCHEDULE_NOT_FOUND");
    }

    @DisplayName("권한이 없는 경우 일정을 삭제할 수 없다.")
    @Test
    void forbiddenToDeleteSchedule() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u2 = userRepository.findAll().get(1);

        ScheduleCreateRequest createRequest = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .isTimeSelected(false)
            .isDateSelected(false)
            .isAllDay(false)
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.NONE)
            .participants(List.of(
                UserReadResponse.of(u1), UserReadResponse.of(u2)))
            .build();
        scheduleService.createSchedule(u1.getUserSeq(), createRequest);
        Schedule schedule = scheduleRepository.findAll().get(0);

        // when // then
        assertThatThrownBy(() -> scheduleService.deleteSchedule(u2.getUserSeq(),
            schedule.getScheduleSeq()))
            .isInstanceOf(RestApiException.class)
            .hasMessage("SCHEDULE_FORBIDDEN");
    }

    @DisplayName("유효하지 않은 일정에 접근해 댓글 생성에 실패한다.")
    @Test
    void noScheduleToComment() {
        // given
        User user = userRepository.findAll().get(0);

        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .scheduleContent("여기는 동기화 되는 메모야")
            .scheduleMemo("이거는 안되는 메모고")
            .isDateSelected(false)
            .isTimeSelected(false)
            .isAllDay(false)
            .alarmTime(AlarmTime.NONE)
            .isAuthorizedAll(false)
            .participants(List.of(
                UserReadResponse.of(user)))
            .build();
        scheduleService.createSchedule(user.getUserSeq(), request);

        Schedule s = scheduleRepository.findAll().get(0);

        // when // then
        assertThatThrownBy(() -> scheduleService.createCommentOnSchedule(
            s.getScheduleSeq() + 1, user.getUserSeq(),
            CommentCreateRequest.of("세상에서 제일 불행한 사람임"))
        ).isInstanceOf(RestApiException.class)
            .hasMessage("SCHEDULE_NOT_FOUND");
    }

    @DisplayName("유효하지 않은 댓글에 접근해 삭제에 실패한다.")
    @Test
    void invalidDeleteComment() {
        // given
        User user = userRepository.findAll().get(0);

        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .scheduleContent("여기는 동기화 되는 메모야")
            .scheduleMemo("이거는 안되는 메모고")
            .isDateSelected(false)
            .isTimeSelected(false)
            .isAllDay(false)
            .alarmTime(AlarmTime.NONE)
            .isAuthorizedAll(false)
            .participants(List.of(
                UserReadResponse.of(user)))
            .build();
        scheduleService.createSchedule(user.getUserSeq(), request);
        Schedule s = scheduleRepository.findAll().get(0);

        CommentReadResponse response = scheduleService.createCommentOnSchedule(
            s.getScheduleSeq(), user.getUserSeq(), CommentCreateRequest.of("세상에서 제일 불행한 사람임"));

        // when // then
        assertThatThrownBy(
            () -> scheduleService.deleteComment(response.commentSeq() + 1, user.getUserSeq()))
            .isInstanceOf(RestApiException.class)
            .hasMessage("COMMENT_NOT_FOUND");
    }

    @DisplayName("본인의 댓글만 삭제할 수 있다.")
    @Test
    void forbiddenToDeleteComment() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u2 = userRepository.findAll().get(1);

        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .scheduleContent("여기는 동기화 되는 메모야")
            .scheduleMemo("이거는 안되는 메모고")
            .isDateSelected(false)
            .isTimeSelected(false)
            .isAllDay(false)
            .alarmTime(AlarmTime.NONE)
            .isAuthorizedAll(false)
            .participants(List.of(
                UserReadResponse.of(u1)))
            .build();
        scheduleService.createSchedule(u1.getUserSeq(), request);
        Schedule s = scheduleRepository.findAll().get(0);

        CommentReadResponse response = scheduleService.createCommentOnSchedule(
            s.getScheduleSeq(), u1.getUserSeq(), CommentCreateRequest.of("세상에서 제일 불행한 사람임"));

        // when // then
        assertThatThrownBy(
            () -> scheduleService.deleteComment(response.commentSeq(), u2.getUserSeq()))
            .isInstanceOf(RestApiException.class)
            .hasMessage("COMMENT_FORBIDDEN");
    }

    @DisplayName("기존에 있던 카테고리 이름으로 카테고리 생성에 실패한다.")
    @Test
    void categoryAlreadyExists() {
        // given
        User user = userRepository.findAll().get(0);
        CategoryCreateRequest r1 = CategoryCreateRequest.of("자율기절", "HOTPINK");
        CategoryCreateRequest r2 = CategoryCreateRequest.of("자율기절", "GREEN");
        scheduleService.createCategory(user.getUserSeq(), r1);

        // when, then
        assertThatThrownBy(() -> scheduleService.createCategory(user.getUserSeq(), r2))
            .isInstanceOf(RestApiException.class)
            .hasMessage("CATEGORY_ALREADY_EXISTS");
    }


    @DisplayName("날짜 조건으로 일정을 조회하려면 연 단위 입력이 있어야 한다.")
    @Transactional
    @Test
    void noSearchConditionWithYear() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u2 = userRepository.findAll().get(1);

        ScheduleCreateRequest r1 = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-10-01 08:59:30")
            .scheduleEndAt("2023-11-01 09:00:00")
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.NONE)
            .participants(List.of(
                UserReadResponse.of(u1)))
            .build();
        ScheduleCreateRequest r2 = ScheduleCreateRequest.builder()
            .scheduleName("나도 일정이야")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-11-01 08:59:30")
            .scheduleEndAt("2023-11-01 09:00:00")
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.ON_TIME)
            .participants(List.of(UserReadResponse.of(u1), UserReadResponse.of(u2)))
            .build();
        ScheduleCreateRequest r3 = ScheduleCreateRequest.builder()
            .scheduleName("나도 일정이야")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-10-30 08:59:30")
            .scheduleEndAt("2023-10-30 09:00:00")
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.ON_TIME)
            .participants(List.of(UserReadResponse.of(u1), UserReadResponse.of(u2)))
            .build();
        ScheduleCreateRequest r4 = ScheduleCreateRequest.builder()
            .scheduleName("나도 일정이야")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-11-30 08:59:30")
            .scheduleEndAt("2023-12-01 09:00:00")
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.ON_TIME)
            .participants(List.of(UserReadResponse.of(u1)))
            .build();
        ScheduleCreateRequest r5 = ScheduleCreateRequest.builder()
            .scheduleName("나도 일정이야")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-11-30 08:59:30")
            .scheduleEndAt("2023-12-01 09:00:00")
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.ON_TIME)
            .participants(List.of(UserReadResponse.of(u2)))
            .build();
        scheduleService.createSchedule(u1.getUserSeq(), r1);
        scheduleService.createSchedule(u1.getUserSeq(), r2);
        scheduleService.createSchedule(u1.getUserSeq(), r3);
        scheduleService.createSchedule(u1.getUserSeq(), r4);
        scheduleService.createSchedule(u2.getUserSeq(), r5);

        // when
        Map<String, String> searchCondition = Map.of("categories", "", "searchKey", "",
            "unscheduled", "", "month", "11", "day", "1", "year", "");

        // then
        assertThatThrownBy(() -> scheduleService.getScheduleListBySearchCondition(
            u1.getUserSeq(), searchCondition))
            .isInstanceOf(RestApiException.class)
            .hasMessage("SCHEDULE_INVALID_REQUEST");
    }

    @DisplayName("일 단위로 일정을 조회하려면 월 단위 입력이 있어야 한다.")
    @Transactional
    @Test
    void noSearchConditionWithMonth() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u2 = userRepository.findAll().get(1);

        ScheduleCreateRequest r1 = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-10-01 08:59:30")
            .scheduleEndAt("2023-11-01 09:00:00")
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.NONE)
            .participants(List.of(
                UserReadResponse.of(u1)))
            .build();
        ScheduleCreateRequest r2 = ScheduleCreateRequest.builder()
            .scheduleName("나도 일정이야")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-11-01 08:59:30")
            .scheduleEndAt("2023-11-01 09:00:00")
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.ON_TIME)
            .participants(List.of(UserReadResponse.of(u1), UserReadResponse.of(u2)))
            .build();
        ScheduleCreateRequest r3 = ScheduleCreateRequest.builder()
            .scheduleName("나도 일정이야")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-10-30 08:59:30")
            .scheduleEndAt("2023-10-30 09:00:00")
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.ON_TIME)
            .participants(List.of(UserReadResponse.of(u1), UserReadResponse.of(u2)))
            .build();
        ScheduleCreateRequest r4 = ScheduleCreateRequest.builder()
            .scheduleName("나도 일정이야")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-11-30 08:59:30")
            .scheduleEndAt("2023-12-01 09:00:00")
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.ON_TIME)
            .participants(List.of(UserReadResponse.of(u1)))
            .build();
        ScheduleCreateRequest r5 = ScheduleCreateRequest.builder()
            .scheduleName("나도 일정이야")
            .isTimeSelected(true)
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-11-30 08:59:30")
            .scheduleEndAt("2023-12-01 09:00:00")
            .isAuthorizedAll(false)
            .alarmTime(AlarmTime.ON_TIME)
            .participants(List.of(UserReadResponse.of(u2)))
            .build();
        scheduleService.createSchedule(u1.getUserSeq(), r1);
        scheduleService.createSchedule(u1.getUserSeq(), r2);
        scheduleService.createSchedule(u1.getUserSeq(), r3);
        scheduleService.createSchedule(u1.getUserSeq(), r4);
        scheduleService.createSchedule(u2.getUserSeq(), r5);

        // when
        Map<String, String> searchCondition = Map.of("categories", "", "searchKey", "",
            "unscheduled", "", "month", "", "day", "1", "year", "2023");

        // then
        assertThatThrownBy(() -> scheduleService.getScheduleListBySearchCondition(
            u1.getUserSeq(), searchCondition))
            .isInstanceOf(RestApiException.class)
            .hasMessage("SCHEDULE_INVALID_REQUEST");
    }


    @DisplayName("유효하지 않은 카테고리에 접근해 수정에 실패한다.")
    @Test
    void invalidUpdateCategory() {
        // given
        User user = userRepository.findAll().get(0);
        CategoryCreateRequest createRequest = CategoryCreateRequest.of("자율기절", "HOTPINK");
        scheduleService.createCategory(user.getUserSeq(), createRequest);

        Category category = categoryRepository.findAll().get(0);

        CategoryPatchRequest patchRequest = CategoryPatchRequest.builder()
            .categoryName("자율기절")
            .categoryColor("GREEN")
            .build();

        // when // then
        assertThatThrownBy(
            () -> scheduleService.updateCategory(user.getUserSeq(), category.getCategorySeq() + 1,
                patchRequest))
            .isInstanceOf(RestApiException.class)
            .hasMessage("CATEGORY_NOT_FOUND");
    }

    @DisplayName("본인의 카테고리만 수정할 수 있다.")
    @Test
    void forbiddenToUpdateCategory() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u2 = userRepository.findAll().get(1);

        CategoryCreateRequest createRequest = CategoryCreateRequest.of("자율기절", "HOTPINK");
        scheduleService.createCategory(u1.getUserSeq(), createRequest);

        Category category = categoryRepository.findAll().get(0);

        CategoryPatchRequest patchRequest = CategoryPatchRequest.builder()
            .categoryName("자율기절")
            .categoryColor("GREEN")
            .build();

        // when // then
        assertThatThrownBy(
            () -> scheduleService.updateCategory(u2.getUserSeq(), category.getCategorySeq(),
                patchRequest))
            .isInstanceOf(RestApiException.class)
            .hasMessage("CATEGORY_FORBIDDEN");
    }

    @DisplayName("유효하지 않은 카테고리에 접근해 삭제에 실패한다.")
    @Test
    void invalidDeleteCategory() {
        // given
        User user = userRepository.findAll().get(0);
        CategoryCreateRequest createRequest = CategoryCreateRequest.of("자율기절", "HOTPINK");
        scheduleService.createCategory(user.getUserSeq(), createRequest);

        Category category = categoryRepository.findAll().get(0);

        // when // then
        assertThatThrownBy(() -> scheduleService.deleteCategory(user.getUserSeq(),
            category.getCategorySeq() + 1))
            .isInstanceOf(RestApiException.class)
            .hasMessage("CATEGORY_NOT_FOUND");
    }

    @DisplayName("본인의 카테고리만 삭제할 수 있다.")
    @Test
    void forbiddenToDeleteCategory() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u2 = userRepository.findAll().get(1);

        CategoryCreateRequest createRequest = CategoryCreateRequest.of("자율기절", "HOTPINK");
        scheduleService.createCategory(u1.getUserSeq(), createRequest);

        Category category = categoryRepository.findAll().get(0);

        // when // then
        assertThatThrownBy(() -> scheduleService.deleteCategory(u2.getUserSeq(),
            category.getCategorySeq()))
            .isInstanceOf(RestApiException.class)
            .hasMessage("CATEGORY_FORBIDDEN");
    }

    // =================== 개별 일정 알림 설정 테스트 ===================

    @DisplayName("멘션 알림 설정을 On으로 설정한다.")
    @Test
    void mention_alarm_on() {
        // givne
        User user = userRepository.findAll().get(0);

        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .scheduleContent("여기는 동기화 되는 메모야")
            .scheduleMemo("이거는 안되는 메모고")
            .isDateSelected(false)
            .isTimeSelected(false)
            .isAllDay(false)
            .alarmTime(AlarmTime.NONE)
            .isAuthorizedAll(false)
            .participants(List.of(
                UserReadResponse.of(user)))
            .build();
        scheduleService.createSchedule(user.getUserSeq(), request);

        Schedule schedule = scheduleRepository.findAll().get(0);

        // when
        scheduleService.setMentionAlarm(user.getUserSeq(), schedule.getScheduleSeq(), true);

        // then
        Optional<Participation> participation = participationRepository.findBySchedule_ScheduleSeqAndUser_UserSeqAndIsDeletedFalse(
            schedule.getScheduleSeq(), user.getUserSeq());
        assertTrue(participation.isPresent());
        assertTrue(participation.get().isMentionAlarmOn());
    }

    @DisplayName("멘션 알림 설정을 Off으로 설정한다.")
    @Test
    void mention_alarm_off() {
        // givne
        User user = userRepository.findAll().get(0);

        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .scheduleContent("여기는 동기화 되는 메모야")
            .scheduleMemo("이거는 안되는 메모고")
            .isDateSelected(false)
            .isTimeSelected(false)
            .isAllDay(false)
            .alarmTime(AlarmTime.NONE)
            .isAuthorizedAll(false)
            .participants(List.of(
                UserReadResponse.of(user)))
            .build();
        scheduleService.createSchedule(user.getUserSeq(), request);

        Schedule schedule = scheduleRepository.findAll().get(0);

        // when
        scheduleService.setMentionAlarm(user.getUserSeq(), schedule.getScheduleSeq(), false);

        // then
        Optional<Participation> participation = participationRepository.findBySchedule_ScheduleSeqAndUser_UserSeqAndIsDeletedFalse(
            schedule.getScheduleSeq(), user.getUserSeq());
        assertTrue(participation.isPresent());
        assertFalse(participation.get().isMentionAlarmOn());
    }

    @DisplayName("존재하지 않는 일정의 멘션 알림을 수정할 수 없다.")
    @Test
    void cannot_set_mention_alram_if_schedule_not_exist() {
        // givne
        User user = userRepository.findAll().get(0);

        // when + then
        assertThatThrownBy(() -> scheduleService.setMentionAlarm(user.getUserSeq(), 1L, true))
            .isInstanceOf(RestApiException.class)
            .hasMessage("SCHEDULE_NOT_FOUND");
    }

    @DisplayName("수정 알림 설정을 On으로 설정한다.")
    @Test
    void update_alarm_on() {
        // givne
        User user = userRepository.findAll().get(0);

        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .scheduleContent("여기는 동기화 되는 메모야")
            .scheduleMemo("이거는 안되는 메모고")
            .isDateSelected(false)
            .isTimeSelected(false)
            .isAllDay(false)
            .alarmTime(AlarmTime.NONE)
            .isAuthorizedAll(false)
            .participants(List.of(
                UserReadResponse.of(user)))
            .build();
        scheduleService.createSchedule(user.getUserSeq(), request);

        Schedule schedule = scheduleRepository.findAll().get(0);

        // when
        scheduleService.setUpdateAlarm(user.getUserSeq(), schedule.getScheduleSeq(), true);

        // then
        Optional<Participation> participation = participationRepository.findBySchedule_ScheduleSeqAndUser_UserSeqAndIsDeletedFalse(
            schedule.getScheduleSeq(), user.getUserSeq());
        assertTrue(participation.isPresent());
        assertTrue(participation.get().isUpdateAlarmOn());
    }

    @DisplayName("수정 알림 설정을 Off으로 설정한다.")
    @Test
    void update_alarm_off() {
        // givne
        User user = userRepository.findAll().get(0);

        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
            .scheduleName("안녕 내가 일정 이름이야")
            .scheduleContent("여기는 동기화 되는 메모야")
            .scheduleMemo("이거는 안되는 메모고")
            .isDateSelected(false)
            .isTimeSelected(false)
            .isAllDay(false)
            .alarmTime(AlarmTime.NONE)
            .isAuthorizedAll(false)
            .participants(List.of(
                UserReadResponse.of(user)))
            .build();
        scheduleService.createSchedule(user.getUserSeq(), request);

        Schedule schedule = scheduleRepository.findAll().get(0);

        // when
        scheduleService.setUpdateAlarm(user.getUserSeq(), schedule.getScheduleSeq(), false);

        // then
        Optional<Participation> participation = participationRepository.findBySchedule_ScheduleSeqAndUser_UserSeqAndIsDeletedFalse(
            schedule.getScheduleSeq(), user.getUserSeq());
        assertTrue(participation.isPresent());
        assertFalse(participation.get().isUpdateAlarmOn());
    }

    @DisplayName("존재하지 않는 일정의 수정 알림을 수정할 수 없다.")
    @Test
    void cannot_set_update_alram_if_schedule_not_exist() {
        // givne
        User user = userRepository.findAll().get(0);

        // when + then
        assertThatThrownBy(() -> scheduleService.setUpdateAlarm(user.getUserSeq(), 1L, true))
            .isInstanceOf(RestApiException.class)
            .hasMessage("SCHEDULE_NOT_FOUND");
    }
}
