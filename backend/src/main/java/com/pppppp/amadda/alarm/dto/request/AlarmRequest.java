package com.pppppp.amadda.alarm.dto.request;

import com.pppppp.amadda.alarm.entity.AlarmType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AlarmRequest(
    @NotNull(message = "유저값은 필수값입니다.") Long userSeq,
    @NotNull(message = "알림 타입은 필수값입니다.") AlarmType alarmType) {

}
