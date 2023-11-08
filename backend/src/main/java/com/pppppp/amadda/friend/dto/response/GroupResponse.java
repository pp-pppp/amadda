package com.pppppp.amadda.friend.dto.response;

import com.pppppp.amadda.friend.entity.GroupMember;
import com.pppppp.amadda.friend.entity.UserGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record GroupResponse(
        Long groupSeq,
        String groupName,
        List<MemberResponse> members
){

    public static GroupResponse of(
            UserGroup group,
            List<MemberResponse> memberResponses
    ){
        return GroupResponse.builder()
                .groupSeq(group.getGroupSeq())
                .groupName(group.getGroupName())
                .members(memberResponses)
                .build();
    }
}
