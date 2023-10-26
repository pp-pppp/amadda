package com.pppppp.amadda.friend.repository;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.friend.entity.Friend;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

class FriendRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private FriendRepository friendRepository;

    @DisplayName("친구 신청을 보내면 friend_request 테이블에 친구 요청 보냄 상태로 저장된다. ")
    @Test
    void sendFriendRequest() {
        // given
//        Friend f1 = Friend.


        // when


        // then

    }

}