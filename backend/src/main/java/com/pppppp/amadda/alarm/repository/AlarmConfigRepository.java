package com.pppppp.amadda.alarm.repository;

import com.pppppp.amadda.alarm.entity.AlarmConfig;
import com.pppppp.amadda.alarm.entity.AlarmType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmConfigRepository extends JpaRepository<AlarmConfig, Long> {

    Optional<AlarmConfig> findByUser_UserSeqAndAlarmTypeAndIsDeletedFalse(Long userSeq,
        AlarmType alarmType);

    Optional<AlarmConfig> findByUser_UserSeqAndAlarmTypeAndIsEnabledFalseAndIsDeletedFalse(
        Long userSeq, AlarmType alarmType);

}
