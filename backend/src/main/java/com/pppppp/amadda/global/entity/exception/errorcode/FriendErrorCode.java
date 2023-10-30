package com.pppppp.amadda.global.entity.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FriendErrorCode implements ErrorCode {
    FRIEND_INVALID(HttpStatus.BAD_REQUEST, "해당 친구 요청은 유효하지 않습니다. "),
    FRIEND_NOT_FOUND(HttpStatus.NOT_FOUND, "친구 관계")
    ;

    private final HttpStatus httpStatus;
    private final String message;
    public String getMessage() {
        return message;
    }
}
