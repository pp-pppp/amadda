package com.pppppp.amadda.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserJwtRequest(
        @NotNull(message = "유저 seq값 필수") Long userSeq,
        @NotBlank(message = "카카오 프사 url 필수") String imageUrl) {

}
