package com.pppppp.amadda.user.service;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.user.dto.request.UserInitRequest;
import com.pppppp.amadda.user.dto.request.UserJwtRequest;
import com.pppppp.amadda.user.dto.request.UserRefreshRequest;
import com.pppppp.amadda.user.dto.response.UserAccessResponse;
import com.pppppp.amadda.user.dto.response.UserJwtInitResponse;
import com.pppppp.amadda.user.dto.response.UserJwtResponse;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

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
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        userRepository.saveAll(List.of(u1, u2));

        // when
        User u = userService.getUserInfoBySeq(1111L);

        // then
        assertThat(u).isNotNull();
        assertThat(u).extracting("userName", "userId", "imageUrl")
                .containsExactlyInAnyOrder("유저1", "id1", "imageUrl1");
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
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        List<User> users = userRepository.saveAll(List.of(u1));

        UserJwtRequest request = UserJwtRequest.builder()
                .userSeq(1111L)
                .imageUrl("url1")
                .build();

        // when
        UserJwtInitResponse response = userService.getTokensAndCheckInit(request);
        boolean at = tokenProvider.verifyToken(response.accessToken());
        boolean rt = tokenProvider.verifyToken(response.refreshToken());
        Long rak = tokenProvider.decodeRefreshAccessKey(response.refreshAccessKey());
        boolean isInited = response.isInited();

        // then
        assertThat(at).isTrue();
        assertThat(rt).isTrue();
        assertThat(rak).isEqualTo(1111L);
        assertThat(isInited).isTrue();
    }

    @DisplayName("새로운 토큰들을 발급받는다. ")
    @Test
    void getNewTokens() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        List<User> users = userRepository.saveAll(List.of(u1));

        UserJwtRequest request = UserJwtRequest.builder()
                .userSeq(1111L)
                .imageUrl("url1")
                .build();
        UserJwtInitResponse response = userService.getTokensAndCheckInit(request);

        UserRefreshRequest r = UserRefreshRequest.builder()
                .refreshToken(response.refreshToken())
                .build();

        // when
        UserJwtResponse res = userService.getNewTokens(r, 1111L);
        boolean at = tokenProvider.verifyToken(res.accessToken());
        boolean rt = tokenProvider.verifyToken(res.refreshToken());
        Long rak = tokenProvider.decodeRefreshAccessKey(res.refreshAccessKey());

        // then
        assertThat(at).isTrue();
        assertThat(rt).isTrue();
        assertThat(rak).isEqualTo(1111L);
    }

    @DisplayName("신규/기존 유저를 구분한다. ")
    @Test
    void checkIsInited() {
        // given
        Long userSeq = 0L;

        // when
        boolean b = userService.checkIsInited(userSeq);

        // then
        assertThat(b).isFalse();
    }

    @DisplayName("유저를 잘 저장한다. ")
    @Test
    void saveUser() {
        // given
        UserInitRequest request = UserInitRequest.builder()
                .userId("jammminjung")
                .userName("잼민정")
                .imageUrl("imgUrl")
                .userSeq(1111L)
                .build();

        // when
        userService.saveUser(request);

        // then
        assertThat(userRepository.findAll()).hasSize(1)
                .extracting("userId", "userName", "imageUrl", "userSeq")
                .containsExactly(
                        tuple("jammminjung", "잼민정", "imgUrl", 1111L)
                );
    }

    @DisplayName("정상 토큰인 경우 ")
    @Test
    void validateUser1() {
        // given
        String token = tokenProvider.createTokens(1111L).get(0);

        // when
        UserAccessResponse response = userService.validateUser(token);

        // then
        assertThat(response)
                .extracting("isExpired", "isBroken", "refreshAccessKey")
                .containsExactly(false, false, "-");

    }

    @DisplayName("망가진 토큰인 경우 ")
    @Test
    void validateUser3() {
        // given
        String token = "broken-token";

        // when
        UserAccessResponse response = userService.validateUser(token);

        // then
        assertThat(response)
                .extracting("isExpired", "isBroken", "refreshAccessKey")
                .containsExactly(true, true, "-");
    }
}