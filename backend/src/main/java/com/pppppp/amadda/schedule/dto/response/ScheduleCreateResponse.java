package com.pppppp.amadda.schedule.dto.response;

import com.pppppp.amadda.schedule.entity.Schedule;
import lombok.Builder;

@Builder
public record ScheduleCreateResponse(
    Long scheduleSeq
) {

    public static ScheduleCreateResponse of(Schedule schedule) {
        return ScheduleCreateResponse.builder()
            .scheduleSeq(schedule.getScheduleSeq())
            .build();
    }
}
