package com.pppppp.amadda.friend.service;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.friend.dto.request.GroupCreateRequest;
import com.pppppp.amadda.friend.dto.request.GroupUpdateRequest;
import com.pppppp.amadda.friend.repository.GroupMemberRepository;
import com.pppppp.amadda.friend.repository.UserGroupRepository;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;


class GroupMemberServiceTest extends IntegrationTestSupport {

    @Autowired
    private GroupMemberService groupMemberService;
    @Autowired
    private UserGroupService userGroupService;
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
        userRepository.deleteAllInBatch();
    }

    @DisplayName("그룹멤버가 모두 정상 저장된다. ")
    @Test
    @Transactional
    void createGroupMemberjp() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        User u3 = User.create(2222L, "유저3", "id3", "imageUrl3");
        List<User> users = userRepository.saveAll(List.of(u1, u2, u3));

        GroupCreateRequest gcr = GroupCreateRequest.builder()
                .ownerSeq(1111L)
                .groupName("그룹명1")
                .userSeqs(List.of(1234L, 2222L))
                .build();
        Long groupSeq = userGroupService.createUserGroup(gcr);

        // when
        groupMemberService.createGroupMember(gcr, groupSeq);

        // then
        assertThat(groupMemberRepository.findAll()).hasSize(2)
                .extracting("member.userSeq")
                .containsExactlyInAnyOrder(
                        1234L, 2222L
                );
    }

    @DisplayName("그룹 만들 모든 유저들이 유효 유저들이다. ")
    @Test
    void isUserValid() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        User u3 = User.create(2222L, "유저3", "id3", "imageUrl3");
        List<User> users = userRepository.saveAll(List.of(u1, u2, u3));

        GroupCreateRequest gcr = GroupCreateRequest.builder()
                .ownerSeq(1111L)
                .groupName("그룹명1")
                .userSeqs(List.of(1234L, 2222L))
                .build();

        // when // then
        groupMemberService.isUserValid(gcr.userSeqs());

    }

    @DisplayName("그룹 만들 유자가 유효 유저가 아니면 예외처리. ")
    @Test
    void userNotValid() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        User u3 = User.create(2222L, "유저3", "id3", "imageUrl3");
        List<User> users = userRepository.saveAll(List.of(u1, u2, u3));

        GroupCreateRequest gcr = GroupCreateRequest.builder()
                .ownerSeq(1111L)
                .groupName("그룹명1")
                .userSeqs(List.of(3333L, 2222L))
                .build();

        // when // then
        assertThatThrownBy(() -> groupMemberService.isUserValid(gcr.userSeqs()))
                .isInstanceOf(RestApiException.class)
                .hasMessage("USER_NOT_FOUND");
    }

    @DisplayName("유저그룹이 존재하지 않는 그룹멤버를 만들면 예외처리. ")
    @Test
    void userGroupNotFound() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        User u3 = User.create(2222L, "유저3", "id3", "imageUrl3");
        List<User> users = userRepository.saveAll(List.of(u1, u2, u3));

        GroupCreateRequest gcr = GroupCreateRequest.builder()
                .ownerSeq(1111L)
                .groupName("그룹명1")
                .userSeqs(List.of(1234L, 2222L))
                .build();

        // when // then
        assertThatThrownBy(() -> groupMemberService.createGroupMember(gcr, 0L))
                .isInstanceOf(RestApiException.class)
                .hasMessage("GROUP_NOT_FOUND");
    }

    @DisplayName("그룹의 이름과 멤버를 수정한다. ")
    @Test
    void editGroup() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        User u3 = User.create(2222L, "유저3", "id3", "imageUrl3");
        User u4 = User.create(3456L, "유저4", "id4", "imageUrl4");
        List<User> users = userRepository.saveAll(List.of(u1, u2, u3, u4));

        GroupCreateRequest gcr = GroupCreateRequest.builder()
                .ownerSeq(1111L)
                .groupName("그룹명1")
                .userSeqs(List.of(1234L, 2222L))
                .build();
        Long groupSeq = userGroupService.createUserGroup(gcr);
        groupMemberService.createGroupMember(gcr, groupSeq);

        GroupUpdateRequest gpr = GroupUpdateRequest.builder()
                .groupSeq(groupSeq)
                .groupName("새로운 이름")
                .userSeqs(List.of(2222L, 3456L))
                .build();

        // when
        groupMemberService.editGroup(gpr);

        // then
        assertThat(groupMemberRepository.findAll()).hasSize(2)
                .extracting("group.groupSeq", "member.userSeq")
                .containsExactly(
                        tuple(groupSeq, 2222L),
                        tuple(groupSeq, 3456L)
                );
        assertThat(userGroupRepository.findAll()).hasSize(1)
                .extracting("groupName", "owner.userSeq")
                .containsExactly(
                        tuple("새로운 이름", 1111L)
                );
    }

}