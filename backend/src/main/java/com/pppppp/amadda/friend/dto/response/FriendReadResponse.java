package com.pppppp.amadda.friend.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record FriendReadResponse(
        List<GroupResponse> groups,
        List<MemberResponse> members
){

    public static FriendReadResponse of(
            List<GroupResponse> groupResponses,
            List<MemberResponse> memberResponses) {
        return FriendReadResponse.builder()
                .groups(groupResponses)
                .members(memberResponses)
                .build();
    }
}
