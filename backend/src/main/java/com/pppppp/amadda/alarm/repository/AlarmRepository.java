package com.pppppp.amadda.alarm.repository;

import com.pppppp.amadda.alarm.entity.Alarm;
import com.pppppp.amadda.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    List<Alarm> findAllByUserAndIsReadFalseAndIsDeletedFalse(User user);
}
