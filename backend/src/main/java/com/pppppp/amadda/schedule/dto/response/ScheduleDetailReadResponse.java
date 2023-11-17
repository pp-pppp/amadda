package com.pppppp.amadda.schedule.dto.response;

import com.pppppp.amadda.schedule.entity.Participation;
import com.pppppp.amadda.schedule.entity.Schedule;
import com.pppppp.amadda.user.dto.response.UserReadResponse;
import java.time.format.DateTimeFormatter;
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
    UserReadResponse authorizedUser,
    List<UserReadResponse> participants,
    Boolean isFinished,

    // ================ Participation =================
    String alarmTime,
    String scheduleName,
    String scheduleMemo,
    CategoryReadResponse category,

    // ================= Comment ===================
    List<CommentReadResponse> comments
) {

    public static ScheduleDetailReadResponse of(Schedule schedule,
        List<UserReadResponse> participants,
        Participation participation, List<CommentReadResponse> comments, Boolean isFinished) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return ScheduleDetailReadResponse.builder()
            .scheduleSeq(schedule.getScheduleSeq())
            .scheduleContent(schedule.getScheduleContent())
            .isTimeSelected(schedule.isTimeSelected())
            .isDateSelected(schedule.isDateSelected())
            .isAllDay(schedule.isAllDay())
            .isAuthorizedAll(schedule.isAuthorizedAll())
            .scheduleStartAt(
                (schedule.isDateSelected()) ? schedule.getScheduleStartAt().format(formatter) : "")
            .scheduleEndAt(
                (schedule.isTimeSelected()) ? schedule.getScheduleEndAt().format(formatter) : "")
            .authorizedUser(
                !(schedule.isAuthorizedAll()) ? UserReadResponse.of(schedule.getAuthorizedUser())
                    : null)
            .participants(participants)
            .isFinished(isFinished)
            .alarmTime(participation.getAlarmTime().getContent())
            .scheduleName(participation.getScheduleName())
            .scheduleMemo(participation.getScheduleMemo())
            .category((participation.getCategory() != null) ?
                CategoryReadResponse.of(participation.getCategory()) : null)
            .comments(comments)
            .build();
    }
}
