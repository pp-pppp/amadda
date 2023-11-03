package com.pppppp.amadda.schedule.dto.request;

import com.pppppp.amadda.schedule.entity.AlarmTime;
import com.pppppp.amadda.schedule.entity.Participation;
import com.pppppp.amadda.schedule.entity.Schedule;
import com.pppppp.amadda.user.dto.response.UserReadResponse;
import com.pppppp.amadda.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Builder;

@Builder
public record ScheduleCreateRequest(
    // ================= Schedule ===================
    String scheduleContent,
    @NotNull(message = "시간 확정 여부가 결정되지 않았어요!") Boolean isTimeSelected,
    @NotNull(message = "날짜 확정 여부가 결정되지 않았어요!") Boolean isDateSelected,
    @NotNull(message = "하루종일 지속되는 일정인지 알려주세요!") Boolean isAllDay,
    @NotNull(message = "일정에 대한 수정 권한이 필요해요!") Boolean isAuthorizedAll,
    String scheduleStartAt,
    String scheduleEndAt,
    List<UserReadResponse> participants,

    // ================ Participation =================

    @NotNull(message = "알림 시간 설정이 필요해요!") AlarmTime alarmTime,
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
