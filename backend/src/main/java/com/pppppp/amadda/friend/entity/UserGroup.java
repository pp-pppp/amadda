package com.pppppp.amadda.friend.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.pppppp.amadda.global.entity.BaseEntity;
import com.pppppp.amadda.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupSeq;

    @Column(nullable = false, length = 50)
    private String groupName;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "user_seq")
    private User owner;

    @Builder
    private UserGroup(String groupName, User owner) {
        this.groupName = groupName;
        this.owner = owner;
    }

    public static UserGroup create(String name, User u) {
        return UserGroup.builder()
            .groupName(name)
            .owner(u)
            .build();
    }

    public void updateGroupName(String name) {
        this.groupName = name;
    }
}
