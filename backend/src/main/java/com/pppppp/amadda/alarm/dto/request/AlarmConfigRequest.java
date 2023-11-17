package com.pppppp.amadda.alarm.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AlarmConfigRequest(
    @NotNull(message = "알림 타입을 입력해 주세요.") String alarmType) {

}
