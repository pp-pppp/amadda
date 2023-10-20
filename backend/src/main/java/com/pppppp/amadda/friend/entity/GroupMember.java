package com.pppppp.amadda.friend.entity;

import com.pppppp.amadda.global.entity.BaseEntity;
import com.pppppp.amadda.user.entity.User;
import jakarta.persistence.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
public class GroupMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long relationSeq;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "group_seq")
    private UserGroup group;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_seq")
    private User member;

}
