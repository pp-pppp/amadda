package com.pppppp.amadda.schedule.dto.response;

import com.pppppp.amadda.schedule.entity.Category;
import lombok.Builder;

@Builder
public record CategoryCreateResponse(
    Long categorySeq
) {

    public static CategoryCreateResponse of(Category category) {
        return CategoryCreateResponse.builder()
            .categorySeq(category.getCategorySeq())
            .build();
    }
}
