package com.pppppp.amadda.schedule.entity;

import com.pppppp.amadda.global.entity.BaseEntity;
import com.pppppp.amadda.schedule.dto.request.CategoryUpdateRequest;
import com.pppppp.amadda.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categorySeq;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_seq")
    private User user;

    @Column(nullable = false, length = 20)
    private String categoryName;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private CategoryColor categoryColor;

    @Builder
    public Category(User user, String categoryName, CategoryColor categoryColor) {
        this.user = user;
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
    }

    public void updateCategoryInfo(CategoryUpdateRequest request) {
        this.categoryName = request.categoryName();
        this.categoryColor = CategoryColor.valueOf(request.categoryColor());
    }
}
