package com.pppppp.amadda.global.entity.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ScheduleErrorCode implements ErrorCode {
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 일정이 존재하지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
