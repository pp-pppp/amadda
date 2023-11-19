package com.pppppp.amadda.schedule.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.schedule.entity.Comment;
import com.pppppp.amadda.schedule.entity.Schedule;
import com.pppppp.amadda.user.entity.User;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CommentRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @BeforeEach
    void setUp() {
        User user1 = User.create("1L", "박동건", "icebearrrr", "url1");
        User user2 = User.create("2L", "정민영", "minyoung", "url2");
        userRepository.saveAll(List.of(user1, user2));

        Schedule schedule1 = Schedule.builder()
            .authorizedUser(user1)
            .isTimeSelected(false)
            .isDateSelected(false)
            .build();
        Schedule schedule2 = Schedule.builder()
            .authorizedUser(user2)
            .isTimeSelected(false)
            .isDateSelected(false)
            .build();
        Schedule schedule3 = Schedule.builder()
            .authorizedUser(user1)
            .isTimeSelected(false)
            .isDateSelected(false)
            .build();
        scheduleRepository.saveAll(List.of(schedule1, schedule2, schedule3));
    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAllInBatch();
        scheduleRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    // ====================================== Create Test ====================================== //

    @DisplayName("해당 일정에 새로운 댓글을 작성한다.")
    @Test
    void createNewComment() {
        // given
        User user = userRepository.findAll().get(0);
        Schedule schedule = scheduleRepository.findAll().get(0);
        Comment comment = Comment.builder()
            .user(user)
            .schedule(schedule)
            .commentContent("우리 언제 만나")
            .build();

        // when
        Comment c = commentRepository.save(comment);

        // then
        assertThat(c)
            .extracting("user", "schedule", "commentContent")
            .containsExactlyInAnyOrder(user, schedule, "우리 언제 만나");
    }

    // ====================================== Read Test ====================================== //

    @DisplayName("한 일정의 모든 댓글을 조회한다.")
    @Transactional
    @Test
    void getComments() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u2 = userRepository.findAll().get(1);
        Schedule schedule = scheduleRepository.findAll().get(0);
        Comment c1 = Comment.builder()
            .user(u1)
            .schedule(schedule)
            .commentContent("방탈출 할 사람")
            .build();
        Comment c2 = Comment.builder()
            .user(u2)
            .schedule(schedule)
            .commentContent("ㅈㅅ 못함")
            .build();
        Comment c3 = Comment.builder()
            .user(u1)
            .schedule(schedule)
            .commentContent("그냥 죽을게")
            .build();
        commentRepository.saveAll(List.of(c1, c2, c3));

        // when
        List<Comment> comments = commentRepository.findBySchedule_ScheduleSeq(
            schedule.getScheduleSeq());

        // then
        assertThat(comments).hasSize(3)
            .extracting("user", "schedule", "commentContent")
            .containsExactlyInAnyOrder(
                tuple(u1, schedule, "방탈출 할 사람"),
                tuple(u2, schedule, "ㅈㅅ 못함"),
                tuple(u1, schedule, "그냥 죽을게"));
    }


    @DisplayName("여러 일정 중 해당하는 일정의 댓글만 가져온다.")
    @Transactional
    @Test
    void getCommentsByScheduleSeq() {
        // given
        User u1 = userRepository.findAll().get(0);
        User u2 = userRepository.findAll().get(1);
        Schedule s1 = scheduleRepository.findAll().get(0);
        Schedule s2 = scheduleRepository.findAll().get(1);
        Comment c1 = Comment.builder()
            .user(u1)
            .schedule(s1)
            .commentContent("방탈출 할 사람")
            .build();
        Comment c2 = Comment.builder()
            .user(u2)
            .schedule(s2)
            .commentContent("아 집가고 싶다")
            .build();
        Comment c3 = Comment.builder()
            .user(u1)
            .schedule(s1)
            .commentContent("없니..")
            .build();
        commentRepository.saveAll(List.of(c1, c2, c3));

        // when
        List<Comment> comments = commentRepository.findBySchedule_ScheduleSeq(
            s1.getScheduleSeq());

        // then
        assertThat(comments).hasSize(2)
            .extracting("user", "schedule", "commentContent")
            .containsExactlyInAnyOrder(
                tuple(u1, s1, "방탈출 할 사람"),
                tuple(u1, s1, "없니.."));
    }
}
