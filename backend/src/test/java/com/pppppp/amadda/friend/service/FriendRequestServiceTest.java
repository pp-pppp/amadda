package com.pppppp.amadda.friend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.friend.dto.request.FriendRequestRequest;
import com.pppppp.amadda.friend.dto.response.FriendRequestResponse;
import com.pppppp.amadda.friend.dto.response.FriendResponse;
import com.pppppp.amadda.friend.entity.FriendRequest;
import com.pppppp.amadda.friend.entity.FriendRequestStatus;
import com.pppppp.amadda.friend.entity.GroupMember;
import com.pppppp.amadda.friend.entity.UserGroup;
import com.pppppp.amadda.friend.repository.FriendRepository;
import com.pppppp.amadda.friend.repository.FriendRequestRepository;
import com.pppppp.amadda.friend.repository.GroupMemberRepository;
import com.pppppp.amadda.friend.repository.UserGroupRepository;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @AfterEach
    void tearDown() {
        groupMemberRepository.deleteAllInBatch();
        userGroupRepository.deleteAllInBatch();
        friendRequestRepository.deleteAllInBatch();
        friendRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("다른 유저에게 친구 신청을 할 수 있다. ")
    @Test
    void sendFriendRequest() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        FriendRequestRequest request = FriendRequestRequest.builder()
            .userSeq(users.get(0).getUserSeq())
            .targetSeq(users.get(1).getUserSeq())
            .build();

        // when
        FriendRequestResponse response = friendRequestService.createFriendRequest(request);

        //then
        assertThat(response).isNotNull();
        assertThat(response).extracting("ownerSeq", "friendSeq", "status")
            .containsExactlyInAnyOrder(users.get(0).getUserSeq(), users.get(1).getUserSeq(), "REQUESTED");
    }

    @DisplayName("이미 친구인 유저에게 친구 신청을 하면 예외가 발생한다. ")
    @Test
    void sendAlreadyRequestedRequest() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        FriendRequest fr = FriendRequest.create(u1, u2); // u1가 u2에게 신청
        List<FriendRequest> friendRequests = friendRequestRepository.saveAll(List.of(fr));

        FriendRequestRequest request = FriendRequestRequest.builder()
            .userSeq(users.get(0).getUserSeq())
            .targetSeq(users.get(1).getUserSeq())
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
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        FriendRequest fr = FriendRequest.create(u1, u2); // u1가 u2에게 신청
        List<FriendRequest> friendRequests = friendRequestRepository.saveAll(List.of(fr));

        List<FriendRequest> savedFr = friendRequestRepository.findAll();
        savedFr.get(0).updateStatus(FriendRequestStatus.ACCEPTED); // 수정

        FriendRequestRequest request = FriendRequestRequest.builder()
            .userSeq(users.get(0).getUserSeq())
            .targetSeq(users.get(1).getUserSeq())
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
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        FriendRequestRequest request = FriendRequestRequest.builder()
            .userSeq(users.get(0).getUserSeq())
            .targetSeq(users.get(1).getUserSeq())
            .build();

        FriendRequestResponse response = friendRequestService.createFriendRequest(request);
        Long requestSeq = response.requestSeq();

        // when
        response = friendRequestService.declineFriendRequest(users.get(1).getUserSeq(), requestSeq);

        // then
        assertThat(response).isNotNull();
        assertThat(response).extracting("ownerSeq", "friendSeq", "status")
            .containsExactlyInAnyOrder(users.get(0).getUserSeq(), users.get(1).getUserSeq(), "DECLINED");
    }

    @DisplayName("친구 요청을 한사람이 요청을 거절할 수는 없다. 예외가 발생한다. ")
    @Test
    void declineFriendRequestError() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        FriendRequestRequest request = FriendRequestRequest.builder()
            .userSeq(users.get(0).getUserSeq())
            .targetSeq(users.get(1).getUserSeq())
            .build();

        FriendRequestResponse response = friendRequestService.createFriendRequest(request);
        Long requestSeq = response.requestSeq();

        // when // then
        assertThatThrownBy(() -> friendRequestService.declineFriendRequest(users.get(0).getUserSeq(), requestSeq))
            .isInstanceOf(RestApiException.class)
            .hasMessage("FRIEND_REQUEST_INVALID");
    }

    @DisplayName("친구 요청을 수락하면 ACCEPTED 상태로 바뀌고 친구 관계가 된다. ")
    @Test
    void acceptFriendRequest() {
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        FriendRequestRequest request = FriendRequestRequest.builder()
            .userSeq(users.get(0).getUserSeq())
            .targetSeq(users.get(1).getUserSeq())
            .build();
        FriendRequestResponse response = friendRequestService.createFriendRequest(request);
        FriendRequest fr = friendRequestService.findFriendRequestBySeq(response.requestSeq()).get();

        // when
        FriendRequestResponse res = friendRequestService.acceptFriendRequest(users.get(1).getUserSeq(),
            fr.getRequestSeq());

        //then
        assertThat(res).isNotNull();
        assertThat(res).extracting("ownerSeq", "friendSeq", "status")
            .containsExactlyInAnyOrder(users.get(0).getUserSeq(), users.get(1).getUserSeq(), "ACCEPTED");

        // 추가로 친구 테이블에도 잘 저장되었는지 확인
        assertThat(
            List.of(
                FriendResponse.of(friendService.findFriendByOwnerAndFriend(u1, u2).get()),
                FriendResponse.of(friendService.findFriendByOwnerAndFriend(u2, u1).get())
            )
        ).hasSize(2)
            .extracting("ownerSeq", "friendSeq")
            .containsExactlyInAnyOrder(
                tuple(users.get(0).getUserSeq(), users.get(1).getUserSeq()),
                tuple(users.get(1).getUserSeq(), users.get(0).getUserSeq())
            );
    }

    @DisplayName("두 유저가 이미 친구라면 해당 요청에 대한 예외가 발생한다. ")
    @Test
    void alreadyFriends() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        FriendRequestRequest request = FriendRequestRequest.builder()
            .userSeq(users.get(0).getUserSeq())
            .targetSeq(users.get(1).getUserSeq())
            .build();
        FriendRequestResponse response = friendRequestService.createFriendRequest(request);
        FriendRequest fr = friendRequestService.findFriendRequestBySeq(response.requestSeq()).get();
        friendRequestService.acceptFriendRequest(users.get(1).getUserSeq(), fr.getRequestSeq());

        // when // then
        assertThatThrownBy(
            () -> friendRequestService.acceptFriendRequest(users.get(1).getUserSeq(), fr.getRequestSeq()))
            .isInstanceOf(RestApiException.class)
            .hasMessage("FRIEND_REQUEST_INVALID");
    }

    @DisplayName("친구요청 기록을 삭제한다. ")
    @Test
    void deleteFriendAndRequest() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        FriendRequestRequest request = FriendRequestRequest.builder()
            .userSeq(users.get(0).getUserSeq())
            .targetSeq(users.get(1).getUserSeq())
            .build();
        FriendRequestResponse response = friendRequestService.createFriendRequest(request);
        response = friendRequestService.findFriendRequestBySeq(response.requestSeq())
            .get().updateStatus(FriendRequestStatus.ACCEPTED);
        friendService.createFriend(response);

        UserGroup ug1 = UserGroup.create("그룹명1", u1);
        ug1 = userGroupRepository.save(ug1);
        GroupMember mem1 = GroupMember.create(ug1, u2);
        groupMemberRepository.saveAll(List.of(mem1));

        // when
        friendRequestService.deleteFriendAndRequest(users.get(0).getUserSeq(), users.get(1).getUserSeq());

        // then
        assertThat(friendRequestRepository.findAll()).hasSize(0);
        assertThat(friendRepository.findAll()).hasSize(0);
        assertThat(userGroupRepository.findAll()).hasSize(0);
        assertThat(groupMemberRepository.findAll()).hasSize(0);
    }

    @DisplayName("존재하지 않는 친구요청 기록을 삭제하면 예외가 발생한다. ")
    @Test
    void deleteFriendRequestError() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        // when // then
        assertThatThrownBy(() -> friendRequestService.deleteFriendAndRequest(users.get(0).getUserSeq(), users.get(1).getUserSeq()))
            .isInstanceOf(RestApiException.class)
            .hasMessage("FRIEND_REQUEST_NOT_FOUND");
    }

}
