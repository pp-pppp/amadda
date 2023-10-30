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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.tuple;

class FriendRequestServiceTest extends IntegrationTestSupport {

    @Autowired
    private FriendRequestService friendRequestService;
    @Autowired
    private FriendService friendService;
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        friendRequestRepository.deleteAllInBatch();
        friendRepository.deleteAllInBatch();
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
        FriendRequestResponse response = friendRequestService.createFriendRequest(request);

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
        assertThatThrownBy(() -> friendRequestService.createFriendRequest(request))
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
        assertThatThrownBy(() -> friendRequestService.createFriendRequest(request))
                .isInstanceOf(RestApiException.class)
                .hasMessage("FRIEND_REQUEST_INVALID");
    }

    @DisplayName("친구 요청을 받은 사람이 친구 요청을 거절하면 DECLINE 상태로 바뀐다. ")
    @Test
    void declineFriendRequest() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1", false);
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2", true);
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        FriendRequestRequest request = FriendRequestRequest.builder()
                .userSeq(1111L)
                .targetSeq(1234L)
                .build();

        FriendRequestResponse response = friendRequestService.createFriendRequest(request);
        Long requestSeq = response.requestSeq();

        // when
        response = friendRequestService.declineFriendRequest(1234L, requestSeq);

        // then
        assertThat(response).isNotNull();
        assertThat(response).extracting("ownerSeq", "friendSeq", "status")
                .containsExactlyInAnyOrder(1111L, 1234L, "DECLINED");
    }

    @DisplayName("친구 요청을 한사람이 요청을 거절할 수는 없다. 예외가 발생한다. ")
    @Test
    void declineFriendRequestError() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1", false);
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2", true);
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        FriendRequestRequest request = FriendRequestRequest.builder()
                .userSeq(1111L)
                .targetSeq(1234L)
                .build();

        FriendRequestResponse response = friendRequestService.createFriendRequest(request);
        Long requestSeq = response.requestSeq();

        // when // then
        assertThatThrownBy(() -> friendRequestService.declineFriendRequest(1111L, requestSeq))
                .isInstanceOf(RestApiException.class)
                .hasMessage("FRIEND_REQUEST_INVALID");
    }

    @DisplayName("친구 요청을 수락하면 ACCEPTED 상태로 바뀌고 친구 관계가 된다. ")
    @Test
    void acceptFriendRequest() {
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1", false);
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2", true);
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        FriendRequestRequest request = FriendRequestRequest.builder()
                .userSeq(1111L)
                .targetSeq(1234L)
                .build();
        FriendRequestResponse response = friendRequestService.createFriendRequest(request);
        FriendRequest fr = friendRequestService.findFriendRequestBySeq(response.requestSeq()).get();

        // when
        FriendRequestResponse res = friendRequestService.acceptFriendRequest(1234L, fr.getRequestSeq());

        //then
        assertThat(res).isNotNull();
        assertThat(res).extracting("ownerSeq", "friendSeq", "status")
                .containsExactlyInAnyOrder(1111L, 1234L, "ACCEPTED");

        // 추가로 친구 테이블에도 잘 저장되었는지 확인
        assertThat(
                List.of(
                        FriendResponse.of(friendService.findFriendByOwnerAndFriend(u1, u2).get()),
                        FriendResponse.of(friendService.findFriendByOwnerAndFriend(u2, u1).get())
                )
        ).hasSize(2)
                .extracting("ownerSeq", "friendSeq")
                .containsExactlyInAnyOrder(
                        tuple(1111L, 1234L),
                        tuple(1234L, 1111L)
                );
    }

    @DisplayName("두 유저가 이미 친구라면 해당 요청에 대한 예외가 발생한다. ")
    @Test
    void alreadyFriends() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1", false);
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2", true);
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        FriendRequestRequest request = FriendRequestRequest.builder()
                .userSeq(1111L)
                .targetSeq(1234L)
                .build();
        FriendRequestResponse response = friendRequestService.createFriendRequest(request);
        FriendRequest fr = friendRequestService.findFriendRequestBySeq(response.requestSeq()).get();
        friendRequestService.acceptFriendRequest(1234L, fr.getRequestSeq());

        // when // then
        assertThatThrownBy(() -> friendRequestService.acceptFriendRequest(1234L, fr.getRequestSeq()))
                .isInstanceOf(RestApiException.class)
                .hasMessage("FRIEND_REQUEST_INVALID");
    }

}