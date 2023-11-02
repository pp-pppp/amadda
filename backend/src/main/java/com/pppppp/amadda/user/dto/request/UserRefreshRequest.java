package com.pppppp.amadda.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserRefreshRequest(
        @NotBlank(message = "리프레시 토큰은 필수값입니다. ") String refreshToken) {

}
