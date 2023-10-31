package com.pppppp.amadda.schedule.repository;

import com.pppppp.amadda.schedule.entity.Participation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    Optional<Participation> findBySchedule_ScheduleSeqAndUser_UserSeqAndIsDeletedFalse(
        Long scheduleSeq,
        Long userSeq);
}
