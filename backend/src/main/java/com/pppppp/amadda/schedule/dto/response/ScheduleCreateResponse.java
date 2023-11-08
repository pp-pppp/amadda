package com.pppppp.amadda.schedule.dto.response;

import com.pppppp.amadda.schedule.entity.AlarmTime;
import com.pppppp.amadda.schedule.entity.Category;
import com.pppppp.amadda.schedule.entity.Participation;
import com.pppppp.amadda.schedule.entity.Schedule;
import com.pppppp.amadda.user.dto.response.UserReadResponse;
import java.util.List;
import lombok.Builder;

@Builder
public record ScheduleCreateResponse(
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

    AlarmTime alarmTime,
    String scheduleName,
    String scheduleMemo,
    Category category
) {

    public static ScheduleCreateResponse of(Schedule schedule,
        List<UserReadResponse> participants,
        Participation participation) {
        return ScheduleCreateResponse.builder()
            .scheduleSeq(schedule.getScheduleSeq())
            .scheduleContent(schedule.getScheduleContent())
            .isTimeSelected(schedule.isTimeSelected())
            .isDateSelected(schedule.isDateSelected())
            .isAllDay(schedule.isAllDay())
            .isAuthorizedAll(schedule.isAuthorizedAll())
            .scheduleStartAt(String.valueOf(schedule.getScheduleStartAt()))
            .scheduleEndAt(String.valueOf(schedule.getScheduleEndAt()))
            .participants(participants)
            .alarmTime(participation.getAlarmTime())
            .scheduleName(participation.getScheduleName())
            .scheduleMemo(participation.getScheduleMemo())
            .category(participation.getCategory())
            .build();
    }
}
