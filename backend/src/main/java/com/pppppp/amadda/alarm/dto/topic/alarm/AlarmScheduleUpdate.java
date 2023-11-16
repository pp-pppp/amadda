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
public class AlarmScheduleUpdate extends BaseTopicValue {

    private Long scheduleSeq;
    private String scheduleName;

    @JsonIgnore
    @Builder
    private AlarmScheduleUpdate(Long scheduleSeq, String scheduleName, Long relatedSeq) {
        super(relatedSeq);
        this.scheduleSeq = scheduleSeq;
        this.scheduleName = scheduleName;
    }

    @JsonIgnore
    public static AlarmScheduleUpdate create(Long scheduleSeq, String scheduleName,
        Long relatedSeq) {
        return AlarmScheduleUpdate.builder()
            .scheduleSeq(scheduleSeq)
            .scheduleName(scheduleName)
            .relatedSeq(relatedSeq)
            .build();
    }
}
