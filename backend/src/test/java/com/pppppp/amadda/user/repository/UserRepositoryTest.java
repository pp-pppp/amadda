package com.pppppp.amadda.user.repository;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.UserErrorCode;
import com.pppppp.amadda.user.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;


class UserRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("유저를 저장하고 조회한다. ")
    @Test
    void saveUser() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");

        // when
        List<User> savedUsers = userRepository.saveAll(List.of(u1, u2));

        //then
        assertThat(savedUsers).hasSize(2)
                .extracting("userSeq", "userName", "userId", "imageUrl")
                .containsExactlyInAnyOrder(
                        tuple(1111L, "유저1", "id1", "imageUrl1"),
                        tuple(1234L, "유저2", "id2", "imageUrl2")
                );
    }

    @DisplayName("userSeq로 유저를 조회할 수 있다. ")
    @Test
    void findUserByUserSeq() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        userRepository.saveAll(List.of(u1, u2));

        // when
        User u = userRepository.findByUserSeq(1111L).get();

        //then
        assertThat(u).isNotNull();
        assertThat(u).extracting("userSeq", "userName", "userId", "imageUrl")
                .containsExactlyInAnyOrder(1111L, "유저1", "id1", "imageUrl1");
    }

    @DisplayName("유저 아이디로 해당 유저를 조회한다. ")
    @Test
    void findByUserId() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        userRepository.saveAll(List.of(u1, u2));

        // when
        User u = userRepository.findByUserId("id2").get();

        // then
        assertThat(u).isNotNull();
        assertThat(u).extracting("userSeq", "userName", "userId", "imageUrl")
                .containsExactlyInAnyOrder(1234L, "유저2", "id2", "imageUrl2");
    }

}