package com.pppppp.amadda.friend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record FriendRequestRequest(
    @NotNull(message = "친구 신청 대상자를 입력해 주세요.") Long targetSeq) {

}
