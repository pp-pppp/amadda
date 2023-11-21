package com.pppppp.amadda.global.entity.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum HttpErrorCode implements ErrorCode {
    HTTP_COOKIE_NULL(HttpStatus.UNAUTHORIZED, "쿠키가 비어있습니다. "),
    HTTP_COOKIE_KEY_NOT_FOUND(HttpStatus.NOT_FOUND, "쿠키 내에 해당 키가 존재하지 않습니다. "),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage() {
        return message;
    }
}
