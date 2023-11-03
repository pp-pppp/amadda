package com.pppppp.amadda.schedule.dto.request;

import com.pppppp.amadda.schedule.entity.Category;
import com.pppppp.amadda.schedule.entity.CategoryColor;
import com.pppppp.amadda.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CategoryCreateRequest(
    @NotBlank(message = "카테고리 이름을 입력해 주세요!") String categoryName,
    String categoryColor
) {

    public static CategoryCreateRequest of(String categoryName, String categoryColor) {
        return CategoryCreateRequest.builder()
            .categoryName(categoryName)
            .categoryColor(categoryColor)
            .build();
    }

    public Category toEntity(User user) {
        return Category.builder()
            .user(user)
            .categoryName(categoryName)
            .categoryColor(CategoryColor.valueOf(categoryColor))
            .build();
    }
}
