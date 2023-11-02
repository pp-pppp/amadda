package com.pppppp.amadda.schedule.dto.request;

public record CategoryCreateRequest(
    String categoryName,
    String categoryColor
) {

    public static CategoryCreateRequest of(String categoryName, String categoryColor) {
        return new CategoryCreateRequest(categoryName, categoryColor);
    }
}
