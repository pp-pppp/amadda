package com.pppppp.amadda.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserRefreshRequest(
    @NotBlank(message = "리프레시 토큰을 입력해 주세요.") String refreshToken) {

}
