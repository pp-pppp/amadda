package com.pppppp.amadda.schedule.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreateRequest(
    @NotBlank(message = "카테고리 이름을 입력해 주세요!") String categoryName,
    String categoryColor
) {

    public static CategoryCreateRequest of(String categoryName, String categoryColor) {
        return new CategoryCreateRequest(categoryName, categoryColor);
    }
}
