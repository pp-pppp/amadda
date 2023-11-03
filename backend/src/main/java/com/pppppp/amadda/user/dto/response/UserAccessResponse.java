package com.pppppp.amadda.user.dto.response;

import lombok.Builder;

@Builder
public record UserAccessResponse(
        boolean isExpired,
        boolean isBroken,
        String refreshAccessKey
) {

    public static UserAccessResponse of(boolean isExpired, boolean isBroken, String rak) {
        return UserAccessResponse.builder()
                .isExpired(isExpired)
                .isBroken(isBroken)
                .refreshAccessKey(rak)
                .build();
    }
}
