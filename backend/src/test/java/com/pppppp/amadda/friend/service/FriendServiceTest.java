package com.pppppp.amadda.friend.service;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.alarm.repository.AlarmRepository;
import com.pppppp.amadda.friend.dto.request.FriendRequestRequest;
import com.pppppp.amadda.friend.dto.response.FriendReadResponse;
import com.pppppp.amadda.friend.dto.response.FriendRequestResponse;
import com.pppppp.amadda.friend.dto.response.FriendResponse;
import com.pppppp.amadda.friend.dto.response.GroupResponse;
import com.pppppp.amadda.friend.entity.*;
import com.pppppp.amadda.friend.repository.FriendRepository;
import com.pppppp.amadda.friend.repository.FriendRequestRepository;
import com.pppppp.amadda.friend.repository.GroupMemberRepository;
import com.pppppp.amadda.friend.repository.UserGroupRepository;
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
    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private AlarmRepository alarmRepository;

    @AfterEach
    void tearDown() {
        alarmRepository.deleteAllInBatch();
        groupMemberRepository.deleteAllInBatch();
        userGroupRepository.deleteAllInBatch();
        friendRepository.deleteAllInBatch();
        friendRequestRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("두 유저가 서로 친구관계가 된다. ")
    @Test
    void beFriends() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
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
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
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
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
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
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
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
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        FriendRequest fr = FriendRequest.create(u1, u2);
        fr.updateStatus(FriendRequestStatus.ACCEPTED);
        friendService.createFriend(FriendRequestResponse.of(fr));

        // when
        friendService.deleteFriends(u1, u2);

        // then
        assertThat(friendRepository.findAll()).hasSize(0);
    }

    @DisplayName("리스트와 서치키로 해당 멤버들만 조회한다. ")
    @Test
    void searchInGroups() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        User u3 = User.create(2222L, "유저3", "id3", "imageUrl3");
        User u4 = User.create(9999L, "유자4", "id4", "imageUrl4");
        User u5 = User.create(3456L, "유저5", "id5", "imageUrl5");

        List<User> users = userRepository.saveAll(List.of(u1, u2, u3, u4, u5));

        UserGroup ug1 = UserGroup.create("그룹명1", u1);
        ug1 = userGroupRepository.save(ug1);

        GroupMember mem1 = GroupMember.create(ug1, u2);
        GroupMember mem2 = GroupMember.create(ug1, u3);
        GroupMember mem3 = GroupMember.create(ug1, u4);
        groupMemberRepository.saveAll(List.of(mem1, mem2, mem3));

        UserGroup ug2 = UserGroup.create("그룹명2", u1);
        ug2 = userGroupRepository.save(ug2);

        GroupMember mem4 = GroupMember.create(ug2, u4);
        GroupMember mem5 = GroupMember.create(ug2, u5);
        groupMemberRepository.saveAll(List.of(mem4, mem5));

        // when
        List<GroupResponse> mems = friendService.searchInGroups(1111L, "유저");

        // then
        assertThat(mems).hasSize(2)
                .extracting("groupSeq", "groupName")
                .containsExactlyInAnyOrder(
                        tuple(ug1.getGroupSeq(), "그룹명1"),
                        tuple(ug2.getGroupSeq(), "그룹명2")
                );
        assertThat(mems.get(0).members()).hasSize(2)
                .extracting("userSeq", "userName", "userId", "imageUrl")
                .containsExactlyInAnyOrder(
                        tuple(1234L, "유저2", "id2", "imageUrl2"),
                        tuple(2222L, "유저3", "id3", "imageUrl3")
                );
        assertThat(mems.get(1).members()).hasSize(1)
                .extracting("userSeq", "userName", "userId", "imageUrl")
                .containsExactlyInAnyOrder(
                        tuple(3456L, "유저5", "id5", "imageUrl5")
                );
    }

    @DisplayName("내 유저 seq와 검색키로 친구와 그룹을 검색한다. ")
    @Test
    void searchFriends() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        User u3 = User.create(2222L, "유저3", "id3", "imageUrl3");
        User u4 = User.create(9999L, "유자4", "id4", "imageUrl4");
        User u5 = User.create(3456L, "유저5", "id5", "imageUrl5");

        List<User> users = userRepository.saveAll(List.of(u1, u2, u3, u4, u5));

        Friend f1 = Friend.create(u1, u2);
        Friend f2 = Friend.create(u1, u3);
        Friend f3 = Friend.create(u1, u4);
        Friend f4 = Friend.create(u1, u5);
        friendRepository.saveAll(List.of(f1, f2, f3, f4));

        UserGroup ug1 = UserGroup.create("그룹명1", u1);
        ug1 = userGroupRepository.save(ug1);
        GroupMember mem1 = GroupMember.create(ug1, u2);
        GroupMember mem2 = GroupMember.create(ug1, u3);
        GroupMember mem3 = GroupMember.create(ug1, u4);
        groupMemberRepository.saveAll(List.of(mem1, mem2, mem3));

        UserGroup ug2 = UserGroup.create("그룹명2", u1);
        ug2 = userGroupRepository.save(ug2);
        GroupMember mem4 = GroupMember.create(ug2, u4);
        GroupMember mem5 = GroupMember.create(ug2, u5);
        groupMemberRepository.saveAll(List.of(mem4, mem5));

        // when
        FriendReadResponse response = friendService.searchFriends(1111L, "유저");

        // then
        assertThat(response.groups()).hasSize(2)
                .extracting("groupSeq", "groupName")
                .containsExactlyInAnyOrder(
                        tuple(ug1.getGroupSeq(), "그룹명1"),
                        tuple(ug2.getGroupSeq(), "그룹명2")
                );
        assertThat(response.groups().get(0).members()).hasSize(2)
                .extracting("userSeq", "userName", "userId", "imageUrl")
                .containsExactlyInAnyOrder(
                        tuple(1234L, "유저2", "id2", "imageUrl2"),
                        tuple(2222L, "유저3", "id3", "imageUrl3")
                );
        assertThat(response.groups().get(1).members()).hasSize(1)
                .extracting("userSeq", "userName", "userId", "imageUrl")
                .containsExactlyInAnyOrder(
                        tuple(3456L, "유저5", "id5", "imageUrl5")
                );
        assertThat(response.members()).hasSize(3)
                .extracting("userSeq", "userName", "userId", "imageUrl")
                .containsExactlyInAnyOrder(
                        tuple(1234L, "유저2", "id2", "imageUrl2"),
                        tuple(2222L, "유저3", "id3", "imageUrl3"),
                        tuple(3456L, "유저5", "id5", "imageUrl5")
                );
    }

}
