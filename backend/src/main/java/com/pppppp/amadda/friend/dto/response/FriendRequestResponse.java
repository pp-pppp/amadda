package com.pppppp.amadda.friend.dto.response;

import com.pppppp.amadda.friend.entity.FriendRequest;
import lombok.Builder;

@Builder
public record FriendRequestResponse(
        Long requestSeq,
        Long ownerSeq,
        Long friendSeq,
        String status) {

    public static FriendRequestResponse of(FriendRequest friendRequest) {
        return FriendRequestResponse.builder()
                .requestSeq(friendRequest.getRequestSeq())
                .ownerSeq(friendRequest.getOwner().getUserSeq())
                .friendSeq(friendRequest.getFriend().getUserSeq())
                .status(friendRequest.getStatus().toString())
                .build();
    }

}
