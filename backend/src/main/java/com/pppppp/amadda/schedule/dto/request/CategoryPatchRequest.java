package com.pppppp.amadda.schedule.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CategoryPatchRequest(
    @NotNull(message = "유효하지 않은 카테고리입니다.") Long categorySeq,
    String categoryName,
    String categoryColor
) {

}
