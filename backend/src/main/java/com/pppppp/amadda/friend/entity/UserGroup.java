package com.pppppp.amadda.friend.entity;

import com.pppppp.amadda.global.entity.BaseEntity;
import com.pppppp.amadda.user.entity.User;
import jakarta.persistence.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
public class UserGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupSeq;

    @Column(nullable = false, length = 50)
    private String groupName;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "user_seq")
    private User owner;

}
