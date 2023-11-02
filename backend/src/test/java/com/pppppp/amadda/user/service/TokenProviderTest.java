package com.pppppp.amadda.user.service;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.user.dto.request.UserJwtRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
        List<String> l = tokenProvider.createTokens(request);

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

}