package com.pppppp.amadda.schedule.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.schedule.dto.request.CategoryCreateRequest;
import com.pppppp.amadda.schedule.dto.request.CommentCreateRequest;
import com.pppppp.amadda.schedule.dto.request.ScheduleCreateRequest;
import com.pppppp.amadda.schedule.dto.response.CategoryReadResponse;
import com.pppppp.amadda.schedule.dto.response.CommentReadResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleCreateResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleDetailReadResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleListReadResponse;
import com.pppppp.amadda.schedule.entity.AlarmTime;
import com.pppppp.amadda.schedule.entity.Category;
import com.pppppp.amadda.schedule.entity.CategoryColor;
import com.pppppp.amadda.schedule.entity.Schedule;
import com.pppppp.amadda.schedule.repository.CategoryRepository;
import com.pppppp.amadda.schedule.repository.CommentRepository;
import com.pppppp.amadda.schedule.repository.ParticipationRepository;
import com.pppppp.amadda.schedule.repository.ScheduleRepository;
import com.pppppp.amadda.user.dto.response.UserReadResponse;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ScheduleServiceTest extends IntegrationTestSupport {

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


    @BeforeEach
    void setUp() {
        User user1 = User.create(1L, "박동건", "icebearrrr", "url1");
        User user2 = User.create(2L, "정민영", "minyoung", "url2");
        User user3 = User.create(3L, "김민정", "mindy0414", "url3");
        userRepository.saveAll(List.of(user1, user2, user3));
    }

    @AfterEach
    void tearDown() {
        participationRepository.deleteAllInBatch();
        categoryRepository.deleteAllInBatch();
        commentRepository.deleteAllInBatch();
        scheduleRepository.deleteAllInBatch();
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
            "unscheduled", "");
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
            "searchKey", "", "unscheduled", "");

        Map<String, String> searchCondition2 = Map.of("categories",
            String.valueOf(categories.get(0).getCategorySeq()), "searchKey", "", "unscheduled", "");

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
            "categories", "");
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
            "unscheduled", "true");
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

    @DisplayName("일정 참가자 명단에서 이름으로 유저를 검색한다.")
    @Transactional
    @Test
    void getParticipatingUserListBySearchKey() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u2 = userRepository.findAll().get(1);
        User u3 = userRepository.findAll().get(2);

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

    // =================== 실패 테스트 ===================

    @DisplayName("유효하지 않은 일정 seq로 인해 일정 조회에 실패한다.")
    @Test
    void noScheduleSeq() {
        // given
        Long scheduleSeq = 3L;
        User user = userRepository.findAll().get(0);

        assertThatThrownBy(() -> scheduleService.getScheduleDetail(scheduleSeq, user.getUserSeq()))
            .isInstanceOf(RestApiException.class)
            .hasMessage("SCHEDULE_NOT_FOUND");
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
}