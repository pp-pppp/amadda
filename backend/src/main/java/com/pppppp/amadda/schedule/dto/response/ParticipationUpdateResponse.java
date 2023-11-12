package com.pppppp.amadda.schedule.dto.response;

import com.pppppp.amadda.schedule.entity.Participation;
import lombok.Builder;

@Builder
public record ParticipationUpdateResponse(
    Long scheduleId
) {

    public static ParticipationUpdateResponse of(Participation participation) {
        return ParticipationUpdateResponse.builder()
            .scheduleId(participation.getSchedule().getScheduleSeq())
            .build();
    }
}
