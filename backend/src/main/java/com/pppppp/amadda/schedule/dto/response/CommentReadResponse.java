package com.pppppp.amadda.schedule.dto.response;

import com.pppppp.amadda.schedule.entity.Comment;
import com.pppppp.amadda.user.dto.response.UserReadResponse;
import lombok.Builder;

@Builder
public record CommentReadResponse(
    Long commentSeq,
    UserReadResponse user,
    String commentContent,
    String createdAt
) {

    public static CommentReadResponse of(Comment comment, UserReadResponse user) {
        return CommentReadResponse.builder()
            .commentSeq(comment.getCommentSeq())
            .user(user)
            .commentContent(comment.getCommentContent())
            .createdAt(String.valueOf(comment.getCreatedAt()))
            .build();
    }
}
