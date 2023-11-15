package com.pppppp.amadda.alarm.repository;

import com.pppppp.amadda.alarm.entity.Alarm;
import com.pppppp.amadda.alarm.entity.AlarmType;
import com.pppppp.amadda.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    List<Alarm> findAllByUserAndIsReadFalseAndIsDeletedFalse(User user);

    @Query("select a from Alarm a "
        + "where a.user = :user "
        + "and a.alarmType = :alarmType "
        + "and a.isRead = false "
        + "and a.content like concat(:name, '%') ")
    Optional<Alarm> findFriendRequestAlarm(@Param("user") User user,
        @Param("alarmType") AlarmType alarmType, @Param("name") String name);
}
