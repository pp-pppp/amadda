package com.pppppp.amadda.friend.entity;

import com.pppppp.amadda.global.entity.BaseEntity;
import com.pppppp.amadda.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long relationSeq;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "owner_seq")
    private User owner;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "friend_seq")
    private User friend;

    @Builder
    private Friend(User u1, User u2) {
        this.owner = u1;
        this.friend = u2;
    }

    public static Friend create(User u1, User u2) {
        return Friend.builder()
                .u1(u1)
                .u2(u2)
                .build();
    }

}
