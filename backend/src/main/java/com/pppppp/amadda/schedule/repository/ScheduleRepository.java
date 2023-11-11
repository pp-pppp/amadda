package com.pppppp.amadda.schedule.repository;

import com.pppppp.amadda.schedule.entity.Schedule;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByAuthorizedUser_UserSeqAndIsDeletedFalse(Long userSeq);

    List<Schedule> findAllByAuthorizedUser_UserSeqAndScheduleStartAtBeforeAndIsDeletedFalse(
        Long userSeq, LocalDateTime time);

    List<Schedule> findAllByAuthorizedUser_UserSeqAndScheduleStartAtAfterAndIsDeletedFalse(
        Long userSeq, LocalDateTime time);
}
