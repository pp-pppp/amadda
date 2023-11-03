package com.pppppp.amadda.global.entity.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CategoryErrorCode implements ErrorCode {
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리 이름입니다."),
    CATEGORY_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 있는 카테고리 이름입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
