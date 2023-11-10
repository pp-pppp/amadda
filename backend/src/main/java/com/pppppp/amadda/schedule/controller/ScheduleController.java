package com.pppppp.amadda.schedule.controller;

import com.pppppp.amadda.alarm.service.AlarmService;
import com.pppppp.amadda.global.dto.ApiResponse;
import com.pppppp.amadda.schedule.dto.request.CategoryCreateRequest;
import com.pppppp.amadda.schedule.dto.request.CategoryUpdateRequest;
import com.pppppp.amadda.schedule.dto.request.CommentCreateRequest;
import com.pppppp.amadda.schedule.dto.request.ScheduleCreateRequest;
import com.pppppp.amadda.schedule.dto.request.ScheduleUpdateRequest;
import com.pppppp.amadda.schedule.dto.response.CategoryCreateResponse;
import com.pppppp.amadda.schedule.dto.response.CategoryReadResponse;
import com.pppppp.amadda.schedule.dto.response.CategoryUpdateResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleCreateResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleDetailReadResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleListReadResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleUpdateResponse;
import com.pppppp.amadda.schedule.service.ScheduleService;
import com.pppppp.amadda.user.dto.response.UserReadResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final AlarmService alarmService;

    private final Long mockUserSeq = 1111L;

    // TODO: 로그인 구현 후 코드 수정

    // ==================== 일정 ====================

    @PostMapping("")
    public ApiResponse<ScheduleCreateResponse> createSchedule(
        @Valid @RequestBody ScheduleCreateRequest request) {
        log.info("POST /api/schedule");
        ScheduleCreateResponse scheduleCreateResponse = scheduleService.createSchedule(mockUserSeq,
            request);
        return ApiResponse.ok(scheduleCreateResponse);
    }

    @GetMapping("/serverTime")
    public ApiResponse<String> getServerTime() {
        log.info("GET /api/schedule/serverTime");
        log.info("time: {}", scheduleService.getServerTime());
        return ApiResponse.ok(scheduleService.getServerTime());
    }

    @GetMapping("/{scheduleSeq}")
    public ApiResponse<ScheduleDetailReadResponse> getSchedule(@PathVariable Long scheduleSeq) {
        log.info("GET /api/schedule/" + scheduleSeq);
        return ApiResponse.ok(scheduleService.getScheduleDetail(scheduleSeq, mockUserSeq));
    }

    @GetMapping("")
    public ApiResponse<List<ScheduleListReadResponse>> getScheduleList(
        @RequestParam(value = "category", required = false) Optional<String> categorySeqList,
        @RequestParam(value = "searchKey", required = false) Optional<String> searchKey,
        @RequestParam(value = "unscheduled", required = false) Optional<String> unscheduled,
        @RequestParam(value = "year", required = false) Optional<String> year,
        @RequestParam(value = "month", required = false) Optional<String> month,
        @RequestParam(value = "day", required = false) Optional<String> day) {
        log.info(
            "GET /api/schedule?category={}&searchKey={}&unscheduled={}&year={}&month={}&day={}",
            categorySeqList,
            searchKey, unscheduled, year, month, day);

        Map<String, String> searchCondition = Map.of("categories", categorySeqList.orElse(""),
            "searchKey", searchKey.orElse(""), "unscheduled", unscheduled.orElse(""), "year",
            year.orElse(""), "month",
            month.orElse(""), "day", day.orElse(""));

        return ApiResponse.ok(scheduleService.getScheduleListBySearchCondition(mockUserSeq,
            searchCondition));
    }

    @GetMapping("/{scheduleSeq}/participation")
    public ApiResponse<List<UserReadResponse>> getParticipatingUsers(
        @PathVariable Long scheduleSeq,
        @RequestParam(value = "userName", required = false) Optional<String> searchKey) {
        log.info("GET /api/schedule/{}/participation?userName={}", scheduleSeq, searchKey);

        // searchKey가 존재하면 검색 결과를, 존재하지 않으면 전체 참여자 목록을 반환
        return searchKey.map(s -> ApiResponse.ok(
                scheduleService.getParticipatingUserListBySearchKey(scheduleSeq, s)))
            .orElseGet(() -> ApiResponse.ok(scheduleService.getParticipatingUserList(scheduleSeq)));
    }

    @PutMapping("/{scheduleSeq}")
    public ApiResponse<ScheduleUpdateResponse> updateSchedule(@PathVariable Long scheduleSeq,
        @Valid @RequestBody ScheduleUpdateRequest request) {
        log.info("PUT /api/schedule/{}", scheduleSeq);
        ScheduleUpdateResponse response = scheduleService.updateSchedule(
            mockUserSeq, scheduleSeq, request);
        alarmService.sendScheduleUpdate(response.scheduleSeq(), mockUserSeq);
        return ApiResponse.of(HttpStatus.OK, "수정되었습니다.", response);
    }

    @DeleteMapping("/{scheduleSeq}")
    public ApiResponse<String> deleteSchedule(@PathVariable Long scheduleSeq) {
        log.info("DELETE /api/schedule/{}", scheduleSeq);
        scheduleService.deleteSchedule(mockUserSeq, scheduleSeq);
        return ApiResponse.ok("삭제되었습니다.");
    }

    // ==================== 댓글 ====================

    @PostMapping("/{scheduleSeq}/comment")
    public ApiResponse<String> createComment(@PathVariable Long scheduleSeq,
        @Valid @RequestBody CommentCreateRequest request) {
        log.info("POST /api/schedule/{}/comment", scheduleSeq);
        scheduleService.createCommentOnSchedule(scheduleSeq, mockUserSeq, request);
        return ApiResponse.ok("댓글이 생성되었습니다.");
    }

    @DeleteMapping("/{scheduleSeq}/comment/{commentSeq}")
    public ApiResponse<String> deleteComment(@PathVariable Long scheduleSeq,
        @PathVariable Long commentSeq) {
        log.info("DELETE /api/schedule/{}/comment/{}", scheduleSeq, commentSeq);
        scheduleService.deleteComment(commentSeq, mockUserSeq);
        return ApiResponse.ok("삭제되었습니다.");
    }

    // ==================== 카테고리 ====================

    @PostMapping("/user/category")
    public ApiResponse<CategoryCreateResponse> createCategory(
        @Valid @RequestBody CategoryCreateRequest request) {
        log.info("POST /api/schedule/user/category");
        return ApiResponse.ok(scheduleService.createCategory(mockUserSeq, request));
    }

    @GetMapping("/user/category")
    public ApiResponse<List<CategoryReadResponse>> getCategoryList() {
        log.info("GET /api/schedule/user/category");
        return ApiResponse.ok(scheduleService.getCategoryList(mockUserSeq));
    }

    @PutMapping("/user/category/{categorySeq}")
    public ApiResponse<CategoryUpdateResponse> updateCategory(@PathVariable Long categorySeq,
        @Valid @RequestBody CategoryUpdateRequest request) {
        log.info("PUT /api/schedule/user/category/{}", categorySeq);
        return ApiResponse.ok(
            scheduleService.updateCategory(mockUserSeq, categorySeq, request));
    }

    @DeleteMapping("/user/category/{categorySeq}")
    public ApiResponse<String> deleteCategory(@PathVariable Long categorySeq) {
        log.info("DELETE /api/schedule/user/category/{}", categorySeq);
        scheduleService.deleteCategory(mockUserSeq, categorySeq);
        return ApiResponse.ok("삭제되었습니다.");
    }

    // ==================== 개별 알림 설정 ====================

    @PostMapping("/{scheduleSeq}/subscribe/mention")
    public ApiResponse<String> subscribeMention(@PathVariable Long scheduleSeq) {
        log.info("POST /api/schedule/{}/subscribe/mention", scheduleSeq);
        Long userSeq = 1L;
        scheduleService.setMentionAlarm(userSeq, scheduleSeq, true);
        return ApiResponse.ok("일정의 댓글 멘션 알림을 설정합니다.");
    }

    @PostMapping("/{scheduleSeq}/unsubscribe/mention")
    public ApiResponse<String> unsubscribeMention(@PathVariable Long scheduleSeq) {
        log.info("POST /api/schedule/{}/unsubscribe/mention", scheduleSeq);
        Long userSeq = 1L;
        scheduleService.setMentionAlarm(userSeq, scheduleSeq, false);
        return ApiResponse.ok("일정의 댓글 멘션 알림을 해제합니다.");
    }

    @PostMapping("/{scheduleSeq}/subscribe/update")
    public ApiResponse<String> subscribeUpdate(@PathVariable Long scheduleSeq) {
        log.info("POST /api/schedule/{}/subscribe/update", scheduleSeq);
        Long userSeq = 1L;
        scheduleService.setUpdateAlarm(userSeq, scheduleSeq, true);
        return ApiResponse.ok("일정의 수정 알림을 설정합니다.");
    }

    @PostMapping("/{scheduleSeq}/unsubscribe/update")
    public ApiResponse<String> unsubscribeUpdate(@PathVariable Long scheduleSeq) {
        log.info("POST /api/schedule/{}/unsubscribe/update", scheduleSeq);
        Long userSeq = 1L;
        scheduleService.setUpdateAlarm(userSeq, scheduleSeq, false);
        return ApiResponse.ok("일정의 수정 알림을 해제합니다.");
    }
}
