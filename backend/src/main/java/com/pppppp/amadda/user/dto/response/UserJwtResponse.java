package com.pppppp.amadda.user.dto.response;

import lombok.Builder;

@Builder
public record UserJwtResponse(
    String accessToken,
    String refreshToken,
    String refreshAccessKey) {

    public static UserJwtResponse of(String at, String rf, String rak) {
        return UserJwtResponse.builder()
            .accessToken(at)
            .refreshToken(rf)
            .refreshAccessKey(rak)
            .build();
    }
}
