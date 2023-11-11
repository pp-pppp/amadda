package com.pppppp.amadda.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserJwtRequest(
    @NotBlank(message = "유저의 seq값 필수값입니다. ") String userSeq,
    @NotBlank(message = "카카오 프사의 url은 필수값입니다. ") String imageUrl) {

}
