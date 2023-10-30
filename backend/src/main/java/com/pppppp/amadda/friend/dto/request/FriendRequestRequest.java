package com.pppppp.amadda.friend.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
public record FriendRequestRequest(
        @NotNull(message = "친구 신청자의 seq값 필수") Long userSeq,
        @NotNull(message = "친구 신청 대상자의 seq값 필수") Long targetSeq) {

}
