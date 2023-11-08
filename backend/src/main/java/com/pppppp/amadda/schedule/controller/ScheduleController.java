package com.pppppp.amadda.schedule.controller;

import com.pppppp.amadda.alarm.service.AlarmService;
import com.pppppp.amadda.global.dto.ApiResponse;
import com.pppppp.amadda.schedule.dto.request.CategoryCreateRequest;
import com.pppppp.amadda.schedule.dto.request.CommentCreateRequest;
import com.pppppp.amadda.schedule.dto.request.ScheduleCreateRequest;
import com.pppppp.amadda.schedule.dto.response.CategoryReadResponse;
import com.pppppp.amadda.schedule.dto.response.CommentReadResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleCreateResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleDetailReadResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleListReadResponse;
import com.pppppp.amadda.schedule.service.ScheduleService;
import com.pppppp.amadda.user.dto.response.UserReadResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        alarmService.sendScheduleAssigned(scheduleCreateResponse.scheduleSeq());
        return ApiResponse.ok(scheduleCreateResponse);
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
        @RequestParam(value = "unscheduled", required = false) Optional<String> unscheduled) {
        log.info("GET /api/schedule?category={}&searchKey={}&unscheduled={}", categorySeqList,
            searchKey, unscheduled);

        Map<String, String> searchCondition = Map.of("categories", categorySeqList.orElse(""),
            "searchKey", searchKey.orElse(""), "unscheduled", unscheduled.orElse(""));

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

    // ==================== 댓글 ====================

    @PostMapping("/{scheduleSeq}/comment")
    public ApiResponse<CommentReadResponse> createComment(@PathVariable Long scheduleSeq,
        @Valid @RequestBody CommentCreateRequest request) {
        log.info("POST /api/schedule/{}/comment", scheduleSeq);
        return ApiResponse.ok(
            scheduleService.createCommentOnSchedule(scheduleSeq, mockUserSeq, request));
    }

    // ==================== 카테고리 ====================

    @PostMapping("/user/category")
    public ApiResponse<CategoryReadResponse> createCategory(
        @Valid @RequestBody CategoryCreateRequest request) {
        log.info("POST /api/schedule/user/category");
        return ApiResponse.ok(scheduleService.createCategory(mockUserSeq, request));
    }

    @GetMapping("/user/category")
    public ApiResponse<List<CategoryReadResponse>> getCategoryList() {
        log.info("GET /api/schedule/user/category");
        return ApiResponse.ok(scheduleService.getCategoryList(mockUserSeq));
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
