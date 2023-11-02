package com.pppppp.amadda.schedule.dto.response;

import com.pppppp.amadda.schedule.entity.Category;
import lombok.Builder;

@Builder
public record CategoryReadResponse(
    Long categorySeq,
    String categoryName,
    String categoryColor
) {

    public static CategoryReadResponse of(Category category) {
        return CategoryReadResponse.builder()
            .categorySeq(category.getCategorySeq())
            .categoryName(category.getCategoryName())
            .categoryColor(category.getCategoryColor().name())
            .build();
    }
}
