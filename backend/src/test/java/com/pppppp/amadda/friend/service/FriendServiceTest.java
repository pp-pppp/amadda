package com.pppppp.amadda.friend.service;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.friend.dto.request.FriendRequestRequest;
import com.pppppp.amadda.friend.dto.response.FriendRequestResponse;
import com.pppppp.amadda.friend.dto.response.FriendResponse;
import com.pppppp.amadda.friend.entity.FriendRequest;
import com.pppppp.amadda.friend.entity.FriendRequestStatus;
import com.pppppp.amadda.friend.repository.FriendRepository;
import com.pppppp.amadda.friend.repository.FriendRequestRepository;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import com.pppppp.amadda.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class FriendServiceTest extends IntegrationTestSupport {

    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private FriendService friendService;
    @Autowired
    private FriendRequestService friendRequestService;

    @AfterEach
    void tearDown() {
        friendRepository.deleteAllInBatch();
        friendRequestRepository.deleteAllInBatch();
    }

    @DisplayName("두 유저가 서로 친구관계가 된다. ")
    @Test
    void beFriends() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1", false);
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2", true);
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        // 친구 요청을 만들고 ACCEPTED 상태로 바꿔줘 친구 요청을 수락한 상황으로 가정
        FriendRequestRequest request = FriendRequestRequest.builder()
                .userSeq(1111L)
                .targetSeq(1234L)
                .build();
        FriendRequestResponse response = friendRequestService.createFriendRequest(request);
        FriendRequest fr = friendRequestService.findFriendRequestBySeq(response.requestSeq()).get();
        response = fr.updateStatus(FriendRequestStatus.ACCEPTED);

        // when
        List<FriendResponse> friendResponse = friendService.createFriend(response);

        // then
        assertThat(friendResponse).hasSize(2)
                .extracting("ownerSeq", "friendSeq")
                .containsExactlyInAnyOrder(
                        tuple(1111L, 1234L),
                        tuple(1234L, 1111L)
                );
    }

    @DisplayName("두 유저의 friendRequest의 상태가 ACCEPTED가 아니라면 예외가 발생한다. ")
    @Test
    void cantBeFriends() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1", false);
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2", true);
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        FriendRequestRequest request = FriendRequestRequest.builder()
                .userSeq(1111L)
                .targetSeq(1234L)
                .build();
        FriendRequestResponse response = friendRequestService.createFriendRequest(request);

        // when // then
        assertThatThrownBy(() -> friendService.createFriend(response))
                .isInstanceOf(RestApiException.class)
                .hasMessage("FRIEND_INVALID");
    }

    @DisplayName("이미 친구인지 판별한다 - true인 경우")
    @Test
    void alreadyFriends() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1", false);
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2", true);
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        FriendRequest fr = FriendRequest.create(u1, u2);
        fr.updateStatus(FriendRequestStatus.ACCEPTED);
        friendService.createFriend(FriendRequestResponse.of(fr));

        // when
        boolean b = friendService.isAlreadyFriends(u1, u2);

        // then
        assertThat(b).isTrue();
    }

    @DisplayName("이미 친구인지 판별한다 - false인 경우")
    @Test
    void notFriends() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1", false);
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2", true);
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        // when
        boolean b = friendService.isAlreadyFriends(u1, u2);

        // then
        assertThat(b).isFalse();

    }

    @DisplayName("친구를 삭제한다. ")
    @Test
    void deleteFriend() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1", false);
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2", true);
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        FriendRequest fr = FriendRequest.create(u1, u2);
        fr.updateStatus(FriendRequestStatus.ACCEPTED);
        friendService.createFriend(FriendRequestResponse.of(fr));

        // when
        friendService.deleteFriends(u1, u2);

        // then
        assertThat(friendRepository.findAll()).hasSize(0);
    }


}