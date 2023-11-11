package com.pppppp.amadda.friend.dto.response;

import com.pppppp.amadda.friend.entity.UserGroup;
import java.util.List;
import lombok.Builder;

@Builder
public record GroupResponse(
    Long groupSeq,
    String groupName,
    List<MemberResponse> members
) {

    public static GroupResponse of(
        UserGroup group,
        List<MemberResponse> memberResponses
    ) {
        return GroupResponse.builder()
            .groupSeq(group.getGroupSeq())
            .groupName(group.getGroupName())
            .members(memberResponses)
            .build();
    }
}
