package com.pppppp.amadda.alarm.entity;


import lombok.Getter;

@Getter
public enum AlarmType {

    FRIEND_REQUEST("FRIEND_REQUEST", "친구 신청"),
    FRIEND_ACCEPT("FRIEND_ACCEPT", "친구 수락"),
    SCHEDULE_ASSIGNED("SCHEDULE_ASSIGNED", "일정 할당"),
    MENTIONED("MENTIONED", "댓글 멘션"),
    SCHEDULE_UPDATE("SCHEDULE_UPDATE", "일정 수정"),
    SCHEDULE_NOTIFICATION("SCHEDULE_NOTIFICATION", "일정 예정"),
    ;

    private final String code;
    private final String content;

    AlarmType(String code, String content) {
        this.code = code;
        this.content = content;
    }
}
