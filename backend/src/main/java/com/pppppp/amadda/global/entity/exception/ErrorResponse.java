package com.pppppp.amadda.global.entity.exception;

import lombok.Builder;

@Builder
public record ErrorResponse(String code, String message) {

}
