package com.pppppp.amadda.global.entity.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ScheduleErrorCode implements ErrorCode {
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "일정이 존재하지 않습니다."),
    SCHEDULE_DATE_NOT_SELECTED(HttpStatus.BAD_REQUEST, "언제 참석하는 일정인지 알려주세요!"),
    SCHEDULE_TIME_NOT_SELECTED(HttpStatus.BAD_REQUEST, "일정이 몇 시에 시작하는지 알려주세요!"),
    SCHEDULE_FORBIDDEN(HttpStatus.FORBIDDEN, "일정에 대한 권한이 없습니다."),
    SCHEDULE_ALREADY_PARTICIPATED(HttpStatus.BAD_REQUEST, "이미 일정에 참가 중입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
