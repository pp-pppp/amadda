package com.pppppp.amadda.global.entity.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ImageErrorCode implements ErrorCode {
    IMAGE_DOWNLOAD_FAILED(HttpStatus.NOT_FOUND, "카카오 이미지의 다운로드 과정에서 문제가 발생했습니다. "),
    IMAGE_UPLOAD_FAILED(HttpStatus.NOT_FOUND, "이미지의 업로드 과정에서 문제가 발생했습니다. "),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage() {
        return message;
    }
}
