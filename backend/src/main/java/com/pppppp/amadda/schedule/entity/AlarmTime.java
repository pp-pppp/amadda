package com.pppppp.amadda.schedule.entity;

import lombok.Getter;

@Getter
public enum AlarmTime {
    ONE_DAY_BEFORE("하루 전", 1440),
    ONE_HOUR_BEFORE("1시간 전", 60),
    THIRTY_MINUTES_BEFORE("30분 전", 30),
    FIFTEEN_MINUTES_BEFORE("15분 전", 15),
    ON_TIME("약속 시간", 0),
    NONE("알림 없음", -1);

    private final String content;
    private final int minute;

    AlarmTime(String content, int minute) {
        this.content = content;
        this.minute = minute;
    }
}
