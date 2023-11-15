package com.pppppp.amadda.user.dto.response;

import com.pppppp.amadda.user.entity.User;
import lombok.Builder;

@Builder
public record UserRelationResponse(
    Long userSeq,
    String userName,
    String userId,
    String imageUrl,
    boolean isFriend
) {

    public static UserRelationResponse of(User user, boolean isFriend) {
        return UserRelationResponse.builder()
            .userSeq(user.getUserSeq())
            .userName(user.getUserName())
            .userId(user.getUserId())
            .imageUrl(user.getImageUrl())
            .isFriend(isFriend)
            .build();
    }

    public static UserRelationResponse notFound() {
        return UserRelationResponse.builder()
            .userSeq(null)
            .userName("")
            .userId("")
            .imageUrl("")
            .isFriend(false)
            .build();
    }
}
