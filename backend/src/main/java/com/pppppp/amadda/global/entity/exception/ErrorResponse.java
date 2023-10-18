package com.pppppp.amadda.global.entity.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse {
	// 에러 코드
	private final String code;

	// 에러와 함께 전달될 메시지
	private final String message;
}
