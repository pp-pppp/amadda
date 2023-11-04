package com.pppppp.amadda.global.entity.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ParticipationErrorCode implements ErrorCode {
    PARTICIPATION_INVALID(HttpStatus.BAD_REQUEST, "할당되지 않은 일정입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
