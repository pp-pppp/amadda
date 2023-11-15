package com.pppppp.amadda.user.dto.response;

import lombok.Builder;

@Builder
public record UserJwtInitResponse(
    String accessToken,
    String refreshToken,
    String refreshAccessKey,
    boolean isInited) {

    public static UserJwtInitResponse of(String at, String rf, String rak, boolean isInited) {
        return UserJwtInitResponse.builder()
            .accessToken(at)
            .refreshToken(rf)
            .refreshAccessKey(rak)
            .isInited(isInited)
            .build();
    }
}
