package com.pppppp.amadda.global.entity.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FriendRequestErrorCode implements ErrorCode {
    FRIEND_REQUEST_INVALID(HttpStatus.BAD_REQUEST, "해당 친구 신청 요청은 유효하지 않습니다. "),
    FRIEND_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 친구 신청 요청이 존재하지 않습니다. "),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage() {
        return message;
    }
}
