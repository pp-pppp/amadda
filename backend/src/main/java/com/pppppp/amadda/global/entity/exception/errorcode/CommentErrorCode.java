package com.pppppp.amadda.global.entity.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements ErrorCode {
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글이 존재하지 않습니다."),
    COMMENT_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "이미 삭제된 댓글입니다."),
    COMMENT_ALREADY_RESTORED(HttpStatus.BAD_REQUEST, "이미 복구된 댓글입니다."),
    COMMENT_FORBIDDEN(HttpStatus.FORBIDDEN, "댓글의 작성자가 아닙니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
