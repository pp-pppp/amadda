package com.pppppp.amadda.schedule.dto.request;

import com.pppppp.amadda.schedule.entity.Comment;
import com.pppppp.amadda.schedule.entity.Schedule;
import com.pppppp.amadda.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CommentCreateRequest(
    @NotBlank(message = "댓글 내용을 입력해주세요!") String commentContent
) {

    public static CommentCreateRequest of(String commentContent) {
        return CommentCreateRequest.builder()
            .commentContent(commentContent)
            .build();
    }

    public Comment toEntity(User user, Schedule schedule) {
        return Comment.builder()
            .user(user)
            .schedule(schedule)
            .commentContent(commentContent)
            .build();
    }
}
