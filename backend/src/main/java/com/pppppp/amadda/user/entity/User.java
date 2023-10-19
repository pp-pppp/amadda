package com.pppppp.amadda.user.entity;

import com.pppppp.amadda.global.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User extends BaseEntity {

    @Id
    @Column
    private Long userSeq;

    @Column(nullable = false)
    private String userName;

    @Column
    private String userId;

    @Column(nullable = false)
    private String imageUrl;

}
