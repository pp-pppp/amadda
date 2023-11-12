package com.pppppp.amadda.schedule.dto.response;

import com.pppppp.amadda.schedule.entity.Participation;
import lombok.Builder;

@Builder
public record ParticipationUpdateResponse(
    Long scheduleSeq
) {

    public static ParticipationUpdateResponse of(Participation participation) {
        return ParticipationUpdateResponse.builder()
            .scheduleSeq(participation.getSchedule().getScheduleSeq())
            .build();
    }
}
