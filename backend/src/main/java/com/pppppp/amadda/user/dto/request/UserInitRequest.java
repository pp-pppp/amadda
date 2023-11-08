package com.pppppp.amadda.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserInitRequest(
        @NotBlank(message = "유저의 seq는 필수값입니다. ") String userSeq,
        @NotBlank(message = "카카오 프사의 url은 필수값입니다. ") String imageUrl,
        @NotBlank(message = "유저의 닉네입은 필수값입니다. ") String userName,
        @NotBlank(message = "유저의 아이디는 필수값입니다. ") String userId
) {

}
