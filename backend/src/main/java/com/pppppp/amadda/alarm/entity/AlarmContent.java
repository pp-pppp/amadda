package com.pppppp.amadda.alarm.entity;

import lombok.Getter;

@Getter
public enum AlarmContent {

    TEST(AlarmType.TEST, "테스트용")
    ;

    private final String type;
    private final String content;

    AlarmContent(AlarmType alarmType, String content) {
        this.type = alarmType.getCode();
        this.content = content;
    }

}
