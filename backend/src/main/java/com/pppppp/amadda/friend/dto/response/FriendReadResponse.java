package com.pppppp.amadda.friend.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record FriendReadResponse(
    List<GroupResponse> groups,
    List<MemberResponse> members
) {

    public static FriendReadResponse of(
        List<GroupResponse> groupResponses,
        List<MemberResponse> memberResponses) {
        return FriendReadResponse.builder()
            .groups(groupResponses)
            .members(memberResponses)
            .build();
    }
}
