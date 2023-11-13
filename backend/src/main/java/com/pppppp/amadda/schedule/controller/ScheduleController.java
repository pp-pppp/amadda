package com.pppppp.amadda.schedule.controller;

import com.pppppp.amadda.global.dto.ApiResponse;
import com.pppppp.amadda.global.util.TokenProvider;
import com.pppppp.amadda.schedule.dto.request.CategoryCreateRequest;
import com.pppppp.amadda.schedule.dto.request.CategoryUpdateRequest;
import com.pppppp.amadda.schedule.dto.request.CommentCreateRequest;
import com.pppppp.amadda.schedule.dto.request.ParticipationUpdateRequest;
import com.pppppp.amadda.schedule.dto.request.ScheduleCreateRequest;
import com.pppppp.amadda.schedule.dto.request.ScheduleUpdateRequest;
import com.pppppp.amadda.schedule.dto.response.CategoryCreateResponse;
import com.pppppp.amadda.schedule.dto.response.CategoryReadResponse;
import com.pppppp.amadda.schedule.dto.response.CategoryUpdateResponse;
import com.pppppp.amadda.schedule.dto.response.ParticipationUpdateResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleCreateResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleDetailReadResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleListReadResponse;
import com.pppppp.amadda.schedule.dto.response.ScheduleUpdateResponse;
import com.pppppp.amadda.schedule.service.ScheduleService;
import com.pppppp.amadda.user.dto.response.UserReadResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.LocalDate;
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
    private final TokenProvider tokenProvider;

    // ==================== 일정 ====================

    @PostMapping("")
    public ApiResponse<ScheduleCreateResponse> createSchedule(HttpServletRequest http,
        @Valid @RequestBody ScheduleCreateRequest request) {
        log.info("POST /api/schedule");
        Long userSeq = tokenProvider.getUserSeq(http);
        ScheduleCreateResponse scheduleCreateResponse = scheduleService.createSchedule(userSeq,
            request);
        return ApiResponse.ok(scheduleCreateResponse);
    }

    @GetMapping("/server-time")
    public ApiResponse<String> getServerTime() {
        log.info("GET /api/schedule/server-time");
        log.info("time: {}", scheduleService.getServerDate());
        return ApiResponse.ok(scheduleService.getServerDate());
    }

    @GetMapping("/{scheduleSeq}")
    public ApiResponse<ScheduleDetailReadResponse> getSchedule(HttpServletRequest http,
        @PathVariable Long scheduleSeq) {
        log.info("GET /api/schedule/" + scheduleSeq);
        Long userSeq = tokenProvider.getUserSeq(http);

        LocalDate currentServerDate = LocalDate.parse(scheduleService.getServerDate());

        return ApiResponse.ok(
            scheduleService.getScheduleDetail(scheduleSeq, userSeq, currentServerDate));
    }

    @GetMapping("")
    public ApiResponse<Map<String, List<ScheduleListReadResponse>>> getScheduleList(
        HttpServletRequest http,
        @RequestParam(value = "category", required = false) Optional<String> categorySeqList,
        @RequestParam(value = "search-key", required = false) Optional<String> searchKey,
        @RequestParam(value = "unscheduled", required = false) Optional<String> unscheduled,
        @RequestParam(value = "year", required = false) Optional<String> year,
        @RequestParam(value = "month", required = false) Optional<String> month,
        @RequestParam(value = "day", required = false) Optional<String> day) {
        log.info(
            "GET /api/schedule?category={}&search-key={}&unscheduled={}&year={}&month={}&day={}",
            categorySeqList, searchKey, unscheduled, year, month, day);

        LocalDate currentServerTime = LocalDate.parse(scheduleService.getServerDate());

        Map<String, String> searchCondition = Map.of("categories", categorySeqList.orElse(""),
            "searchKey", searchKey.orElse(""), "unscheduled", unscheduled.orElse(""), "year",
            year.orElse(""), "month", month.orElse(""), "day", day.orElse(""));
        Long userSeq = tokenProvider.getUserSeq(http);
        return ApiResponse.ok(scheduleService.getScheduleListByCondition(userSeq, searchCondition,
            currentServerTime));
    }

    @GetMapping("/{scheduleSeq}/participation")
    public ApiResponse<List<UserReadResponse>> getParticipatingUsers(@PathVariable Long scheduleSeq,
        @RequestParam(value = "userName", required = false) Optional<String> searchKey) {
        log.info("GET /api/schedule/{}/participation?username={}", scheduleSeq, searchKey);

        // searchKey가 존재하면 검색 결과를, 존재하지 않으면 전체 참여자 목록을 반환
        return searchKey.map(s -> ApiResponse.ok(
                scheduleService.getParticipatingUserListBySearchKey(scheduleSeq, s)))
            .orElseGet(() -> ApiResponse.ok(scheduleService.getParticipatingUserList(scheduleSeq)));
    }

    @PutMapping("/{scheduleSeq}")
    public ApiResponse<ScheduleUpdateResponse> updateSchedule(HttpServletRequest http,
        @PathVariable Long scheduleSeq, @Valid @RequestBody ScheduleUpdateRequest request) {
        log.info("PUT /api/schedule/{}", scheduleSeq);
        Long userSeq = tokenProvider.getUserSeq(http);
        ScheduleUpdateResponse response = scheduleService.updateSchedule(userSeq, scheduleSeq,
            request);
        return ApiResponse.of(HttpStatus.OK, "해당 일정을 수정했습니다.", response);

    }

    @PutMapping("/{scheduleSeq}/participation")
    public ApiResponse<ParticipationUpdateResponse> updateParticipation(HttpServletRequest http,
        @PathVariable Long scheduleSeq, @Valid @RequestBody ParticipationUpdateRequest request) {
        log.info("PUT /api/schedule/{}/participation", scheduleSeq);
        Long userSeq = tokenProvider.getUserSeq(http);
        ParticipationUpdateResponse response = scheduleService.updateParticipation(userSeq,
            scheduleSeq,
            request);
        return ApiResponse.of(HttpStatus.OK, "해당 일정을 수정했습니다.", response);

    }

    @DeleteMapping("/{scheduleSeq}")
    public ApiResponse<String> deleteSchedule(HttpServletRequest http,
        @PathVariable Long scheduleSeq) {
        log.info("DELETE /api/schedule/{}", scheduleSeq);
        Long userSeq = tokenProvider.getUserSeq(http);
        scheduleService.deleteSchedule(userSeq, scheduleSeq);
        return ApiResponse.ok("해당 일정을 삭제했습니다.");
    }

    // ==================== 댓글 ====================

    @PostMapping("/{scheduleSeq}/comment")
    public ApiResponse<String> createComment(HttpServletRequest http,
        @PathVariable Long scheduleSeq, @Valid @RequestBody CommentCreateRequest request) {
        log.info("POST /api/schedule/{}/comment", scheduleSeq);
        Long userSeq = tokenProvider.getUserSeq(http);
        scheduleService.createCommentOnSchedule(scheduleSeq, userSeq, request);
        return ApiResponse.ok("댓글이 생성되었습니다.");
    }

    @DeleteMapping("/{scheduleSeq}/comment/{commentSeq}")
    public ApiResponse<String> deleteComment(HttpServletRequest http,
        @PathVariable Long scheduleSeq, @PathVariable Long commentSeq) {
        log.info("DELETE /api/schedule/{}/comment/{}", scheduleSeq, commentSeq);
        Long userSeq = tokenProvider.getUserSeq(http);
        scheduleService.deleteComment(commentSeq, userSeq);
        return ApiResponse.ok("댓글이 삭제되었습니다.");
    }

    // ==================== 카테고리 ====================

    @PostMapping("/user/category")
    public ApiResponse<CategoryCreateResponse> createCategory(HttpServletRequest http,
        @Valid @RequestBody CategoryCreateRequest request) {
        log.info("POST /api/schedule/user/category");
        Long userSeq = tokenProvider.getUserSeq(http);
        return ApiResponse.ok(scheduleService.createCategory(userSeq, request));
    }

    @GetMapping("/user/category")
    public ApiResponse<List<CategoryReadResponse>> getCategoryList(HttpServletRequest http) {
        log.info("GET /api/schedule/user/category");
        Long userSeq = tokenProvider.getUserSeq(http);
        return ApiResponse.ok(scheduleService.getCategoryList(userSeq));
    }

    @PutMapping("/user/category/{categorySeq}")
    public ApiResponse<CategoryUpdateResponse> updateCategory(HttpServletRequest http,
        @PathVariable Long categorySeq, @Valid @RequestBody CategoryUpdateRequest request) {
        log.info("PUT /api/schedule/user/category/{}", categorySeq);
        Long userSeq = tokenProvider.getUserSeq(http);
        return ApiResponse.ok(scheduleService.updateCategory(userSeq, categorySeq, request));
    }

    @DeleteMapping("/user/category/{categorySeq}")
    public ApiResponse<String> deleteCategory(HttpServletRequest http,
        @PathVariable Long categorySeq) {
        log.info("DELETE /api/schedule/user/category/{}", categorySeq);
        Long userSeq = tokenProvider.getUserSeq(http);
        scheduleService.deleteCategory(userSeq, categorySeq);
        return ApiResponse.ok("삭제되었습니다.");
    }

    // ==================== 개별 알림 설정 ====================

    @PostMapping("/{scheduleSeq}/subscribe/mention")
    public ApiResponse<String> subscribeMention(HttpServletRequest http,
        @PathVariable Long scheduleSeq) {
        log.info("POST /api/schedule/{}/subscribe/mention", scheduleSeq);
        Long userSeq = tokenProvider.getUserSeq(http);
        scheduleService.setMentionAlarm(userSeq, scheduleSeq, true);
        return ApiResponse.ok("일정의 댓글 멘션 알림을 설정합니다.");
    }

    @PostMapping("/{scheduleSeq}/unsubscribe/mention")
    public ApiResponse<String> unsubscribeMention(HttpServletRequest http,
        @PathVariable Long scheduleSeq) {
        log.info("POST /api/schedule/{}/unsubscribe/mention", scheduleSeq);
        Long userSeq = tokenProvider.getUserSeq(http);
        scheduleService.setMentionAlarm(userSeq, scheduleSeq, false);
        return ApiResponse.ok("일정의 댓글 멘션 알림을 해제합니다.");
    }

    @PostMapping("/{scheduleSeq}/subscribe/update")
    public ApiResponse<String> subscribeUpdate(HttpServletRequest http,
        @PathVariable Long scheduleSeq) {
        log.info("POST /api/schedule/{}/subscribe/update", scheduleSeq);
        Long userSeq = tokenProvider.getUserSeq(http);
        scheduleService.setUpdateAlarm(userSeq, scheduleSeq, true);
        return ApiResponse.ok("일정의 수정 알림을 설정합니다.");
    }

    @PostMapping("/{scheduleSeq}/unsubscribe/update")
    public ApiResponse<String> unsubscribeUpdate(HttpServletRequest http,
        @PathVariable Long scheduleSeq) {
        log.info("POST /api/schedule/{}/unsubscribe/update", scheduleSeq);
        Long userSeq = tokenProvider.getUserSeq(http);
        scheduleService.setUpdateAlarm(userSeq, scheduleSeq, false);
        return ApiResponse.ok("일정의 수정 알림을 해제합니다.");
    }
}
