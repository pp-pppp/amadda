package com.pppppp.amadda.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserRefreshRequest(
        @NotBlank(message = "리프레시 토큰 필수") String refreshToken) {

}
