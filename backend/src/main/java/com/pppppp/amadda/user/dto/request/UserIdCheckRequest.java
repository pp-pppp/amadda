package com.pppppp.amadda.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserIdCheckRequest(
    @NotNull(message = "유저의 아이디를 입력해 주세요.") String userId
) {

}
