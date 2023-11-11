package com.pppppp.amadda.schedule.repository;

import com.pppppp.amadda.schedule.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findBySchedule_ScheduleSeqAndIsDeletedFalse(Long scheduleSeq);

}
