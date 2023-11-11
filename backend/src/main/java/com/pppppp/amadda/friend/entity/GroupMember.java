package com.pppppp.amadda.friend.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.pppppp.amadda.global.entity.BaseEntity;
import com.pppppp.amadda.user.entity.User;
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
public class GroupMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long relationSeq;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "group_seq")
    private UserGroup group;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "member_seq")
    private User member;

    @Builder
    private GroupMember(UserGroup group, User member) {
        this.group = group;
        this.member = member;
    }

    public static GroupMember create(UserGroup group, User member) {
        return GroupMember.builder()
            .group(group)
            .member(member)
            .build();
    }

}
