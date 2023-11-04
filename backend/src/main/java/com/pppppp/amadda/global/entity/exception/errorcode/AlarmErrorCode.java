package com.pppppp.amadda.global.entity.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AlarmErrorCode implements ErrorCode {
    CANNOT_SET_GLOBAL_CONFIG(HttpStatus.BAD_REQUEST, "일정 예정 알람은 켜고 끌 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
