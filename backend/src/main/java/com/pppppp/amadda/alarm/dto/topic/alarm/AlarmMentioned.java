package com.pppppp.amadda.alarm.dto.topic.alarm;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pppppp.amadda.alarm.dto.topic.BaseTopicValue;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AlarmMentioned extends BaseTopicValue {

    private Long scheduleSeq;
    private String scheduleName;
    private Long writerUserSeq;
    private String writerUserName;

    @JsonIgnore
    @Builder
    private AlarmMentioned(Long scheduleSeq, String scheduleName, Long writerUserSeq,
        String writerUserName) {
        this.scheduleSeq = scheduleSeq;
        this.scheduleName = scheduleName;
        this.writerUserSeq = writerUserSeq;
        this.writerUserName = writerUserName;
    }

    @JsonIgnore
    public static AlarmMentioned create(Long scheduleSeq, String scheduleName, Long writerUserSeq,
        String writerUserName) {
        return AlarmMentioned.builder()
            .scheduleSeq(scheduleSeq)
            .scheduleName(scheduleName)
            .writerUserSeq(writerUserSeq)
            .writerUserName(writerUserName)
            .build();
    }
}
