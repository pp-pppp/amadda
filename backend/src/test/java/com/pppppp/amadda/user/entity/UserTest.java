package com.pppppp.amadda.user.entity;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

class UserTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("유저 탈퇴시 유저 테이터를 다 초기화. ")
    @Test
    @Transactional
    void deleteUser() {
        // given
        User u1 = User.create(0L, "유저1", "id1", "imageUrl1");
        User u = userRepository.save(u1);

        // when
        u.delete();

        // then
        assertThat(userRepository.findAll())
                .extracting("userSeq", "userName", "userId", "imageUrl")
                .containsExactlyInAnyOrder(
                        tuple(u.getUserSeq(), "", "", "")
                );
    }


}