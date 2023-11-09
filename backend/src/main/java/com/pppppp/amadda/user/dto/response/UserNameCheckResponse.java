package com.pppppp.amadda.user.dto.response;

import lombok.Builder;

@Builder
public record UserNameCheckResponse(
    boolean isValid
) {

    public static UserNameCheckResponse of(boolean isValid) {
        return UserNameCheckResponse.builder()
                .isValid(isValid)
                .build();
    }
}
