package com.pppppp.amadda.global.entity.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GroupErrorCode implements ErrorCode {
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저그룹이 존재하지 않습니다. "),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 그룹멤버가 존재하지 않습니다. "),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage() {
        return message;
    }
}
