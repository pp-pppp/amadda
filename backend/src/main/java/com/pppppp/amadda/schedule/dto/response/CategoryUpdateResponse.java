package com.pppppp.amadda.schedule.dto.response;

import com.pppppp.amadda.schedule.entity.Category;
import lombok.Builder;

@Builder
public record CategoryUpdateResponse(
    Long categorySeq
) {

    public static CategoryUpdateResponse of(Category category) {
        return CategoryUpdateResponse.builder()
            .categorySeq(category.getCategorySeq())
            .build();
    }
}
