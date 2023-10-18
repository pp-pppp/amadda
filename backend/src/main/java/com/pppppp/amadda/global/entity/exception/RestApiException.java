package com.pppppp.amadda.global.entity.exception;

import com.pppppp.amadda.global.entity.exception.errorcode.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException {
	// 전달할 에러 코드
	private final ErrorCode errorCode;
}
