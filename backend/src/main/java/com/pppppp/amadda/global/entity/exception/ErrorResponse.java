package com.pppppp.amadda.global.entity.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse {
	private final String code;

	private final String message;
}
