package com.pppppp.amadda.friend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.friend.dto.request.GroupCreateRequest;
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
import org.springframework.transaction.annotation.Transactional;

class UserGroupServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private GroupMemberService groupMemberService;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @AfterEach
    void tearDown() {
        groupMemberRepository.deleteAllInBatch();
        userGroupRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("유저 그룹을 생성한다. ")
    @Test
    @Transactional
    void createGroup() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        User u3 = User.create(2222L, "유저3", "id3", "imageUrl3");
        List<User> users = userRepository.saveAll(List.of(u1, u2, u3));

        GroupCreateRequest gcr = GroupCreateRequest.builder()
            .groupName("그룹명1")
            .userSeqs(List.of(users.get(1).getUserSeq(), users.get(2).getUserSeq()))
            .build();

        // when
        Long groupSeq = userGroupService.createUserGroup(users.get(0).getUserSeq(), gcr);

        // then
        assertThat(userGroupRepository.findAll()).hasSize(1)
            .extracting("groupName", "owner")
            .containsExactlyInAnyOrder(
                tuple("그룹명1", users.get(0))
            );
    }

    @DisplayName("그룹과 해당 멤버들을 삭제한다. ")
    @Test
    void deleteNotExistingGroup() {
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        User u3 = User.create(2222L, "유저3", "id3", "imageUrl3");
        List<User> users = userRepository.saveAll(List.of(u1, u2, u3));

        GroupCreateRequest gcr = GroupCreateRequest.builder()
            .groupName("그룹명1")
            .userSeqs(List.of(users.get(1).getUserSeq(), users.get(2).getUserSeq()))
            .build();

        Long groupSeq = userGroupService.createUserGroup(users.get(0).getUserSeq(), gcr);
        groupMemberService.createGroupMember(gcr, groupSeq);

        // when
        userGroupService.deleteGroupAndMembers(groupSeq);

        // then
        assertThat(userGroupRepository.findAll()).hasSize(0);
        assertThat(groupMemberRepository.findAll()).hasSize(0);
    }

    @DisplayName("존재하지 않는 그룹을 삭제하면 예외가 발생한다. ")
    @Test
    void deleteGroupAndMembers() {
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        User u3 = User.create(2222L, "유저3", "id3", "imageUrl3");
        List<User> users = userRepository.saveAll(List.of(u1, u2, u3));

        GroupCreateRequest gcr = GroupCreateRequest.builder()
            .groupName("그룹명1")
            .userSeqs(List.of(users.get(1).getUserSeq(), users.get(2).getUserSeq()))
            .build();

        Long groupSeq = userGroupService.createUserGroup(users.get(0).getUserSeq(), gcr);
        groupMemberService.createGroupMember(gcr, groupSeq);

        // when // then
        assertThatThrownBy(() -> userGroupService.deleteGroupAndMembers(0L))
            .isInstanceOf(RestApiException.class)
            .hasMessage("GROUP_NOT_FOUND");
    }

}
