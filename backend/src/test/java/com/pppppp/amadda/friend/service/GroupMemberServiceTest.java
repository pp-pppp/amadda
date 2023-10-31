package com.pppppp.amadda.friend.service;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.friend.dto.request.GroupCreateRequest;
import com.pppppp.amadda.friend.repository.GroupMemberRepository;
import com.pppppp.amadda.friend.repository.UserGroupRepository;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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

    @AfterEach
    void tearDown() {

    }

    @DisplayName("그룹멤버가 모두 정상 저장된다. ")
    @Test
    @Transactional
    void createGroupMemberjp() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1", false);
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2", true);
        User u3 = User.create(2222L, "유저3", "id3", "imageUrl3", true);
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
                .extracting("group.groupSeq", "member.userSeq")
                .containsExactlyInAnyOrder(
                        tuple(1L, 1234L),
                        tuple(1L, 2222L)
                );
    }

}