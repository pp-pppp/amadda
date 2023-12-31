package com.pppppp.amadda.global.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.global.entity.exception.RestApiException;

import java.util.List;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

class TokenProviderTest extends IntegrationTestSupport {

    @Autowired
    private TokenProvider tokenProvider;

    @DisplayName("토큰이 정상적으로 생성되고 검증된다. ")
    @Test
    void generateTokens() throws InterruptedException {
        // given
        List<String> l = tokenProvider.createTokens(1111L);

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
        Long userSeq = 1111L;

        // when
        String t1 = tokenProvider.generateRefreshAccessKey(userSeq);
        String t2 = tokenProvider.generateRefreshAccessKey(userSeq);

        // then
        assertThat(t1).isEqualTo(t2);
    }

    @DisplayName("토큰으로 userSeq를 가져온다. ")
    @Test
    void parseUserSeq() {
        // given
        List<String> l = tokenProvider.createTokens(1111L);

        // when
        Long userSeq = tokenProvider.parseUserSeq(l.get(0));

        // then
        assertThat(userSeq).isEqualTo(1111L);
    }

    @DisplayName("헤더에서 토큰을 추출한다. - 정상")
    @Test
    void getTokenFromHeader() {
        // given
        MockHttpServletRequest http = new MockHttpServletRequest();
        http.addHeader("Authorization", "Bearer originalToken");

        // when
        String token = tokenProvider.getTokenFromHeader(http);

        // then
        assertThat(token).isEqualTo("originalToken");
    }

    @DisplayName("헤더에서 토큰을 추출한다. - 헤더 내에 해당 키 없음. ")
    @Test
    void getTokenFromCookie_cookieNoKey() {
        // given
        MockHttpServletRequest http = new MockHttpServletRequest();

        // when // then
        assertThatThrownBy(() -> tokenProvider.getTokenFromHeader(http))
                .isInstanceOf(RestApiException.class)
                .hasMessage("HTTP_HEADER_KEY_NOT_FOUND");
    }

    @DisplayName("헤더를 주고 유저 seq를 반환받는다. ")
    @Test
    void getUserSeq() {
        // given
        List<String> l = tokenProvider.createTokens(1111L);

        MockHttpServletRequest http = new MockHttpServletRequest();
        http.addHeader("Authorization", "Bearer "+l.get(0));

        // when
        Long userSeq = tokenProvider.getUserSeq(http);

        // then
        assertThat(userSeq).isEqualTo(1111L);
    }

}
