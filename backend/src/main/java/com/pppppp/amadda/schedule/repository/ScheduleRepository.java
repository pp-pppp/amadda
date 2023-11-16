package com.pppppp.amadda.schedule.repository;

import com.pppppp.amadda.schedule.entity.Schedule;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByAuthorizedUser_UserSeq(Long userSeq);

    List<Schedule> findAllByAuthorizedUser_UserSeqAndScheduleStartAtBefore(
        Long userSeq, LocalDateTime time);

    List<Schedule> findAllByAuthorizedUser_UserSeqAndScheduleStartAtAfter(
        Long userSeq, LocalDateTime time);
}
