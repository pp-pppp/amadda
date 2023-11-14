package com.pppppp.amadda.schedule.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.schedule.entity.Category;
import com.pppppp.amadda.schedule.entity.CategoryColor;
import com.pppppp.amadda.schedule.entity.Schedule;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CategoryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

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
        categoryRepository.deleteAllInBatch();
        scheduleRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    // ====================================== Create Test ====================================== //

    @DisplayName("새로운 카테고리를 생성한다.")
    @Test
    void createNewCategory() {
        // given
        User user = userRepository.findAll().get(0);
        Category category = Category.builder()
            .user(user)
            .categoryName("자율기절")
            .categoryColor(CategoryColor.HOTPINK)
            .build();

        // when
        Category c = categoryRepository.save(category);

        // then
        assertThat(c)
            .extracting("user", "categoryName", "categoryColor")
            .containsExactly(user, "자율기절", CategoryColor.HOTPINK);
    }

    // ====================================== Read Test ====================================== //

    @DisplayName("사용자의 카테고리 목록을 보여준다.")
    @Transactional
    @Test
    void getCategoryListByUserSeq() {
        // given
        User user = userRepository.findAll().get(0);
        Category category1 = Category.builder()
            .user(user)
            .categoryName("자율기절")
            .categoryColor(CategoryColor.HOTPINK)
            .build();
        Category category2 = Category.builder()
            .user(user)
            .categoryName("합창단")
            .categoryColor(CategoryColor.GREEN)
            .build();
        categoryRepository.saveAll(List.of(category1, category2));

        // when
        List<Category> categories = categoryRepository.findByUser_UserSeqAndIsDeletedFalse(
            user.getUserSeq());

        // then
        assertThat(categories)
            .extracting("user", "categoryName", "categoryColor")
            .containsExactlyInAnyOrder(
                tuple(user, "자율기절", CategoryColor.HOTPINK),
                tuple(user, "합창단", CategoryColor.GREEN));
    }
}