package com.pppppp.amadda.user.entity;

import com.pppppp.amadda.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;

    @Column(nullable = false, length = 50, updatable = false)
    private String userName;

    @Column(length = 50, unique = true)
    private String userId;

    @Column(nullable = false, unique = true)
    private String imageUrl;

    @Builder
    private User(Long userSeq, String userName, String userId, String imageUrl) {
        this.userName = userName;
        this.userId = userId;
        this.imageUrl = imageUrl;
    }

    public static User create(Long userSeq, String userName, String userId, String imageUrl) {
        return User.builder()
            .userSeq(userSeq)
            .userName(userName)
            .userId(userId)
            .imageUrl(imageUrl)
            .build();
    }

    public void delete() {
        this.userName = "";
        this.userId = "";
        this.imageUrl = "";
    }
}
