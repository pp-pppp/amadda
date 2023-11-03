package com.pppppp.amadda.schedule.dto.request;

import com.pppppp.amadda.schedule.entity.AlarmTime;
import com.pppppp.amadda.schedule.entity.Participation;
import com.pppppp.amadda.schedule.entity.Schedule;
import com.pppppp.amadda.user.dto.response.UserReadResponse;
import com.pppppp.amadda.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Builder;

@Builder
public record ScheduleCreateRequest(
    // ================= Schedule ===================
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
    @NotBlank(message = "일정 이름을 입력해주세요!") String scheduleName,
    String scheduleMemo
) {

    public static ScheduleCreateRequest of(Schedule schedule,
        List<UserReadResponse> participants,
        Participation participation) {
        return ScheduleCreateRequest.builder()
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
            .build();
    }

    // dto to entity
    public Schedule toEntity(User user) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return Schedule.builder()
            .scheduleContent(scheduleContent)
            .isTimeSelected(isTimeSelected)
            .isDateSelected(isDateSelected)
            .isAllDay(isAllDay)
            .scheduleStartAt(
                (isDateSelected) ? LocalDateTime.parse(scheduleStartAt, formatter) : null)
            .scheduleEndAt(
                (isTimeSelected) ? LocalDateTime.parse(scheduleEndAt, formatter) : null)
            .isAuthorizedAll(isAuthorizedAll)
            // 생성한 사용자 정보, isAuthorizedAll == false일 때 authorizedUser로 사용
            .user(user)
            .build();
    }
}