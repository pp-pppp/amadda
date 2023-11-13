package com.pppppp.amadda.schedule.entity;

import com.pppppp.amadda.global.entity.BaseEntity;
import com.pppppp.amadda.schedule.dto.request.ParticipationUpdateRequest;
import com.pppppp.amadda.schedule.dto.request.ScheduleCreateRequest;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Participation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participationSeq;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_seq")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schedule_seq")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_seq")
    private Category category;

    @Column(nullable = false, length = 50)
    private String scheduleName;

    @Column
    private String scheduleMemo;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AlarmTime alarmTime;

    @Column
    private LocalDateTime alarmAt;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    @ColumnDefault("1")
    private boolean isMentionAlarmOn;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    @ColumnDefault("1")
    private boolean isUpdateAlarmOn;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    @ColumnDefault("0")
    private boolean isAlarmed;

    @Builder
    public Participation(User user, Schedule schedule, Category category, String scheduleName,
        String scheduleMemo, AlarmTime alarmTime, LocalDateTime alarmAt, boolean isMentionAlarmOn,
        boolean isUpdateAlarmOn, boolean isAlarmed) {
        this.user = user;
        this.schedule = schedule;
        this.category = category;
        this.scheduleName = scheduleName;
        this.scheduleMemo = scheduleMemo;
        this.alarmTime = alarmTime;
        this.alarmAt = alarmAt;
        this.isMentionAlarmOn = isMentionAlarmOn;
        this.isUpdateAlarmOn = isUpdateAlarmOn;
        this.isAlarmed = isAlarmed;
    }

    public static Participation create(ScheduleCreateRequest request, User participant,
        Schedule schedule, Category category, boolean isMentionAlarmOn, boolean isUpdateAlarmOn) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime alarmAt =
            (request.isTimeSelected()) ? LocalDateTime.parse(request.scheduleStartAt(), formatter)
                .minusMinutes(request.alarmTime().getMinute()) : null;

        return Participation.builder()
            .scheduleName(request.scheduleName())
            .scheduleMemo(request.scheduleMemo())
            .alarmTime(request.alarmTime())
            .alarmAt(alarmAt)
            .user(participant)
            .schedule(schedule)
            .category(category)
            .isMentionAlarmOn(isMentionAlarmOn)
            .isUpdateAlarmOn(isUpdateAlarmOn)
            .isAlarmed(false)
            .build();
    }

    public void updateParticipationInfo(ParticipationUpdateRequest request) {
        this.scheduleName = request.scheduleName();
        this.scheduleMemo = request.scheduleMemo();
        this.alarmTime = request.alarmTime();
    }

    public void updateAlarmAt(LocalDateTime startAt) {
        if (startAt != null) {
            this.alarmAt = startAt.minusMinutes(this.alarmTime.getMinute());
        }
    }

    public void updateCategory(Category category) {
        this.category = category;
    }

    public void updateIsMentionAlarmOn(boolean isMentionAlarmOn) {
        this.isMentionAlarmOn = isMentionAlarmOn;
    }

    public void updateIsUpdateAlarmOn(boolean isUpdateAlarmOn) {
        this.isUpdateAlarmOn = isUpdateAlarmOn;
    }

    public void updateIsAlarmed(boolean isAlarmed) {
        this.isAlarmed = isAlarmed;
    }
}
