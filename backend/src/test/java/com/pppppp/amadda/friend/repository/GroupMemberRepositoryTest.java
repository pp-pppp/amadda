package com.pppppp.amadda.friend.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.friend.entity.GroupMember;
import com.pppppp.amadda.friend.entity.UserGroup;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class GroupMemberRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        groupMemberRepository.deleteAllInBatch();
        userGroupRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("그룹멤버 저장하고 조회한다. ")
    @Test
    void createGroupMember() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));
        UserGroup ug = UserGroup.create("그룹명1", u1);
        ug = userGroupRepository.save(ug);

        GroupMember gm = GroupMember.create(ug, u2);

        // when
        gm = groupMemberRepository.save(gm);

        // then
        assertThat(gm)
            .extracting("group", "member")
            .containsExactly(ug, u2);
    }

    @DisplayName("그룹 seq로 모든 그룹을 조회한다. ")
    @Test
    void findAllByGroup_GroupSeq() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        User u3 = User.create(2222L, "유저3", "id3", "imageUrl3");

        List<User> users = userRepository.saveAll(List.of(u1, u2, u3));
        UserGroup ug = UserGroup.create("그룹명1", u1);
        Long groupSeq = userGroupRepository.save(ug).getGroupSeq();

        GroupMember gm1 = GroupMember.create(ug, u2);
        GroupMember gm2 = GroupMember.create(ug, u3);
        groupMemberRepository.saveAll(List.of(gm1, gm2));

        // when
        List<GroupMember> members =
                groupMemberRepository.findAllByGroup_GroupSeq(groupSeq);

        // then
        assertThat(members)
                .extracting("group.groupSeq", "member.userSeq")
                .containsExactly(
                        tuple(groupSeq, 1234L),
                        tuple(groupSeq, 2222L)
                );
    }

    @DisplayName("그룹 seq와 멤버의 유저 seq로 groupmember를 삭제한다. ")
    @Test
    @Transactional
    void deleteByGroup_GroupSeqAndMember_UserSeq() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2");
        User u3 = User.create(2222L, "유저3", "id3", "imageUrl3");

        List<User> users = userRepository.saveAll(List.of(u1, u2, u3));
        UserGroup ug = UserGroup.create("그룹명1", u1);
        Long groupSeq = userGroupRepository.save(ug).getGroupSeq();

        GroupMember gm1 = GroupMember.create(ug, u2);
        GroupMember gm2 = GroupMember.create(ug, u3);
        groupMemberRepository.saveAll(List.of(gm1, gm2));

        // when
        groupMemberRepository.deleteByGroup_GroupSeqAndMember_UserSeq(groupSeq, 1234L);

        // then
        assertThat(groupMemberRepository.findAll())
                .extracting("group.groupSeq", "member.userSeq")
                .containsExactly(
                        tuple(groupSeq, 2222L)
                );
    }

}
