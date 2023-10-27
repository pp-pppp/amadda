package com.pppppp.amadda.friend.service;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.friend.dto.request.FriendRequestRequest;
import com.pppppp.amadda.friend.dto.response.FriendRequestResponse;
import com.pppppp.amadda.friend.entity.FriendRequest;
import com.pppppp.amadda.friend.entity.FriendRequestStatus;
import com.pppppp.amadda.friend.repository.FriendRequestRepository;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FriendRequestServiceTest extends IntegrationTestSupport {

    @Autowired
    private FriendRequestService friendRequestService;
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        friendRequestRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("다른 유저에게 친구 신청을 할 수 있다. ")
    @Test
    void sendFriendRequest() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1", false);
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2", true);
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        FriendRequestRequest request = FriendRequestRequest.builder()
                .userSeq(1111L)
                .targetSeq(1234L)
                .build();

        // when
        FriendRequestResponse response = friendRequestService.createRequest(request);

        //then
        assertThat(response).isNotNull();
        assertThat(response).extracting("ownerSeq", "friendSeq", "status")
                .containsExactlyInAnyOrder(1111L, 1234L, "REQUESTED");
    }

    @DisplayName("이미 친구인 유저에게 친구 신청을 하면 예외가 발생한다. ")
    @Test
    void sendAlreadyRequestedRequest() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1", false);
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2", true);
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        FriendRequest fr = FriendRequest.create(u1, u2); // u1가 u2에게 신청
        List<FriendRequest> friendRequests = friendRequestRepository.saveAll(List.of(fr));

        FriendRequestRequest request = FriendRequestRequest.builder()
                .userSeq(1111L)
                .targetSeq(1234L)
                .build();

        // when // then
        assertThatThrownBy(() -> friendRequestService.createRequest(request))
                .isInstanceOf(RestApiException.class)
                .hasMessage("FRIEND_REQUEST_INVALID");
    }

    @DisplayName("이미 친구인 유저에게 친구 신청을 하면 예외가 발생한다. ")
    @Test
    void sendAlreadyFriendRequest() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1", false);
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2", true);
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        FriendRequest fr = FriendRequest.create(u1, u2); // u1가 u2에게 신청
        List<FriendRequest> friendRequests = friendRequestRepository.saveAll(List.of(fr));

        List<FriendRequest> savedFr = friendRequestRepository.findAll();
        savedFr.get(0).updateStatus(FriendRequestStatus.ACCEPTED); // 수정

        FriendRequestRequest request = FriendRequestRequest.builder()
                .userSeq(1111L)
                .targetSeq(1234L)
                .build();

        // when // then
        assertThatThrownBy(() -> friendRequestService.createRequest(request))
                .isInstanceOf(RestApiException.class)
                .hasMessage("FRIEND_REQUEST_INVALID");
    }

}