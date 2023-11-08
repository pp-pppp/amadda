package com.pppppp.amadda.alarm.dto.response;

import com.pppppp.amadda.alarm.entity.Alarm;
import com.pppppp.amadda.alarm.entity.AlarmType;
import lombok.Builder;

@Builder
public record AlarmReadResponse(Long alarmSeq, String content, boolean isRead,
                                AlarmType alarmType, boolean isEnabled) {

    public static AlarmReadResponse of(Alarm alarm, boolean isEnabled) {
        return AlarmReadResponse.builder()
            .alarmSeq(builder().alarmSeq)
            .content(alarm.getContent())
            .isRead(alarm.isRead())
            .alarmType(alarm.getAlarmType())
            .isEnabled(isEnabled)
            .build();
    }
}
