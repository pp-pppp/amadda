package com.pppppp.amadda.alarm.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import com.pppppp.amadda.global.entity.BaseEntity;
import com.pppppp.amadda.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmConfig extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alarmConfigSeq;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_seq")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AlarmType alarmType;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    @ColumnDefault("1")
    private boolean isEnabled;

    @Builder
    private AlarmConfig(User user, AlarmType alarmType, boolean isEnabled) {
        this.user = user;
        this.alarmType = alarmType;
        this.isEnabled = isEnabled;
    }

    public static AlarmConfig create(User user, AlarmType alarmType, boolean isEnabled) {
        return AlarmConfig.builder()
            .user(user)
            .alarmType(alarmType)
            .isEnabled(isEnabled)
            .build();
    }

    public void updateIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

}
