package com.pppppp.amadda.friend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record FriendRequestRequest(
    @NotNull(message = "유효하지 않은 요청입니다.") Long userSeq,
    @NotNull(message = "친구 신청 대상자를 입력해 주세요.") Long targetSeq) {

}
