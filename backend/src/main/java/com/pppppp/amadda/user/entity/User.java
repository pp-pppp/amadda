package com.pppppp.amadda.user.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import com.pppppp.amadda.global.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Getter
@Table(name="users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    private Long userSeq;

    @Column(nullable = false, length = 50, updatable = false)
    private String userName;

    @Column(length = 50, unique = true)
    private String userId;

    @Column(nullable = false, unique = true)
    private String imageUrl;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    @ColumnDefault(value = "0")
    private boolean isInited;

    @Builder
    private User(Long userSeq, String userName, String userId, String imageUrl, boolean isInited) {
        this.userSeq = userSeq;
        this.userName = userName;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.isInited = isInited;
    }

    public static User create(Long userSeq, String userName, String userId, String imageUrl, boolean isInited) {
        return User.builder()
                .userSeq(userSeq)
                .userName(userName)
                .userId(userId)
                .imageUrl(imageUrl)
                .isInited(isInited)
                .build();
    }
}
