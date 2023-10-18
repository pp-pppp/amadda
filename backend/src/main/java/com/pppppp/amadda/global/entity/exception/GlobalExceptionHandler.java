package com.pppppp.amadda.global.entity.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pppppp.amadda.global.entity.exception.errorcode.ErrorCode;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	@ExceptionHandler(RestApiException.class)
	public ResponseEntity<Object> handleCustomException(RestApiException e){
		ErrorCode errorCode = e.getErrorCode();
		return handleExceptionInternal(errorCode, e.getMessage());
	}

	private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String message) {
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(doErrorResponse(errorCode, message));
	}

	private ErrorResponse doErrorResponse(ErrorCode errorCode, String message) {
		return ErrorResponse.builder()
			.code(errorCode.name())
			.message(message)
			.build();
	}
}
