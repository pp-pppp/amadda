package com.pppppp.amadda.user.service;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.user.dto.request.UserJwtRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenProviderTest extends IntegrationTestSupport {

    @Autowired
    private TokenProvider tokenProvider;

    @DisplayName("토큰이 정상적으로 생성되고 검증된다. ")
    @Test
    void generateTokens() throws InterruptedException {
        // given
        UserJwtRequest request = UserJwtRequest.builder()
                .userSeq(1111L)
                .imageUrl("url1")
                .build();
        List<String> l = tokenProvider.createTokens(request.userSeq());

//        Thread.sleep(1000L);

        // when
        boolean at = tokenProvider.verifyToken(l.get(0));
        boolean rt = tokenProvider.verifyToken(l.get(1));
        Long rak = tokenProvider.decodeRefreshAccessKey(l.get(2));

        // then
        assertThat(at).isTrue();
        assertThat(rt).isTrue();
        assertThat(rak).isEqualTo(1111L);
    }

    @DisplayName("잘못된 refresh 토큰은 예외를 발생시킨다. ")
    @Test
    void verifyTokenFalse() {
        // given
        String token = "assfasdf";

        // when // then
        assertThatThrownBy(() -> tokenProvider.verifyToken(token))
                .isInstanceOf(Exception.class);
    }

    @DisplayName("특정 유저의 rat는 언제나 동일하다. ")
    @Test
    void chkRefreshAccessKey() {
        // given
        Long userSeq = 1111l;

        // when
        String t1 = tokenProvider.generateRefreshAccessKey(userSeq);
        String t2 = tokenProvider.generateRefreshAccessKey(userSeq);

        // then
        assertThat(t1).isEqualTo(t2);
    }


}