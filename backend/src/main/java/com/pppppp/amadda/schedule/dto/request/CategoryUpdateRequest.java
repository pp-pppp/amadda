package com.pppppp.amadda.schedule.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CategoryUpdateRequest(
    @NotBlank(message = "카테고리 이름을 입력해 주세요!") String categoryName,
    @NotBlank(message = "카테고리 색을 설정해 주세요!") String categoryColor
) {

}
