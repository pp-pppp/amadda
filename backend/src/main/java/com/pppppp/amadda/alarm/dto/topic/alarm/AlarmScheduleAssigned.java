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
public class AlarmScheduleAssigned extends BaseTopicValue {

    private Long scheduleSeq;
    private String scheduleName;
    private Long scheduleOwnerUserSeq;
    private String scheduleOwnerUserName;

    @JsonIgnore
    @Builder
    private AlarmScheduleAssigned(Long scheduleSeq, String scheduleName, Long scheduleOwnerUserSeq,
        String scheduleOwnerUserName, Long relatedSeq) {
        super(relatedSeq);
        this.scheduleSeq = scheduleSeq;
        this.scheduleName = scheduleName;
        this.scheduleOwnerUserSeq = scheduleOwnerUserSeq;
        this.scheduleOwnerUserName = scheduleOwnerUserName;
    }

    @JsonIgnore
    public static AlarmScheduleAssigned create(Long scheduleSeq, String scheduleName,
        Long scheduleOwnerUserSeq, String scheduleOwnerUserName, Long relatedSeq) {
        return AlarmScheduleAssigned.builder()
            .scheduleSeq(scheduleSeq)
            .scheduleName(scheduleName)
            .scheduleOwnerUserSeq(scheduleOwnerUserSeq)
            .scheduleOwnerUserName(scheduleOwnerUserName)
            .relatedSeq(relatedSeq)
            .build();
    }
}
