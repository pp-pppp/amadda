package com.pppppp.amadda.alarm.dto.response;

import com.pppppp.amadda.alarm.entity.Alarm;
import com.pppppp.amadda.alarm.entity.AlarmType;
import com.pppppp.amadda.alarm.entity.FriendRequestAlarm;
import com.pppppp.amadda.alarm.entity.ScheduleAlarm;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.CommonErrorCode;
import lombok.Builder;

@Builder
public record AlarmReadResponse(
    Long alarmSeq,
    String content,
    boolean isRead,
    AlarmType alarmType,
    Long relatedSeq,
    boolean isEnabled) {

    public static AlarmReadResponse of(Alarm alarm, boolean isEnabled) {
        AlarmReadResponseBuilder builder = AlarmReadResponse.builder()
            .alarmSeq(builder().alarmSeq)
            .content(alarm.getContent())
            .isRead(alarm.isRead())
            .alarmType(alarm.getAlarmType())
            .isEnabled(isEnabled);

        if (alarm instanceof ScheduleAlarm scheduleAlarm) {
            return builder
                .relatedSeq(scheduleAlarm.getSchedule().getScheduleSeq())
                .build();
        }
        if (alarm instanceof FriendRequestAlarm friendRequestAlarm) {
            return builder
                .relatedSeq(friendRequestAlarm.getFriendRequest().getRequestSeq())
                .build();
        }

        throw new RestApiException(CommonErrorCode.INVALID_PARAMETER);
    }
}
