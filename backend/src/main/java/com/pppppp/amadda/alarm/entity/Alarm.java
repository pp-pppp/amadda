package com.pppppp.amadda.alarm.entity;

import com.pppppp.amadda.global.entity.BaseEntity;
import com.pppppp.amadda.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Alarm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alarmSeq;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_seq")
    private User user;

    @Column(nullable = false, length = 255)
    private String content;

    @Column(nullable = false, columnDefinition="TINYINT(1) DEFAULT 0")
    private boolean isRead;

    @Column(nullable = false, columnDefinition="TINYINT(1) DEFAULT 0")
    private boolean isSend;

}
