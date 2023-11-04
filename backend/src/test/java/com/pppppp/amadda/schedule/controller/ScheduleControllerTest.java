package com.pppppp.amadda.schedule.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pppppp.amadda.alarm.service.AlarmService;
import com.pppppp.amadda.schedule.dto.request.CategoryCreateRequest;
import com.pppppp.amadda.schedule.dto.request.CommentCreateRequest;
import com.pppppp.amadda.schedule.dto.request.ScheduleCreateRequest;
import com.pppppp.amadda.schedule.dto.response.CategoryReadResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleCreateResponse;
import com.pppppp.amadda.schedule.entity.AlarmTime;
import com.pppppp.amadda.schedule.service.ScheduleService;
import com.pppppp.amadda.user.dto.response.UserReadResponse;
import com.pppppp.amadda.user.entity.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ScheduleController.class)
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ScheduleService scheduleService;

    @MockBean
    private AlarmService alarmService;

    // TODO: 동적쿼리 구현 후 테스트 코드 추가 작성 필요

    @DisplayName("일정을 생성한다.")
    @Test
    void createSchedule() throws Exception {
        // given
        User user = User.create(1111L, "박동건", "icebearrrr", "imgUrl1");

        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
            // schedule
            .scheduleContent("수원 시민 회관")
            .isDateSelected(true)
            .isTimeSelected(true)
            .isAllDay(false)
            .isAuthorizedAll(true)
            .scheduleStartAt("2023-11-18 18:30:00")
            .scheduleEndAt("2023-11-18 20:30:00")
            .participants(List.of(UserReadResponse.of(user)))
            // participation
            .scheduleName("합창단 공연")
            .scheduleMemo("수원역에서 걸어서 10분")
            .alarmTime(AlarmTime.ONE_DAY_BEFORE)
            .build();

        // stubbing
        when(scheduleService.createSchedule(anyLong(), eq(request)))
            .thenReturn(ScheduleCreateResponse.builder().scheduleSeq(1L).build());

        // when  // then
        mockMvc.perform(
                post("/api/schedule")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(
                print()
            )
            .andExpect(status().isOk());
    }

    @DisplayName("사용자의 단일 일정을 조회한다.")
    @Test
    void getSchedule() throws Exception {
        mockMvc.perform(
                get("/api/schedule/{scheduleSeq}", 1)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(
                print()
            )
            .andExpect(status().isOk());
    }

    @DisplayName("새로운 댓글을 등록한다.")
    @Test
    void createComment() throws Exception {
        Long scheduleSeq = 1L;

        CommentCreateRequest request = CommentCreateRequest.builder()
            .commentContent("방탈출 재밌겠다")
            .build();

        mockMvc.perform(
                post("/api/schedule/{scheduleSeq}/comment", scheduleSeq)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(
                print()
            )
            .andExpect(status().isOk());
    }

    @DisplayName("카테고리를 생성한다.")
    @Test
    void createCategory() throws Exception {
        User user = User.create(1111L, "박동건", "icebearrrr", "imgUrl1");
        CategoryCreateRequest request = CategoryCreateRequest.builder()
            .categoryName("합창단")
            .categoryColor("GREEN")
            .build();

        mockMvc.perform(
                post("/api/schedule/user/category")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(
                print()
            )
            .andExpect(status().isOk());
    }

    @DisplayName("사용자의 카테고리 목록을 조회한다.")
    @Test
    void getCategoryList() throws Exception {
        User user = User.create(1111L, "박동건", "icebearrrr", "imgUrl1");
        List<CategoryReadResponse> response = List.of();

        mockMvc.perform(
                get("/api/schedule/user/category")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(
                print()
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray());
    }

    // ==================== 실패 테스트 ====================

    @DisplayName("일정 생성 시 일정 제목은 필수로 입력되어야 한다.")
    @Test
    void noScheduleName() throws Exception {

        User user = User.create(1111L, "박동건", "icebearrrr", "imgUrl1");

        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
            // schedule
            .scheduleContent("수원 시민 회관")
            .isDateSelected(true)
            .isTimeSelected(true)
            .isAllDay(false)
            .isAuthorizedAll(true)
            .scheduleStartAt("2023-11-18 18:30:00")
            .scheduleEndAt("2023-11-18 20:30:00")
            .participants(List.of(UserReadResponse.of(user)))
            // participation
            .scheduleMemo("수원역에서 걸어서 10분")
            .alarmTime(AlarmTime.ONE_DAY_BEFORE)
            .build();

        mockMvc.perform(
                post("/api/schedule")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(
                print()
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.message").value("일정 이름을 입력해주세요!"));
    }

    @DisplayName("일정 생성 시 날짜 확정 여부를 입력해야 한다.")
    @Test
    void noScheduleDateSelectedInfo() throws Exception {
        // given
        User user = User.create(1111L, "박동건", "icebearrrr", "imgUrl1");

        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
            // schedule
            .scheduleContent("수원 시민 회관")
            .isTimeSelected(true)
            .isAllDay(false)
            .isAuthorizedAll(true)
            .scheduleStartAt("2023-11-18 18:30:00")
            .scheduleEndAt("2023-11-18 20:30:00")
            .participants(List.of(UserReadResponse.of(user)))
            // participation
            .scheduleName("합창단 공연")
            .scheduleMemo("수원역에서 걸어서 10분")
            .alarmTime(AlarmTime.ONE_DAY_BEFORE)
            .build();

        // when  // then
        mockMvc.perform(
                post("/api/schedule")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(
                print()
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("날짜 확정 여부가 결정되지 않았어요!"));
    }

    @DisplayName("일정 생성 시 시간 확정 여부를 입력해야 한다.")
    @Test
    void noScheduleTimeSelectedInfo() throws Exception {
        // given
        User user = User.create(1111L, "박동건", "icebearrrr", "imgUrl1");

        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
            // schedule
            .scheduleContent("수원 시민 회관")
            .isDateSelected(true)
            .isAllDay(false)
            .isAuthorizedAll(true)
            .scheduleStartAt("2023-11-18 18:30:00")
            .scheduleEndAt("2023-11-18 20:30:00")
            .participants(List.of(UserReadResponse.of(user)))
            // participation
            .scheduleName("합창단 공연")
            .scheduleMemo("수원역에서 걸어서 10분")
            .alarmTime(AlarmTime.ONE_DAY_BEFORE)
            .build();

        // when  // then
        mockMvc.perform(
                post("/api/schedule")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(
                print()
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("시간 확정 여부가 결정되지 않았어요!"));
    }


    @DisplayName("일정 생성 시 일정의 하루종일 여부를 전달해야 한다.")
    @Test
    void noScheduleIsAllDayInfo() throws Exception {
        // given
        User user = User.create(1111L, "박동건", "icebearrrr", "imgUrl1");

        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
            // schedule
            .scheduleContent("수원 시민 회관")
            .isDateSelected(true)
            .isTimeSelected(true)
            .isAuthorizedAll(true)
            .scheduleStartAt("2023-11-18 18:30:00")
            .scheduleEndAt("2023-11-18 20:30:00")
            .participants(List.of(UserReadResponse.of(user)))
            // participation
            .scheduleName("합창단 공연")
            .scheduleMemo("수원역에서 걸어서 10분")
            .alarmTime(AlarmTime.ONE_DAY_BEFORE)
            .build();

        // when  // then
        mockMvc.perform(
                post("/api/schedule")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(
                print()
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("하루종일 지속되는 일정인지 알려주세요!"));
    }

    @DisplayName("일정 생성 시 권한을 필수로 설정해야 한다.")
    @Test
    void noScheduleAuthorizationInfo() throws Exception {
        // given
        User user = User.create(1111L, "박동건", "icebearrrr", "imgUrl1");

        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
            // schedule
            .scheduleContent("수원 시민 회관")
            .isDateSelected(true)
            .isTimeSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-11-18 18:30:00")
            .scheduleEndAt("2023-11-18 20:30:00")
            .participants(List.of(UserReadResponse.of(user)))
            // participation
            .scheduleName("합창단 공연")
            .scheduleMemo("수원역에서 걸어서 10분")
            .alarmTime(AlarmTime.ONE_DAY_BEFORE)
            .build();

        // when  // then
        mockMvc.perform(
                post("/api/schedule")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(
                print()
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("일정에 대한 수정 권한이 필요해요!"));
    }


    @DisplayName("일정 생성시 알림시간 설정은 필수다.")
    @Test
    void noScheduleAlarmTimeInfo() throws Exception {
        // given
        User user = User.create(1111L, "박동건", "icebearrrr", "imgUrl1");

        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
            // schedule
            .scheduleContent("수원 시민 회관")
            .isDateSelected(true)
            .isTimeSelected(true)
            .isAllDay(false)
            .isAuthorizedAll(true)
            .scheduleStartAt("2023-11-18 18:30:00")
            .scheduleEndAt("2023-11-18 20:30:00")
            .participants(List.of(UserReadResponse.of(user)))
            // participation
            .scheduleName("합창단 공연")
            .scheduleMemo("수원역에서 걸어서 10분")
            .build();

        // when  // then
        mockMvc.perform(
                post("/api/schedule")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(
                print()
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("알림 시간 설정이 필요해요!"));
    }

    @DisplayName("내용이 없는 빈 댓글은 달 수 없다.")
    @Test
    void noCommentContent() throws Exception {
        Long scheduleSeq = 1L;

        CommentCreateRequest request = CommentCreateRequest.builder()
            .build();

        mockMvc.perform(
                post("/api/schedule/{scheduleSeq}/comment", scheduleSeq)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(
                print()
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("댓글 내용을 입력해주세요!"));
    }

    @DisplayName("카테고리 생성시 이름이 필요하다.")
    @Test
    void noCategoryName() throws Exception {
        User user = User.create(1111L, "박동건", "icebearrrr", "imgUrl1");
        CategoryCreateRequest request = CategoryCreateRequest.builder()
            .categoryColor("GREEN")
            .build();

        mockMvc.perform(
                post("/api/schedule/user/category")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(
                print()
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("카테고리 이름을 입력해 주세요!"));
    }


    @DisplayName("카테고리 생성시 카테고리 색을 지정해야 한다.")
    @Test
    void noCategoryColor() throws Exception {
        User user = User.create(1111L, "박동건", "icebearrrr", "imgUrl1");
        CategoryCreateRequest request = CategoryCreateRequest.builder()
            .categoryName("합창단")
            .build();

        mockMvc.perform(
                post("/api/schedule/user/category")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(
                print()
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("카테고리 색을 선택해주세요!"));
    }
}