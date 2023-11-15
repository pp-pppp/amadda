package com.pppppp.amadda.friend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.alarm.entity.AlarmContent;
import com.pppppp.amadda.alarm.entity.AlarmType;
import com.pppppp.amadda.alarm.entity.FriendRequestAlarm;
import com.pppppp.amadda.alarm.repository.AlarmRepository;
import com.pppppp.amadda.alarm.repository.FriendRequestAlarmRepository;
import com.pppppp.amadda.alarm.service.AlarmService;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;

@EmbeddedKafka(
    partitions = 1,
    brokerProperties = {
        "listeners=PLAINTEXT://localhost:9094",
        "port=9094"
    }
)
class FriendRequestServiceTest extends IntegrationTestSupport {

    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    private FriendService friendService;

    @MockBean
    private AlarmService alarmService;

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

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private FriendRequestAlarmRepository friendRequestAlarmRepository;

    @AfterEach
    void tearDown() {
        friendRequestAlarmRepository.deleteAllInBatch();
        alarmRepository.deleteAllInBatch();
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
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User u2 = User.create("2222", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User user1 = users.get(0);
        User user2 = users.get(1);

        FriendRequestRequest request = FriendRequestRequest.builder()
            .targetSeq(user2.getUserSeq())
            .build();

        // when
        FriendRequestResponse response = friendRequestService.createFriendRequest(
            user1.getUserSeq(), request);

        //then
        assertThat(response).isNotNull();
        assertThat(response).extracting("ownerSeq", "friendSeq", "status")
            .containsExactlyInAnyOrder(user1.getUserSeq(), user2.getUserSeq(),
                "REQUESTED");

        verify(alarmService, times(1))
            .sendFriendRequest(user1.getUserSeq(), user2.getUserSeq());
    }

    @DisplayName("이미 친구인 유저에게 친구 신청을 하면 예외가 발생한다. ")
    @Test
    void sendAlreadyRequestedRequest() {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User u2 = User.create("2222", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User user1 = users.get(0);
        User user2 = users.get(1);

        FriendRequest fr = FriendRequest.create(u1, u2); // u1가 u2에게 신청
        friendRequestRepository.saveAll(List.of(fr));

        FriendRequestRequest request = FriendRequestRequest.builder()
            .targetSeq(user2.getUserSeq())
            .build();

        // when // then
        assertThatThrownBy(
            () -> friendRequestService.createFriendRequest(user1.getUserSeq(), request))
            .isInstanceOf(RestApiException.class)
            .hasMessage("FRIEND_REQUEST_INVALID");

        verify(alarmService, never())
            .sendFriendRequest(user1.getUserSeq(), user2.getUserSeq());
    }

    @DisplayName("이미 친구인 유저에게 친구 신청을 하면 예외가 발생한다. ")
    @Test
    void sendAlreadyFriendRequest() {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User u2 = User.create("2222", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        User user1 = users.get(0);
        User user2 = users.get(1);

        FriendRequest fr = FriendRequest.create(u1, u2); // u1가 u2에게 신청
        friendRequestRepository.saveAll(List.of(fr));

        List<FriendRequest> savedFr = friendRequestRepository.findAll();
        savedFr.get(0).updateStatus(FriendRequestStatus.ACCEPTED); // 수정

        FriendRequestRequest request = FriendRequestRequest.builder()
            .targetSeq(users.get(1).getUserSeq())
            .build();

        // when // then
        assertThatThrownBy(
            () -> friendRequestService.createFriendRequest(users.get(0).getUserSeq(), request))
            .isInstanceOf(RestApiException.class)
            .hasMessage("FRIEND_REQUEST_INVALID");

        verify(alarmService, never())
            .sendFriendRequest(user1.getUserSeq(), user2.getUserSeq());
    }

    @DisplayName("친구 요청을 받은 사람이 친구 요청을 거절하면 DECLINE 상태로 바뀐다. ")
    @Test
    void declineFriendRequest() {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User u2 = User.create("2222", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        User user1 = users.get(0);
        User user2 = users.get(1);

        FriendRequestRequest request = FriendRequestRequest.builder()
            .targetSeq(user2.getUserSeq())
            .build();

        FriendRequestResponse response = friendRequestService.createFriendRequest(
            user1.getUserSeq(), request);
        Long requestSeq = response.requestSeq();
        FriendRequest friendRequest = friendRequestRepository.findById(requestSeq).get();

        // when
        friendRequestService.declineFriendRequest(user2.getUserSeq(), requestSeq);

        // then
        assertThat(friendRequestRepository.findAll()).hasSize(0);

        verify(alarmService, times(1))
            .sendFriendRequest(user1.getUserSeq(), user2.getUserSeq());
        verify(alarmService, never())
            .sendFriendAccept(anyLong(), anyLong());
        verify(alarmService, times(1))
            .readFriendRequestAlarm(requestSeq);
    }

    @DisplayName("친구 요청을 한사람이 요청을 거절할 수는 없다. 예외가 발생한다. ")
    @Test
    void declineFriendRequestError() {
        // given
        User u1 = User.create("1111L", "유저1", "id1", "imageUrl1");
        User u2 = User.create("1234L", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        FriendRequestRequest request = FriendRequestRequest.builder()
            .targetSeq(users.get(1).getUserSeq())
            .build();

        FriendRequestResponse response = friendRequestService.createFriendRequest(
            users.get(0).getUserSeq(), request);
        Long requestSeq = response.requestSeq();

        // when // then
        assertThatThrownBy(
            () -> friendRequestService.declineFriendRequest(users.get(0).getUserSeq(), requestSeq))
            .isInstanceOf(RestApiException.class)
            .hasMessage("FRIEND_REQUEST_INVALID");
    }

    @DisplayName("친구 요청을 수락하면 ACCEPTED 상태로 바뀌고 친구 관계가 된다. ")
    @Test
    void acceptFriendRequest() {
        User u1 = User.create("1111L", "유저1", "id1", "imageUrl1");
        User u2 = User.create("1234L", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        User user1 = users.get(0);
        User user2 = users.get(1);
        FriendRequestRequest request = FriendRequestRequest.builder()
            .targetSeq(user2.getUserSeq())
            .build();
        FriendRequestResponse response = friendRequestService.createFriendRequest(
            user1.getUserSeq(), request);

        FriendRequest friendRequest = friendRequestRepository.findById(response.requestSeq()).get();

        FriendRequestAlarm a = FriendRequestAlarm.create(user2,
            AlarmContent.FRIEND_REQUEST.getMessage(user1.getUserName()),
            AlarmType.FRIEND_REQUEST, friendRequest);
        friendRequestAlarmRepository.save(a);

        FriendRequest fr = friendRequestService.findFriendRequestBySeq(response.requestSeq()).get();

        // when
        FriendRequestResponse res = friendRequestService.acceptFriendRequest(user2.getUserSeq(),
            fr.getRequestSeq());

        //then
        assertThat(res).isNotNull();
        assertThat(res).extracting("ownerSeq", "friendSeq", "status")
            .containsExactlyInAnyOrder(user1.getUserSeq(), user2.getUserSeq(), "ACCEPTED");

        // 추가로 친구 테이블에도 잘 저장되었는지 확인
        assertThat(
            List.of(
                FriendResponse.of(friendService.findFriendByOwnerAndFriend(u1, u2).get()),
                FriendResponse.of(friendService.findFriendByOwnerAndFriend(u2, u1).get())
            )
        ).hasSize(2)
            .extracting("ownerSeq", "friendSeq")
            .containsExactlyInAnyOrder(
                tuple(user1.getUserSeq(), user2.getUserSeq()),
                tuple(user2.getUserSeq(), user1.getUserSeq())
            );

        verify(alarmService, times(1))
            .sendFriendRequest(user1.getUserSeq(), user2.getUserSeq());
        verify(alarmService, times(1))
            .sendFriendAccept(user1.getUserSeq(), user2.getUserSeq());
        verify(alarmService, times(1))
            .readFriendRequestAlarm(fr.getRequestSeq());
    }

    @DisplayName("두 유저가 이미 친구라면 해당 요청에 대한 예외가 발생한다. ")
    @Test
    void alreadyFriends() {
        // given
        User u1 = User.create("1111L", "유저1", "id1", "imageUrl1");
        User u2 = User.create("1234L", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        User user1 = users.get(0);
        User user2 = users.get(1);

        FriendRequestRequest request = FriendRequestRequest.builder()
            .targetSeq(user2.getUserSeq())
            .build();
        FriendRequestResponse response = friendRequestService.createFriendRequest(
            user1.getUserSeq(), request);

        FriendRequest friendRequest = friendRequestRepository.findById(response.requestSeq()).get();

        FriendRequestAlarm a = FriendRequestAlarm.create(user2,
            AlarmContent.FRIEND_REQUEST.getMessage(user1.getUserName()),
            AlarmType.FRIEND_REQUEST, friendRequest);
        friendRequestAlarmRepository.save(a);

        FriendRequest fr = friendRequestService.findFriendRequestBySeq(response.requestSeq()).get();
        friendRequestService.acceptFriendRequest(user2.getUserSeq(), fr.getRequestSeq());

        // when // then
        assertThatThrownBy(
            () -> friendRequestService.acceptFriendRequest(user2.getUserSeq(),
                fr.getRequestSeq()))
            .isInstanceOf(RestApiException.class)
            .hasMessage("FRIEND_REQUEST_INVALID");
    }

    @DisplayName("친구요청 기록을 삭제한다. ")
    @Test
    void deleteFriendAndRequest() {
        // given
        User u1 = User.create("1111L", "유저1", "id1", "imageUrl1");
        User u2 = User.create("1234L", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        FriendRequestRequest request = FriendRequestRequest.builder()
            .targetSeq(users.get(1).getUserSeq())
            .build();
        FriendRequestResponse response = friendRequestService.createFriendRequest(
            users.get(0).getUserSeq(), request);
        response = friendRequestService.findFriendRequestBySeq(response.requestSeq())
            .get().updateStatus(FriendRequestStatus.ACCEPTED);
        friendService.createFriend(response);

        UserGroup ug1 = UserGroup.create("그룹명1", u1);
        ug1 = userGroupRepository.save(ug1);
        GroupMember mem1 = GroupMember.create(ug1, u2);
        groupMemberRepository.saveAll(List.of(mem1));

        // when
        friendRequestService.deleteFriendAndRequest(users.get(0).getUserSeq(),
            users.get(1).getUserSeq());

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
        User u1 = User.create("1111L", "유저1", "id1", "imageUrl1");
        User u2 = User.create("1234L", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        // when // then
        assertThatThrownBy(
            () -> friendRequestService.deleteFriendAndRequest(users.get(0).getUserSeq(),
                users.get(1).getUserSeq()))
            .isInstanceOf(RestApiException.class)
            .hasMessage("FRIEND_REQUEST_NOT_FOUND");
    }

}
