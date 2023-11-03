package com.pppppp.amadda.schedule.repository;

import com.pppppp.amadda.schedule.entity.Schedule;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Optional<Schedule> findByScheduleSeqAndIsDeletedFalse(Long scheduleSeq);

    List<Schedule> findAllByUser_UserSeqAndIsDeletedFalse(Long userSeq);

    List<Schedule> findAllByUser_UserSeqAndScheduleStartAtBeforeAndIsDeletedFalse(
        Long userSeq, LocalDateTime time);

    List<Schedule> findAllByUser_UserSeqAndScheduleStartAtAfterAndIsDeletedFalse(
        Long userSeq, LocalDateTime time);
}
