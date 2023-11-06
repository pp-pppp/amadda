package com.pppppp.amadda.alarm.dto.response;

import com.pppppp.amadda.alarm.entity.Alarm;
import com.pppppp.amadda.alarm.entity.AlarmType;
import lombok.Builder;

@Builder
public record AlarmReadResponse(Long alarmSeq, String content, boolean isRead, AlarmType alarmType) {

    public static AlarmReadResponse of(Alarm alarm) {
        return AlarmReadResponse.builder()
            .alarmSeq(builder().alarmSeq)
            .content(alarm.getContent())
            .isRead(alarm.isRead())
            .alarmType(alarm.getAlarmType())
            .build();
    }
}
