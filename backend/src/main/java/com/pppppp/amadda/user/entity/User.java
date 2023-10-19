package com.pppppp.amadda.user.entity;

import com.pppppp.amadda.global.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User extends BaseEntity {

    @Id
    @Column
    private Long userSeq;

    @Column(nullable = false, length = 50, updatable = false)
    private String userName;

    @Column(length = 50, unique = true)
    private String userId;

    @Column(nullable = false, length = 100, unique = true)
    private String imageUrl;

    @Column(nullable = false)
    private boolean isInited;

}
