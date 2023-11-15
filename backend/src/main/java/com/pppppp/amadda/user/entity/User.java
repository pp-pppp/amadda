package com.pppppp.amadda.user.entity;

import com.pppppp.amadda.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @Column(nullable = false, length = 20)
    private String kakaoId;

    @Column(nullable = false, length = 50)
    private String userName;

    @Column(length = 50, unique = true)
    private String userId;

    @Column(nullable = false, unique = true)
    private String imageUrl;

    @Builder
    private User(String kakaoId, String userName, String userId, String imageUrl) {
        this.kakaoId = kakaoId;
        this.userName = userName;
        this.userId = userId;
        this.imageUrl = imageUrl;
    }

    public static User create(String kakaoId, String userName, String userId, String imageUrl) {
        return User.builder()
            .kakaoId(kakaoId)
            .userName(userName)
            .userId(userId)
            .imageUrl(imageUrl)
            .build();
    }

    public void delete() {
        this.kakaoId = "";
        this.userName = "";
        this.userId = "";
        this.imageUrl = "";
    }
}
