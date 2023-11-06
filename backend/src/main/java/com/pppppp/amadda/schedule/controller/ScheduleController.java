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

        if (categorySeqList.isPresent()) {
            return ApiResponse.ok(
                scheduleService.getScheduleByCategoryList(mockUserSeq, categorySeqList.get()));
        }

        if (searchKey.isPresent()) {
            return ApiResponse.ok(
                scheduleService.getSearchResultByScheduleName(mockUserSeq, searchKey.get()));
        }

        if (unscheduled.isPresent()) {
            return ApiResponse.ok(scheduleService.getUnscheduledScheduleList(mockUserSeq));
        }

        return ApiResponse.ok(scheduleService.getScheduleList(mockUserSeq));
    }

    @GetMapping("/{scheduleSeq}/participation")
    public ApiResponse<List<UserReadResponse>> getParticipatingUsers(
        @PathVariable Long scheduleSeq,
        @RequestParam(value = "userName", required = false) Optional<String> searchKey) {
        log.info("GET /api/schedule/{}/participation?userName={}", scheduleSeq, searchKey);

        if (searchKey.isPresent()) {
            return ApiResponse.ok(
                scheduleService.getParticipatingUserListBySearchKey(scheduleSeq, searchKey.get()));
        }

        return ApiResponse.ok(scheduleService.getParticipatingUserList(scheduleSeq));
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
}
