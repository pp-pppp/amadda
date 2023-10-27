package com.pppppp.amadda.user.dto.response;

import com.pppppp.amadda.user.entity.User;
import lombok.Builder;

@Builder
public record UserReadResponse(
        Long userSeq,
        String userName,
        String userId,
        String imageUrl,
        Boolean isFriend) {

    public static UserReadResponse of(User user, Boolean isFriend) {
        return UserReadResponse.builder()
                .userSeq(user.getUserSeq())
                .userName(user.getUserName())
                .userId(user.getUserId())
                .imageUrl(user.getImageUrl())
                .isFriend(isFriend)
                .build();
    }
}
