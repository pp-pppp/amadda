package com.pppppp.amadda.alarm.entity;

import lombok.Getter;

@Getter
public enum AlarmContent {

    FRIEND_REQUEST(AlarmType.FRIEND_REQUEST, "%s 님이 친구신청을 했어요."),
    FRIEND_ACCEPT(AlarmType.FRIEND_ACCEPT, "%s 님이 친구신청을 수락했어요."),
    SCHEDULE_ASSIGNED(AlarmType.SCHEDULE_ASSIGNED, "%s 님이 '%s' 일정을 추가했어요."),
    MENTIONED(AlarmType.MENTIONED, "%s 님이 '%s' 일정에서 당신을 멘션했어요."),
    SCHEDULE_UPDATE(AlarmType.SCHEDULE_UPDATE, "'%s' 일정이 수정되었어요."),
    SCHEDULE_NOTI(AlarmType.SCHEDULE_NOTI, "'%s' 일정이 %s 예정되어 있어요."),
    ;

    private final String type;
    private final String message;

    AlarmContent(AlarmType alarmType, String message) {
        this.type = alarmType.getCode();
        this.message = message;
    }

    public String getMessage(String... parameter) {
        return String.format(message, parameter);
    }

}
