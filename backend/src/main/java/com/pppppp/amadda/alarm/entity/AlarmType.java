package com.pppppp.amadda.alarm.entity;


import lombok.Getter;

@Getter
public enum AlarmType {

    TEST("TEST", "테스트용")
    ;

    private final String code;
    private final String content;

    AlarmType(String code, String content) {
        this.code = code;
        this.content = content;
    }

}
