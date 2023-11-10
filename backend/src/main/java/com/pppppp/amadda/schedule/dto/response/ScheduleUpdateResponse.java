package com.pppppp.amadda.schedule.dto.response;

import com.pppppp.amadda.schedule.entity.Schedule;
import lombok.Builder;

@Builder
public record ScheduleUpdateResponse(
    Long scheduleSeq
) {

    public static ScheduleUpdateResponse of(Schedule schedule) {
        return ScheduleUpdateResponse.builder()
            .scheduleSeq(schedule.getScheduleSeq())
            .build();
    }
}
