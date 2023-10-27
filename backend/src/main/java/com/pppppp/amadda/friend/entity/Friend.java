package com.pppppp.amadda.friend.entity;

import com.pppppp.amadda.global.entity.BaseEntity;
import com.pppppp.amadda.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;

import static jakarta.persistence.FetchType.LAZY;

@Entity
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

}
