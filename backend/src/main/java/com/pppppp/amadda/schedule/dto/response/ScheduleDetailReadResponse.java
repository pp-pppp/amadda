package com.pppppp.amadda.schedule.dto.response;

import com.pppppp.amadda.schedule.entity.Participation;
import com.pppppp.amadda.schedule.entity.Schedule;
import com.pppppp.amadda.user.dto.response.UserReadResponse;
import java.util.List;
import lombok.Builder;

@Builder
public record ScheduleDetailReadResponse(
    // ================= Schedule ===================
    Long scheduleSeq,
    String scheduleContent,
    Boolean isTimeSelected,
    Boolean isDateSelected,
    Boolean isAllDay,
    Boolean isAuthorizedAll,
    String scheduleStartAt,
    String scheduleEndAt,
    List<UserReadResponse> participants,

    // ================ Participation =================

    String alarmTime,
    String scheduleName,
    String scheduleMemo,
    CategoryReadResponse category,

    // ================= Comment ===================

    List<CommentReadResponse> comments
) {

    // TODO: 카테고리 관련 로직 수정 필요
    public static ScheduleDetailReadResponse of(Schedule schedule,
        List<UserReadResponse> participants,
        Participation participation,
        List<CommentReadResponse> comments) {
        return ScheduleDetailReadResponse.builder()
            .scheduleSeq(schedule.getScheduleSeq())
            .scheduleContent(schedule.getScheduleContent())
            .isTimeSelected(schedule.isTimeSelected())
            .isDateSelected(schedule.isDateSelected())
            .isAllDay(schedule.isAllDay())
            .isAuthorizedAll(schedule.isAuthorizedAll())
            .scheduleStartAt(String.valueOf(schedule.getScheduleStartAt()))
            .scheduleEndAt(String.valueOf(schedule.getScheduleEndAt()))
            .participants(participants)
            .alarmTime(participation.getAlarmTime().getContent())
            .scheduleName(participation.getScheduleName())
            .scheduleMemo(participation.getScheduleMemo())
            .category((participation.getCategory() != null) ? CategoryReadResponse.of(
                participation.getCategory()) : null)
            .comments(comments)
            .build();
    }
}
