package com.pppppp.amadda.schedule.dto.response;

import com.pppppp.amadda.schedule.entity.Participation;
import com.pppppp.amadda.schedule.entity.Schedule;
import com.pppppp.amadda.user.dto.response.UserReadResponse;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Builder;

@Builder
public record ScheduleListReadResponse(
    // ================= Schedule ===================
    Long scheduleSeq,
    Boolean isTimeSelected,
    Boolean isDateSelected,
    Boolean isAllDay,
    String scheduleStartAt,
    String scheduleEndAt,
    Boolean isAuthorizedAll,
    UserReadResponse authorizedUser,
    List<UserReadResponse> participants,
    Boolean isFinished,

    // ================ Participation =================

    String alarmTime,
    String scheduleName,
    CategoryReadResponse category
) {

    public static ScheduleListReadResponse of(Schedule schedule,
        UserReadResponse authorizedUser, List<UserReadResponse> participants,
        Participation participation) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return ScheduleListReadResponse.builder()
            .scheduleSeq(schedule.getScheduleSeq())
            .scheduleName(participation.getScheduleName())
            .participants(participants)
            .isTimeSelected(schedule.isTimeSelected())
            .isDateSelected(schedule.isDateSelected())
            .isAllDay(schedule.isAllDay())
            .scheduleStartAt(
                (schedule.isDateSelected()) ? schedule.getScheduleStartAt().format(formatter) : "")
            .scheduleEndAt(
                (schedule.isTimeSelected()) ? schedule.getScheduleEndAt().format(formatter) : "")
            .alarmTime(participation.getAlarmTime().getContent())
            .isAuthorizedAll(schedule.isAuthorizedAll())
            .authorizedUser(authorizedUser)
            .category((participation.getCategory() != null) ?
                CategoryReadResponse.of(participation.getCategory()) : null)
            .build();
    }

    public static ScheduleListReadResponse of(Schedule schedule,
        UserReadResponse authorizedUser, List<UserReadResponse> participants,
        Participation participation, Boolean isFinished) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return ScheduleListReadResponse.builder()
            .scheduleSeq(schedule.getScheduleSeq())
            .isTimeSelected(schedule.isTimeSelected())
            .isDateSelected(schedule.isDateSelected())
            .isAllDay(schedule.isAllDay())
            .scheduleStartAt(
                (schedule.isDateSelected()) ? schedule.getScheduleStartAt().format(formatter) : "")
            .scheduleEndAt(
                (schedule.isTimeSelected()) ? schedule.getScheduleEndAt().format(formatter) : "")
            .isAuthorizedAll(schedule.isAuthorizedAll())
            .authorizedUser(authorizedUser)
            .participants(participants)
            .isFinished(isFinished)
            .alarmTime(participation.getAlarmTime().getContent())
            .scheduleName(participation.getScheduleName())
            .category((participation.getCategory() != null) ?
                CategoryReadResponse.of(participation.getCategory()) : null)
            .build();
    }
}
