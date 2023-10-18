package com.pppppp.amadda.global.entity.exception.errorcode;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

	// 발생한 예외/에러 이름
	String name();

	// HTTP status code
	HttpStatus getHttpStatus();
}
