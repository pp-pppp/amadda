package com.pppppp.amadda.user.entity;

import org.hibernate.annotations.ColumnDefault;

import com.pppppp.amadda.global.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User extends BaseEntity {

    @Id
    private Long userSeq;

    @Column(nullable = false, length = 50, updatable = false)
    private String userName;

    @Column(length = 50, unique = true)
    private String userId;

    @Column(nullable = false, unique = true)
    private String imageUrl;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    @ColumnDefault(value = "0")
    private boolean isInited;

}
