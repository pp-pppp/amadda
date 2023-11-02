package com.pppppp.amadda.user.dto.response;

import lombok.Builder;

@Builder
public record UserJwtResponse(
        String accessToken,
        String refreshToken,
        String refreshAccessKey,
        boolean isInited) {

    public static UserJwtResponse of(String at, String rf, String rak, boolean isInited) {
        return UserJwtResponse.builder()
                .accessToken(at)
                .refreshToken(rf)
                .refreshAccessKey(rak)
                .isInited(isInited)
                .build();
    }
}
