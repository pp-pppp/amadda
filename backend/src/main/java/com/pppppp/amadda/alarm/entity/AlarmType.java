package com.pppppp.amadda.alarm.entity;


import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

    private static final Map<String, AlarmType> CODE_CONTENT = Collections.unmodifiableMap(
        Stream.of(values())
            .collect(Collectors.toMap(AlarmType::getCode, Function.identity()))
    );
    
    private final String code;
    private final String content;

    AlarmType(String code, String content) {
        this.code = code;
        this.content = content;
    }

    public static AlarmType of(String code) {
        return CODE_CONTENT.get(code);
    }
}
