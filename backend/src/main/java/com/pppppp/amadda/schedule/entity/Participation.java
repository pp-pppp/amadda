package com.pppppp.amadda.schedule.entity;

import com.pppppp.amadda.global.entity.BaseEntity;
import com.pppppp.amadda.schedule.dto.request.ScheduleCreateRequest;
import com.pppppp.amadda.schedule.dto.request.SchedulePatchRequest;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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

    @Builder
    public Participation(User user, Schedule schedule, Category category, String scheduleName,
        String scheduleMemo, AlarmTime alarmTime) {
        this.user = user;
        this.schedule = schedule;
        this.category = category;
        this.scheduleName = scheduleName;
        this.scheduleMemo = scheduleMemo;
        this.alarmTime = alarmTime;
    }

    public static Participation create(ScheduleCreateRequest request, User participant,
        Schedule schedule, Category category) {
        return Participation.builder()
            .scheduleName(request.scheduleName())
            .scheduleMemo(request.scheduleMemo())
            .alarmTime(request.alarmTime())
            .user(participant)
            .schedule(schedule)
            .category(category)
            .build();
    }

    public void updateParticipationInfo(SchedulePatchRequest request) {
        if (StringUtils.hasText(request.scheduleName())) {
            this.scheduleName = request.scheduleName();
        }
        if (StringUtils.hasText(request.scheduleMemo())) {
            this.scheduleMemo = request.scheduleMemo();
        }
        if (!ObjectUtils.isEmpty(request.alarmTime())) {
            this.alarmTime = request.alarmTime();
        }
    }

    public void updateCategory(Category category) {
        this.category = category;
    }
}
