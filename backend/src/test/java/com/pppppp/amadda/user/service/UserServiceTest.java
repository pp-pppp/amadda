package com.pppppp.amadda.user.service;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.UserErrorCode;
import com.pppppp.amadda.user.dto.response.UserReadResponse;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("userSeq로 유저를 조회할 수 있다.")
    @Test
    void findUserByUserSeq() {
        // given
        User u1 = User.create(1111l, "유저1", "id1", "imageUrl1", false);
        User u2 = User.create(1234l, "유저2", "id2", "imageUrl2", true);
        userRepository.saveAll(List.of(u1, u2));

        // when
        User u = userService.findUserByUserSeq(1111l)
                .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));

        // then
        assertThat(u).isNotNull();
        assertThat(u).extracting("userSeq", "userName", "userId", "imageUrl", "isInited")
                .containsExactlyInAnyOrder(1111l, "유저1", "id1", "imageUrl1", false);
    }
}