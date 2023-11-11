package com.pppppp.amadda.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserInitRequest(
    @NotBlank(message = "유효하지 않은 요청입니다.") String userSeq,
    @NotBlank(message = "카카오 프로필 사진을 입력해 주세요.") String imageUrl,
    @NotBlank(message = "유저의 닉네임을 입력해 주세요.") String userName,
    @NotBlank(message = "유저의 아이디를 입력해 주세요.") String userId
) {

}
