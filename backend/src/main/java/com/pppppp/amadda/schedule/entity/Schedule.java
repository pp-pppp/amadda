package com.pppppp.amadda.schedule.entity;

import com.pppppp.amadda.global.entity.BaseEntity;
import com.pppppp.amadda.schedule.dto.request.SchedulePatchRequest;
import com.pppppp.amadda.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleSeq;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "authorized_user_seq")
    private User authorizedUser;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    @ColumnDefault("0")
    private boolean isTimeSelected;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    @ColumnDefault("0")
    private boolean isDateSelected;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    @ColumnDefault("0")
    private boolean isAllDay;

    @Column
    private LocalDateTime scheduleStartAt;

    @Column
    private LocalDateTime scheduleEndAt;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    @ColumnDefault("0")
    private boolean isFinished;

    @Column
    private String scheduleContent;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    @ColumnDefault("0")
    private boolean isAuthorizedAll;

    @Builder
    public Schedule(User authorizedUser, boolean isTimeSelected, boolean isDateSelected,
        boolean isAllDay,
        LocalDateTime scheduleStartAt, LocalDateTime scheduleEndAt, boolean isFinished,
        String scheduleContent, boolean isAuthorizedAll) {
        this.authorizedUser = authorizedUser;
        this.isTimeSelected = isTimeSelected;
        this.isDateSelected = isDateSelected;
        this.isAllDay = isAllDay;
        this.scheduleStartAt = scheduleStartAt;
        this.scheduleEndAt = scheduleEndAt;
        this.isFinished = isFinished;
        this.scheduleContent = scheduleContent;
        this.isAuthorizedAll = isAuthorizedAll;
    }

    public static Schedule create(User user,
        String scheduleContent) {
        return Schedule.builder()
            .authorizedUser(user)
            .isTimeSelected(false)
            .isDateSelected(false)
            .isAllDay(false)
            .isFinished(false)
            .scheduleContent(scheduleContent)
            .isAuthorizedAll(true)
            .build();
    }

    public void updateScheduleInfo(SchedulePatchRequest request) {
        this.scheduleContent = request.scheduleContent();
        this.isDateSelected = request.isDateSelected();
        this.isTimeSelected = request.isTimeSelected();
        this.isAllDay = request.isAllDay();
        if (isDateSelected) {
            this.scheduleStartAt = LocalDateTime.parse(request.scheduleStartAt());
        }
        if (isTimeSelected) {
            this.scheduleEndAt = LocalDateTime.parse(request.scheduleEndAt());
        }
    }
}
