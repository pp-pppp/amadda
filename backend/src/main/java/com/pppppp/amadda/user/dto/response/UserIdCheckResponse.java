package com.pppppp.amadda.user.dto.response;

import lombok.Builder;

@Builder
public record UserIdCheckResponse(
    boolean isDuplicated,
    boolean isValid
) {

    public static UserIdCheckResponse of(boolean isDup, boolean isValid) {
        return UserIdCheckResponse.builder()
            .isDuplicated(isDup)
            .isValid(isValid)
            .build();
    }
}
