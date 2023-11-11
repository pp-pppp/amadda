package com.pppppp.amadda.friend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record FriendRequestRequest(
    @NotNull(message = "친구 신청자의 seq값 필수") Long userSeq,
    @NotNull(message = "친구 신청 대상자의 seq값 필수") Long targetSeq) {

}
