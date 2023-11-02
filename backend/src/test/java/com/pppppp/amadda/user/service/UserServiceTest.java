package com.pppppp.amadda.user.service;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.user.dto.request.UserJwtRequest;
import com.pppppp.amadda.user.dto.response.UserJwtResponse;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenProvider tokenProvider;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("userSeq로 유저를 조회할 수 있다.")
    @Test
    void getUserByUserSeq() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1", false);
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2", true);
        userRepository.saveAll(List.of(u1, u2));

        // when
        User u = userService.getUserInfoBySeq(1111L);

        // then
        assertThat(u).isNotNull();
        assertThat(u).extracting("userName", "userId", "imageUrl", "isInited")
                .containsExactlyInAnyOrder("유저1", "id1", "imageUrl1", false);
    }

    @DisplayName("존재하지 않는 userSeq로 유저를 조회하면 예외가 발생한다. ")
    @Test
    void getUnexpectedUserByUserSeq() {

        // when // then
        assertThatThrownBy(() -> userService.getUserInfoBySeq(1111L))
                .isInstanceOf(RestApiException.class)
                .hasMessage("USER_NOT_FOUND");
    }

    @DisplayName("로그인 후 토큰이랑 isInited 값을 잘 받아온다. ")
    @Test
    void getTokensAndCheckInit() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1", false);
        List<User> users = userRepository.saveAll(List.of(u1));

        UserJwtRequest request = UserJwtRequest.builder()
                .userSeq(1111L)
                .imageUrl("url1")
                .build();

        // when
        UserJwtResponse response = userService.getTokensAndCheckInit(request);
        boolean at = tokenProvider.verifyToken(response.accessToken());
        boolean rt = tokenProvider.verifyToken(response.refreshToken());
        Long rak = tokenProvider.decodeRefreshAccessKey(response.refreshAccessKey());
        boolean isInited = response.isInited();

        // then
        assertThat(at).isTrue();
        assertThat(rt).isTrue();
        assertThat(rak).isEqualTo(1111L);
        assertThat(isInited).isFalse();
    }

}