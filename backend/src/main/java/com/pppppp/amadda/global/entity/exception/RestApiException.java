package com.pppppp.amadda.global.entity.exception;

import com.pppppp.amadda.global.entity.exception.errorcode.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class RestApiException extends RuntimeException {
	private final ErrorCode errorCode;

	public RestApiException(ErrorCode errorCode) {
		super(errorCode.name());
		this.errorCode = errorCode;
	}
}
