package com.pppppp.amadda.schedule.repository;

import com.pppppp.amadda.schedule.entity.AlarmTime;
import com.pppppp.amadda.schedule.entity.Participation;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    Optional<Participation> findBySchedule_ScheduleSeqAndUser_UserSeq
        (Long scheduleSeq, Long userSeq);

    List<Participation> findByUser_UserSeq(Long userSeq);

    List<Participation> findBySchedule_ScheduleSeq(Long scheduleSeq);

    @Query("select p from Participation p "
        + "where p.user.userSeq = :userSeq and p.scheduleName like %:scheduleName%")
    List<Participation> findByUser_UserSeqAndScheduleNameContaining(
        @Param("userSeq") Long userSeq, @Param("scheduleName") String scheduleName);

    List<Participation> findByCategory_CategorySeq(Long categorySeq);

    List<Participation> findByUser_UserSeqAndCategory_CategorySeq
        (Long userSeq, Long categorySeq);

    List<Participation> findAllByIsAlarmedFalseAndAlarmTimeNotAndAlarmAtBetween
        (AlarmTime alarmTime, LocalDateTime start, LocalDateTime end);
}
