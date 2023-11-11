package com.pppppp.amadda.friend.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.pppppp.amadda.friend.dto.response.FriendRequestResponse;
import com.pppppp.amadda.global.entity.BaseEntity;
import com.pppppp.amadda.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Builder
    private FriendRequest(User u1, User u2) {
        this.owner = u1;
        this.friend = u2;
        this.status = FriendRequestStatus.REQUESTED;
    }

    public static FriendRequest create(User u1, User u2) {
        return FriendRequest.builder()
            .u1(u1)
            .u2(u2)
            .build();
    }

    public FriendRequestResponse updateStatus(FriendRequestStatus status) {
        this.status = status;
        return FriendRequestResponse.of(this);
    }

}
