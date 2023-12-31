package com.pppppp.amadda.alarm.repository;

import com.pppppp.amadda.alarm.entity.AlarmConfig;
import com.pppppp.amadda.alarm.entity.AlarmType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmConfigRepository extends JpaRepository<AlarmConfig, Long> {

    Optional<AlarmConfig> findByUser_UserSeqAndAlarmType(Long userSeq, AlarmType alarmType);

    Optional<AlarmConfig> findByUser_UserSeqAndAlarmTypeAndIsEnabledFalse(
        Long userSeq, AlarmType alarmType);

    List<AlarmConfig> findAllByUser_UserSeq(Long userSeq);

}
