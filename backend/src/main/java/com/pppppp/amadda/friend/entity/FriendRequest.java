package com.pppppp.amadda.friend.entity;

import com.pppppp.amadda.global.entity.BaseEntity;
import com.pppppp.amadda.user.entity.User;
import jakarta.persistence.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
public class FriendRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestSeq;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "owner_seq")
    private User owner;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "friend_seq")
    private User friend;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendRequestStatus status; // REQUESTED, ACCEPTED, DECLINED

}
