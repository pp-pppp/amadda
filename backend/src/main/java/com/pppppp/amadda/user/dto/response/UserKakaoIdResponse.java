package com.pppppp.amadda.user.dto.response;

import lombok.Builder;

@Builder
public record UserKakaoIdResponse(
    String kakaoId
) {

    public static UserKakaoIdResponse of(String kakaoId) {
        return UserKakaoIdResponse.builder()
            .kakaoId(kakaoId)
            .build();
    }
}
