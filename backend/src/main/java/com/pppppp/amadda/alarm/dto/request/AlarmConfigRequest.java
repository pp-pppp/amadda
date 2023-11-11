package com.pppppp.amadda.alarm.dto.request;

import com.pppppp.amadda.alarm.entity.AlarmType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AlarmConfigRequest(
    @NotNull(message = "유효하지 않은 요청입니다.") Long userSeq,
    @NotNull(message = "알림 타입을 입력해 주세요.") AlarmType alarmType) {

}
