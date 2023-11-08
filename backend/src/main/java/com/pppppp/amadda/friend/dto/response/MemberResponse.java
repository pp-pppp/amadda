package com.pppppp.amadda.friend.dto.response;

import com.pppppp.amadda.friend.entity.Friend;
import com.pppppp.amadda.friend.entity.GroupMember;
import com.pppppp.amadda.user.entity.User;
import lombok.Builder;

@Builder
public record MemberResponse(
        Long userSeq,
        String userName,
        String userId,
        String imageUrl
){
    public static MemberResponse of(GroupMember m) {
        return MemberResponse.builder()
                .userSeq(m.getMember().getUserSeq())
                .userName(m.getMember().getUserName())
                .userId(m.getMember().getUserId())
                .imageUrl(m.getMember().getImageUrl())
                .build();
    }

    public static MemberResponse of(Friend f) {
        return MemberResponse.builder()
                .userSeq(f.getFriend().getUserSeq())
                .userName(f.getFriend().getUserName())
                .userId(f.getFriend().getUserId())
                .imageUrl(f.getFriend().getImageUrl())
                .build();
    }
}
