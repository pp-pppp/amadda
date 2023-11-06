package com.pppppp.amadda.friend.dto.response;

import com.pppppp.amadda.friend.entity.GroupMember;
import com.pppppp.amadda.friend.entity.UserGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record GroupPatchResponse(
    Long ownerSeq,
    String groupName,
    List<Long> userSeqs){
    
    public static GroupPatchResponse of(UserGroup group, List<Long> members) {
        return GroupPatchResponse.builder()
                .ownerSeq(group.getOwner().getUserSeq())
                .groupName(group.getGroupName())
                .userSeqs(members)
                .build();
    }

}
