package com.pppppp.amadda.friend.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.friend.repository.FriendRequestRepository;
import com.pppppp.amadda.user.entity.User;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class FriendRequestTest extends IntegrationTestSupport {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @AfterEach
    void tearDown() {
        friendRequestRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("친구 요청 상태 수정하고 조회한다. ")
    @Test
    @Transactional
    void updateFriendStatus() {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User u2 = User.create("2222", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        FriendRequest fr = FriendRequest.create(u1, u2); // u1가 u2에게 신청
        List<FriendRequest> friendRequests = friendRequestRepository.saveAll(List.of(fr));

        // when
        List<FriendRequest> savedFr = friendRequestRepository.findAll();
        savedFr.get(0).updateStatus(FriendRequestStatus.ACCEPTED);

        //then
        assertThat(savedFr)
            .extracting("owner", "friend", "status")
            .containsExactlyInAnyOrder(
                tuple(u1, u2, FriendRequestStatus.ACCEPTED)
            );
    }

}
