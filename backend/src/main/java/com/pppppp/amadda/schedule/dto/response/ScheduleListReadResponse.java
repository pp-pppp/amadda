package com.pppppp.amadda.schedule.dto.response;

import com.pppppp.amadda.schedule.entity.AlarmTime;
import com.pppppp.amadda.schedule.entity.Participation;
import com.pppppp.amadda.schedule.entity.Schedule;
import com.pppppp.amadda.user.dto.response.UserReadResponse;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Builder;

@Builder
public record ScheduleListReadResponse(

    Long scheduleSeq,
    String scheduleName,
    List<UserReadResponse> participants,
    String scheduleStartAt,
    String scheduleEndAt,
    Boolean isAllDay,
    Boolean isTimeSelected,
    Boolean isDateSelected,
    AlarmTime alarmTime,
    Boolean isAuthorizedAll,
    UserReadResponse authorizedUser
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
            .alarmTime(participation.getAlarmTime())
            .isAuthorizedAll(schedule.isAuthorizedAll())
            .authorizedUser(authorizedUser)
            .build();
    }

}
