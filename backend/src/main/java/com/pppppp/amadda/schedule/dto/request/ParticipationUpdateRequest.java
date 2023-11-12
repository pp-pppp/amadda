package com.pppppp.amadda.schedule.dto.request;

import com.pppppp.amadda.schedule.entity.AlarmTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ParticipationUpdateRequest(
    @NotNull(message = "알림 시간 설정이 필요해요!") AlarmTime alarmTime,
    @NotBlank(message = "일정 이름을 입력해 주세요!") String scheduleName,
    @NotNull(message = "유효하지 않은 요청입니다.") String scheduleMemo,
    Long categorySeq
) {

}
