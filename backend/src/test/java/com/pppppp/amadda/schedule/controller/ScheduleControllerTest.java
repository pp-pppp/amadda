package com.pppppp.amadda.schedule.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pppppp.amadda.ControllerTestSupport;
import com.pppppp.amadda.schedule.dto.request.CategoryCreateRequest;
import com.pppppp.amadda.schedule.dto.request.CategoryUpdateRequest;
import com.pppppp.amadda.schedule.dto.request.CommentCreateRequest;
import com.pppppp.amadda.schedule.dto.request.ParticipationUpdateRequest;
import com.pppppp.amadda.schedule.dto.request.ScheduleCreateRequest;
import com.pppppp.amadda.schedule.dto.request.ScheduleUpdateRequest;
import com.pppppp.amadda.schedule.dto.response.CategoryUpdateResponse;
import com.pppppp.amadda.schedule.dto.response.ParticipationUpdateResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleCreateResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleUpdateResponse;
import com.pppppp.amadda.user.dto.response.UserReadResponse;
import com.pppppp.amadda.user.entity.User;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;

class ScheduleControllerTest extends ControllerTestSupport {

    @DisplayName("일정을 생성한다.")
    @Test
    void createSchedule() throws Exception {
        // given
        User user = User.create("1111L", "박동건", "icebearrrr", "imgUrl1");

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
            .alarmTime("ONE_DAY_BEFORE")
            .build();

        // stubbing
        given(scheduleService.createSchedule(anyLong(), eq(request)))
            .willReturn(ScheduleCreateResponse.builder().scheduleSeq(1L).build());

        // when  // then
        mockMvc.perform(
                post("/api/schedule")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.data.scheduleSeq").value(1L));
    }

    @DisplayName("서버 시간을 조회한다.")
    @Test
    void getServerTime() throws Exception {
        // stubbing
        String currentTime = String.valueOf(LocalDate.now());
        given(scheduleService.getServerDate())
            .willReturn(currentTime);

        // then
        mockMvc.perform(
                get("/api/schedule/server-time")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.data").value(currentTime));
    }

    @DisplayName("사용자의 단일 일정을 조회한다.")
    @Test
    void getSchedule() throws Exception {
        // stubbing
        String currentTime = String.valueOf(LocalDate.now());
        given(scheduleService.getServerDate())
            .willReturn(currentTime);

        mockMvc.perform(
                get("/api/schedule/{scheduleSeq}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"));
    }

    @DisplayName("사용자의 전체 일정을 조회한다.")
    @Test
    void getAllScheduleList() throws Exception {
        // stubbing
        String currentTime = String.valueOf(LocalDate.now());
        given(scheduleService.getServerDate())
            .willReturn(currentTime);

        mockMvc.perform(
                get("/api/schedule")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.data").isMap());
    }

    @DisplayName("query string을 통해 입력받은 조건에 맞는 일정 목록을 조회한다.")
    @Test
    void getScheduleListByConditions() throws Exception {
        // stubbing
        String currentTime = String.valueOf(LocalDate.now());
        given(scheduleService.getServerDate())
            .willReturn(currentTime);

        // given
        String categorySeqList = "1,2";
        String searchKey = "합창단";
        String unscheduled = "true";
        String year = "2023";
        String month = "11";
        String day = "18";

        mockMvc.perform(
                get("/api/schedule?category={}&searchKey={}&unscheduled={}&year={}&month={}&day={}",
                    categorySeqList, searchKey, unscheduled, year, month, day)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.data").isMap());

    }

    @DisplayName("카테고리에 해당하는 일정 목록을 조회한다.")
    @Test
    void getScheduleListByCategoryList() throws Exception {
        // given
        String categorySeqList = "1,2";

        // stubbing
        String currentTime = String.valueOf(LocalDate.now());
        given(scheduleService.getServerDate())
            .willReturn(currentTime);

        // when // then
        mockMvc.perform(
                get("/api/schedule?category={}", categorySeqList)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.data").isMap());
    }

    @DisplayName("이름으로 일정을 검색한다.")
    @Test
    void getScheduleListByScheduleName() throws Exception {
        // given
        String searchKey = "합창단";

        // stubbing
        String currentTime = String.valueOf(LocalDate.now());
        given(scheduleService.getServerDate())
            .willReturn(currentTime);

        // when // then
        mockMvc.perform(
                get("/api/schedule?searchKey={}", searchKey)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.data").isMap());
    }

    @DisplayName("사용자의 미확정 일정을 조회한다.")
    @Test
    void getUnscheduledScheduleList() throws Exception {
        // given
        String unscheduled = "true";

        // stubbing
        String currentTime = String.valueOf(LocalDate.now());
        given(scheduleService.getServerDate())
            .willReturn(currentTime);

        // when // then
        mockMvc.perform(
                get("/api/schedule?unscheduled={}", unscheduled)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.data").isMap());
    }

    @DisplayName("날짜 조건에 맞는 일정 목록을 가져온다.")
    @Test
    void getScheduleListByDateCondition() throws Exception {
        // given
        String year = "2023";
        String month = "11";
        String day = "18";

        // stubbing
        String currentTime = String.valueOf(LocalDate.now());
        given(scheduleService.getServerDate())
            .willReturn(currentTime);

        // when // then
        mockMvc.perform(
                get("/api/schedule?year={}&month={}&day={}", year, month, day)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.data").isMap());
    }

    @DisplayName("일정 내 참가자 명단에서 사용자를 검색한다.")
    @Test
    void getParticipatingUserListBySearchKey() throws Exception {
        mockMvc.perform(
                get("/api/schedule/{scheduleSeq}/participation?username={}", 1, "박동건")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.data").isArray());
    }

    @DisplayName("일정의 동기화 부분을 수정한다.")
    @Test
    void updateSchedule() throws Exception {
        User user = User.create("111L", "박동건", "icebearrrr", "imgUrl1");
        Long scheduleSeq = 123L;

        ScheduleUpdateRequest request = ScheduleUpdateRequest.builder()
            // schedule
            .scheduleContent("수원 시민 회관")
            .isDateSelected(true)
            .isTimeSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-11-18 18:30:00")
            .scheduleEndAt("2023-11-18 20:30:00")
            .participants(
                List.of(UserReadResponse.of(user)))
            .build();

        // stubbing
        given(scheduleService.updateSchedule(anyLong(), anyLong(),
            any(ScheduleUpdateRequest.class)))
            .willReturn(ScheduleUpdateResponse.builder().scheduleSeq(scheduleSeq).build());

        mockMvc.perform(put("/api/schedule/{scheduleSeq}", scheduleSeq)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"));
    }

    @DisplayName("일정의 비동기화 부분을 수정한다.")
    @ParameterizedTest
    @ValueSource(strings = {"ONE_DAY_BEFORE", "ONE_HOUR_BEFORE", "THIRTY_MINUTES_BEFORE",
        "FIFTEEN_MINUTES_BEFORE", "ON_TIME", "NONE"})
    void updateParticipation(String alarmTime) throws Exception {
        Long scheduleSeq = 123L;
        Long categorySeq = 1L;

        ParticipationUpdateRequest request = ParticipationUpdateRequest.builder()
            .scheduleName("합창단 공연")
            .scheduleMemo("수원역에서 걸어서 10분")
            .alarmTime(alarmTime)
            .build();

        // stubbing
        given(scheduleService.updateParticipation(anyLong(), anyLong(),
            any(ParticipationUpdateRequest.class)))
            .willReturn(ParticipationUpdateResponse.builder().scheduleSeq(scheduleSeq).build());

        mockMvc.perform(
                put("/api/schedule/{scheduleSeq}/participation", scheduleSeq, categorySeq)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"));
    }

    @DisplayName("일정을 삭제한다.")
    @Test
    void deleteSchedule() throws Exception {
        Long scheduleSeq = anyLong();

        mockMvc.perform(
                delete("/api/schedule/{scheduleSeq}", scheduleSeq)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"));
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
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"));
    }

    @DisplayName("댓글을 삭제한다.")
    @Test
    void deleteComment() throws Exception {
        Long scheduleSeq = 123L;
        Long commentSeq = 1L;

        mockMvc.perform(
                delete("/api/schedule/{scheduleSeq}/comment/{commentSeq}", scheduleSeq, commentSeq)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"));
    }

    @DisplayName("카테고리를 생성한다.")
    @Test
    void createCategory() throws Exception {
        CategoryCreateRequest request = CategoryCreateRequest.builder()
            .categoryName("합창단")
            .categoryColor("GREEN")
            .build();

        mockMvc.perform(
                post("/api/schedule/user/category")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"));
    }

    @DisplayName("사용자의 카테고리 목록을 조회한다.")
    @Test
    void getCategoryList() throws Exception {

        mockMvc.perform(
                get("/api/schedule/user/category")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray());
    }

    @DisplayName("카테고리를 수정한다.")
    @ParameterizedTest
    @ValueSource(strings = {"SALMON", "YELLOW", "CYAN", "ORANGE", "HOTPINK", "GREEN", "GRAY"})
    void updateCategory(String categoryColor) throws Exception {
        Long categorySeq = 123L;
        CategoryUpdateRequest request = CategoryUpdateRequest.builder()
            .categoryName("합창단")
            .categoryColor(categoryColor)
            .build();

        given(scheduleService.updateCategory(anyLong(), eq(categorySeq),
            any(CategoryUpdateRequest.class)))
            .willReturn(CategoryUpdateResponse.builder().categorySeq(categorySeq).build());

        mockMvc.perform(
                put("/api/schedule/user/category/{categorySeq}", categorySeq)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"));
    }

    @DisplayName("카테고리를 삭제한다.")
    @Test
    void deleteCategory() throws Exception {
        Long categorySeq = anyLong();

        mockMvc.perform(
                delete("/api/schedule/user/category/{categorySeq}", categorySeq)
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"));
    }

    // ==================== 실패 테스트 ====================

    @DisplayName("일정 생성 시 일정 제목은 필수로 입력되어야 한다.")
    @Test
    void noScheduleName() throws Exception {

        User user = User.create("1111L", "박동건", "icebearrrr", "imgUrl1");

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
            .alarmTime("ONE_DAY_BEFORE")
            .build();

        mockMvc.perform(
                post("/api/schedule")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.message").value("일정 이름을 입력해 주세요!"));
    }

    @DisplayName("일정 생성 시 날짜 확정 여부를 입력해야 한다.")
    @Test
    void noScheduleDateSelectedInfo() throws Exception {
        // given
        User user = User.create("1111L", "박동건", "icebearrrr", "imgUrl1");

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
            .alarmTime("ONE_DAY_BEFORE")
            .build();

        // when  // then
        mockMvc.perform(
                post("/api/schedule")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("날짜 확정 여부가 결정되지 않았어요!"));
    }

    @DisplayName("일정 생성 시 시간 확정 여부를 입력해야 한다.")
    @Test
    void noScheduleTimeSelectedInfo() throws Exception {
        // given
        User user = User.create("1111L", "박동건", "icebearrrr", "imgUrl1");

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
            .alarmTime("ONE_DAY_BEFORE")
            .build();

        // when  // then
        mockMvc.perform(
                post("/api/schedule")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("시간 확정 여부가 결정되지 않았어요!"));
    }

    @DisplayName("일정 생성 시 일정의 하루종일 여부를 전달해야 한다.")
    @Test
    void noScheduleIsAllDayInfo() throws Exception {
        // given
        User user = User.create("1111L", "박동건", "icebearrrr", "imgUrl1");

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
            .alarmTime("ONE_DAY_BEFORE")
            .build();

        // when  // then
        mockMvc.perform(
                post("/api/schedule")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("하루종일 지속되는 일정인지 알려주세요!"));
    }

    @DisplayName("일정 생성 시 권한을 필수로 설정해야 한다.")
    @Test
    void noScheduleAuthorizationInfo() throws Exception {
        // given
        User user = User.create("1111L", "박동건", "icebearrrr", "imgUrl1");

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
            .alarmTime("ONE_DAY_BEFORE")
            .build();

        // when  // then
        mockMvc.perform(
                post("/api/schedule")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("일정에 대한 수정 권한을 설정해 주세요!"));
    }

    @DisplayName("일정 생성시 알림시간 설정은 필수다.")
    @Test
    void noScheduleAlarmTimeInfo() throws Exception {
        // given
        User user = User.create("1111L", "박동건", "icebearrrr", "imgUrl1");

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
            ).andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("알림 시간 설정이 필요해요!"));
    }

    @DisplayName("일정 수정 시 날짜 확정 여부를 입력해야 한다.")
    @Test
    void noScheduleDateSelectedInfoToUpdate() throws Exception {
        // given
        User user = User.create("1111L", "박동건", "icebearrrr", "imgUrl1");

        ScheduleUpdateRequest request = ScheduleUpdateRequest.builder()
            // schedule
            .scheduleContent("수원 시민 회관")
            .isTimeSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-11-18 18:30:00")
            .scheduleEndAt("2023-11-18 20:30:00")
            .participants(List.of(UserReadResponse.of(user)))
            .build();

        // stubbing
        given(scheduleService.updateSchedule(anyLong(), anyLong(),
            any(ScheduleUpdateRequest.class)))
            .willReturn(ScheduleUpdateResponse.builder().scheduleSeq(1L).build());

        // when  // then
        mockMvc.perform(
                put("/api/schedule/{scheduleSeq}", 1L)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("날짜 확정 여부가 결정되지 않았어요!"));
    }

    @DisplayName("일정 수정 시 시간 확정 여부를 입력해야 한다.")
    @Test
    void noScheduleTimeSelectedInfoToUpdate() throws Exception {
        // given
        User user = User.create("1111L", "박동건", "icebearrrr", "imgUrl1");

        ScheduleUpdateRequest request = ScheduleUpdateRequest.builder()
            .scheduleContent("수원 시민 회관")
            .isDateSelected(true)
            .isAllDay(false)
            .scheduleStartAt("2023-11-18 18:30:00")
            .scheduleEndAt("2023-11-18 20:30:00")
            .participants(List.of(UserReadResponse.of(user)))
            .build();

        // stubbing
        given(scheduleService.updateSchedule(anyLong(), anyLong(),
            any(ScheduleUpdateRequest.class)))
            .willReturn(ScheduleUpdateResponse.builder().scheduleSeq(1L).build());

        // when  // then
        mockMvc.perform(
                put("/api/schedule/{scheduleSeq}", 1L)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("시간 확정 여부가 결정되지 않았어요!"));
    }

    @DisplayName("일정 수정 시 일정의 하루종일 여부를 전달해야 한다.")
    @Test
    void noScheduleIsAllDayInfoToUpdate() throws Exception {
        // given
        User user = User.create("1111L", "박동건", "icebearrrr", "imgUrl1");

        ScheduleUpdateRequest request = ScheduleUpdateRequest.builder()
            // schedule
            .scheduleContent("수원 시민 회관")
            .isDateSelected(true)
            .isTimeSelected(true)
            .scheduleStartAt("2023-11-18 18:30:00")
            .scheduleEndAt("2023-11-18 20:30:00")
            .participants(List.of(UserReadResponse.of(user)))
            .build();

        // stubbing
        given(scheduleService.updateSchedule(anyLong(), anyLong(),
            any(ScheduleUpdateRequest.class)))
            .willReturn(ScheduleUpdateResponse.builder().scheduleSeq(1L).build());

        // when  // then
        mockMvc.perform(
                put("/api/schedule/{scheduleSeq}", 1L)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("하루종일 지속되는 일정인지 알려주세요!"));
    }

    @DisplayName("참가정보 수정 시 일정 제목은 필수로 입력되어야 한다.")
    @Test
    void noScheduleNameToUpdate() throws Exception {
        ParticipationUpdateRequest request = ParticipationUpdateRequest.builder()
            .scheduleMemo("수원역에서 걸어서 10분")
            .alarmTime("NONE")
            .build();

        // stubbing
        given(scheduleService.updateSchedule(anyLong(), anyLong(),
            any(ScheduleUpdateRequest.class)))
            .willReturn(ScheduleUpdateResponse.builder().scheduleSeq(1L).build());

        mockMvc.perform(
                put("/api/schedule/{scheduleSeq}/participation", 1L)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.message").value("일정 이름을 입력해 주세요!"));
    }

    @DisplayName("참가정보 수정 시 알림시간 설정은 필수다.")
    @Test
    void noScheduleAlarmTimeInfoToUpdate() throws Exception {
        ParticipationUpdateRequest request = ParticipationUpdateRequest.builder()
            .scheduleName("합창단 공연")
            .scheduleMemo("수원역에서 걸어서 10분")
            .build();

        // stubbing
        given(scheduleService.updateSchedule(anyLong(), anyLong(),
            any(ScheduleUpdateRequest.class)))
            .willReturn(ScheduleUpdateResponse.builder().scheduleSeq(1L).build());

        mockMvc.perform(
                put("/api/schedule/{scheduleSeq}/participation", 1L)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
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
            ).andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("댓글 내용을 입력해 주세요!"));
    }

    @DisplayName("카테고리 생성시 이름이 필요하다.")
    @Test
    void noCategoryName() throws Exception {
        CategoryCreateRequest request = CategoryCreateRequest.builder()
            .categoryColor("GREEN")
            .build();

        mockMvc.perform(
                post("/api/schedule/user/category")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("카테고리 이름을 입력해 주세요!"));
    }

    @DisplayName("카테고리 생성시 카테고리 색을 지정해야 한다.")
    @Test
    void noCategoryColor() throws Exception {
        CategoryCreateRequest request = CategoryCreateRequest.builder()
            .categoryName("합창단")
            .build();

        mockMvc.perform(
                post("/api/schedule/user/category")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("카테고리 색을 선택해주세요!"));
    }

    @DisplayName("카테고리 수정시 이름이 필요하다.")
    @Test
    void noCategoryNameToUpdate() throws Exception {
        CategoryUpdateRequest request = CategoryUpdateRequest.builder()
            .categoryColor("GREEN")
            .build();

        mockMvc.perform(
                put("/api/schedule/user/category/{categorySeq}", 1L)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("카테고리 이름을 입력해 주세요!"));
    }

    @DisplayName("카테고리 수정시 카테고리 색을 지정해야 한다.")
    @Test
    void noCategoryColorToUpdate() throws Exception {
        CategoryUpdateRequest request = CategoryUpdateRequest.builder()
            .categoryName("합창단")
            .build();

        mockMvc.perform(
                put("/api/schedule/user/category/{categorySeq}", 1L)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("카테고리 색을 설정해 주세요!"));
    }

    @DisplayName("일정의 댓글 알림을 설정한다.")
    @Test
    void setMentionAlarmOnPerSchedule() throws Exception {
        mockMvc.perform(
                post("/api/schedule/1/subscribe/mention")
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").value("일정의 댓글 멘션 알림을 설정합니다."));
    }

    @DisplayName("일정의 댓글 알림을 해제한다.")
    @Test
    void setMentionAlarmOffPerSchedule() throws Exception {
        mockMvc.perform(
                post("/api/schedule/1/unsubscribe/mention")
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").value("일정의 댓글 멘션 알림을 해제합니다."));
    }

    @DisplayName("일정의 수정 알림을 설정한다.")
    @Test
    void setUpdateAlarmOnPerSchedule() throws Exception {
        mockMvc.perform(
                post("/api/schedule/1/subscribe/update")
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").value("일정의 수정 알림을 설정합니다."));
    }

    @DisplayName("일정의 수정 알림을 해제한다.")
    @Test
    void setUpdateAlarmOffPerSchedule() throws Exception {
        mockMvc.perform(
                post("/api/schedule/1/unsubscribe/update")
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").value("일정의 수정 알림을 해제합니다."));
    }

}
