package com.pppppp.amadda.friend.dto.response;

import com.pppppp.amadda.friend.entity.Friend;
import lombok.Builder;

@Builder
public record FriendResponse(
    Long relationSeq,
    Long ownerSeq,
    Long friendSeq) {

    public static FriendResponse of(Friend friend) {
        return FriendResponse.builder()
            .relationSeq(friend.getRelationSeq())
            .ownerSeq(friend.getOwner().getUserSeq())
            .friendSeq(friend.getFriend().getUserSeq())
            .build();
    }
}
