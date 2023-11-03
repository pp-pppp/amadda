package com.pppppp.amadda.friend.repository;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.friend.entity.FriendRequest;
import com.pppppp.amadda.friend.entity.FriendRequestStatus;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class FriendRequestRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        friendRequestRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("친구 신청을 저장하고 조회한다. ")
    @Test
    @Transactional
    void sendFriendRequest() {
        // given
        // 사람 두명 만들고 그 두명으로 친구 신청
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        FriendRequest fr = FriendRequest.create(u1, u2); // u1가 u2에게 신청

        // when
        FriendRequest friendRequests = friendRequestRepository.save(fr);

        // then
        assertThat(friendRequests)
                .extracting("owner", "friend", "status")
                .containsExactlyInAnyOrder(u1, u2, FriendRequestStatus.REQUESTED);

    }

    @DisplayName("친구 신청자와 대상자로 요청기록을 조회한다. ")
    @Test
    @Transactional
    void findFriendRequest() {
        // given
        // 사람 두명 만들고 그 두명으로 친구 신청
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        FriendRequest fr = FriendRequest.create(u1, u2); // u1가 u2에게 신청
        FriendRequest friendRequests = friendRequestRepository.save(fr);

        // when
        FriendRequest search = friendRequestRepository.findByOwnerAndFriend(u1, u2).get();

        // then
        assertThat(search)
                .extracting("owner", "friend", "status")
                .containsExactlyInAnyOrder(u1, u2, FriendRequestStatus.REQUESTED);
    }

    @DisplayName("친구 요청 기록을 삭제할 수 있다. ")
    @Test
    void deleteFriendRequest() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        FriendRequest fr = FriendRequest.create(u1, u2); // u1가 u2에게 신청
        FriendRequest friendRequests = friendRequestRepository.save(fr);

        // when
        friendRequestRepository.deleteById(fr.getRequestSeq());

        // then
        assertThat(friendRequestRepository.findAll())
                .hasSize(0);
    }

}