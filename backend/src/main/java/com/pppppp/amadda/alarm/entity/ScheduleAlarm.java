package com.pppppp.amadda.alarm.entity;

import com.pppppp.amadda.schedule.entity.Schedule;
import com.pppppp.amadda.user.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("SCHEDULE")
public class ScheduleAlarm extends Alarm {

    @ManyToOne
    @JoinColumn(name = "schedule_seq")
    private Schedule schedule;

    @Builder
    private ScheduleAlarm(User user, String content, boolean isRead, AlarmType alarmType,
        Schedule schedule) {
        super(user, content, isRead, alarmType);
        this.schedule = schedule;
    }

    public static ScheduleAlarm create(User user, String content, AlarmType alarmType,
        Schedule schedule) {
        return ScheduleAlarm.builder()
            .user(user)
            .content(content)
            .isRead(false)
            .alarmType(alarmType)
            .schedule(schedule)
            .build();
    }
}
