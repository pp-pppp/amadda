package com.pppppp.amadda.schedule.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CategoryPatchRequest(
    @NotNull(message = "유효하지 않은 카테고리입니다.") Long categorySeq,
    @NotBlank(message = "카테고리 이름을 입력해주세요!") String categoryName,
    @NotBlank(message = "카테고리 색을 설정해주세요!") String categoryColor
) {

}
