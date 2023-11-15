package com.pppppp.amadda.global.entity.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다. "),
    USER_ID_DUPLICATED(HttpStatus.BAD_REQUEST, "해당 유저 id는 중복되는 값 입니다. "),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage() {
        return message;
    }
}
