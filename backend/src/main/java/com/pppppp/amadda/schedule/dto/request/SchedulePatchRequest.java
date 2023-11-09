package com.pppppp.amadda.schedule.dto.request;

import com.pppppp.amadda.schedule.entity.AlarmTime;
import com.pppppp.amadda.user.dto.response.UserReadResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

@Builder
public record SchedulePatchRequest(
    // ================= Schedule ===================
    @NotNull(message = "유효하지 않은 요청입니다.") String scheduleContent,
    @NotNull(message = "시간 확정 여부가 결정되지 않았어요!") Boolean isTimeSelected,
    @NotNull(message = "날짜 확정 여부가 결정되지 않았어요!") Boolean isDateSelected,
    @NotNull(message = "하루종일 지속되는 일정인지 알려주세요!") Boolean isAllDay,
    @NotNull(message = "유효하지 않은 요청입니다.") String scheduleStartAt,
    @NotNull(message = "유효하지 않은 요청입니다.") String scheduleEndAt,
    @NotNull(message = "유효하지 않은 요청입니다.") List<UserReadResponse> participants,

    // ================ Participation =================

    @NotNull(message = "알림 시간 설정이 필요해요!") AlarmTime alarmTime,
    @NotBlank(message = "일정 이름을 입력해주세요!") String scheduleName,
    @NotNull(message = "유효하지 않은 요청입니다.") String scheduleMemo,
    Long categorySeq
) {

}
