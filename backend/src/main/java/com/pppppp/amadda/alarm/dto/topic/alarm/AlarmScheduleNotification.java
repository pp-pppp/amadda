package com.pppppp.amadda.alarm.dto.topic.alarm;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pppppp.amadda.alarm.dto.topic.BaseTopicValue;
import com.pppppp.amadda.schedule.entity.AlarmTime;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AlarmScheduleNotification extends BaseTopicValue {

    private Long scheduleSeq;
    private String scheduleName;
    private AlarmTime alarmTime;
    private LocalDateTime alarmDateTime;

    @JsonIgnore
    @Builder
    private AlarmScheduleNotification(Long scheduleSeq, String scheduleName, AlarmTime alarmTime,
        LocalDateTime alarmDateTime) {
        this.scheduleSeq = scheduleSeq;
        this.scheduleName = scheduleName;
        this.alarmTime = alarmTime;
        this.alarmDateTime = alarmDateTime;
    }

    @JsonIgnore
    public static AlarmScheduleNotification create(Long scheduleSeq, String scheduleName,
        AlarmTime alarmTime, LocalDateTime alarmDateTime) {
        return AlarmScheduleNotification.builder()
            .scheduleSeq(scheduleSeq)
            .scheduleName(scheduleName)
            .alarmTime(alarmTime)
            .alarmDateTime(alarmDateTime)
            .build();
    }
}
