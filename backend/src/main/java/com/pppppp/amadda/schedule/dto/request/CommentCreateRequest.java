package com.pppppp.amadda.schedule.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CommentCreateRequest(
    @NotBlank(message = "댓글 내용을 입력해주세요!") String commentContent
) {

    public static CommentCreateRequest of(String commentContent) {
        return new CommentCreateRequest(commentContent);
    }
}
