package com.pppppp.amadda.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserNameCheckRequest(
    @NotBlank(message = "유저의 이름을 입력해 주세요.") String userName
) {

}
