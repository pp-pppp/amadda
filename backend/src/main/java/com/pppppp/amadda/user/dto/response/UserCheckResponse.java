package com.pppppp.amadda.user.dto.response;

import com.pppppp.amadda.user.entity.User;
import lombok.Builder;

@Builder
public record UserCheckResponse(
    boolean isDuplicated
) {

    public static UserCheckResponse of(boolean isDup) {
        return UserCheckResponse.builder()
                .isDuplicated(isDup)
                .build();
    }
}
