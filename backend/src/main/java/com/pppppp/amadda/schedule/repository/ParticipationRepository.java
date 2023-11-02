package com.pppppp.amadda.schedule.repository;

import com.pppppp.amadda.schedule.entity.Participation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    Optional<Participation> findBySchedule_ScheduleSeqAndUser_UserSeqAndIsDeletedFalse(
        Long scheduleSeq,
        Long userSeq);

    List<Participation> findByUser_UserSeqAndIsDeletedFalse(Long userSeq);

    List<Participation> findBySchedule_ScheduleSeqAndIsDeletedFalse(Long scheduleSeq);

    List<Participation> findByUser_UserSeqAndCategory_CategorySeqAndIsDeletedFalse(
        Long userSeq, Long categorySeq);
}
