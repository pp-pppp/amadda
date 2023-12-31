package com.pppppp.amadda.friend.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.friend.entity.GroupMember;
import com.pppppp.amadda.friend.entity.UserGroup;
import com.pppppp.amadda.user.entity.User;
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
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User u2 = User.create("2222", "유저2", "id2", "imageUrl2");
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
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User u2 = User.create("2222", "유저2", "id2", "imageUrl2");
        User u3 = User.create("3333", "유저3", "id3", "imageUrl3");

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
                tuple(groupSeq, users.get(1).getUserSeq()),
                tuple(groupSeq, users.get(2).getUserSeq())
            );
    }

    @DisplayName("그룹 seq와 멤버의 유저 seq로 groupmember를 삭제한다. ")
    @Test
    @Transactional
    void deleteByGroup_GroupSeqAndMember_UserSeq() {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User u2 = User.create("2222", "유저2", "id2", "imageUrl2");
        User u3 = User.create("3333", "유저3", "id3", "imageUrl3");

        List<User> users = userRepository.saveAll(List.of(u1, u2, u3));
        UserGroup ug = UserGroup.create("그룹명1", u1);
        Long groupSeq = userGroupRepository.save(ug).getGroupSeq();

        GroupMember gm1 = GroupMember.create(ug, u2);
        GroupMember gm2 = GroupMember.create(ug, u3);
        groupMemberRepository.saveAll(List.of(gm1, gm2));

        // when
        groupMemberRepository.deleteByGroup_GroupSeqAndMember_UserSeq(groupSeq,
            users.get(1).getUserSeq());

        // then
        assertThat(groupMemberRepository.findAll())
            .extracting("group.groupSeq", "member.userSeq")
            .containsExactly(
                tuple(groupSeq, users.get(2).getUserSeq())
            );
    }

    @DisplayName("그룹 seq와 검색키로 해당 멤버유저를 조회할 수 있다. ")
    @Test
    void findByGroupSeqAndSearchKey() {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User u2 = User.create("2222", "유저2", "id2", "imageUrl2");
        User u3 = User.create("3333", "유저3", "id3", "imageUrl3");

        List<User> users = userRepository.saveAll(List.of(u1, u2, u3));
        UserGroup ug = UserGroup.create("그룹명1", u1);
        Long groupSeq = userGroupRepository.save(ug).getGroupSeq();

        GroupMember gm1 = GroupMember.create(ug, u2);
        GroupMember gm2 = GroupMember.create(ug, u3);
        groupMemberRepository.saveAll(List.of(gm1, gm2));

        // when
        List<GroupMember> members = groupMemberRepository.findByGroupSeqsAndSearchKey(
            List.of(ug.getGroupSeq()), "유저3");

        // then
        assertThat(members)
            .extracting("group.groupName", "member.userSeq", "member.userName")
            .containsExactlyInAnyOrder(
                tuple("그룹명1", users.get(2).getUserSeq(), "유저3")
            );
    }

    @DisplayName("여러 그룹 seq와 검색키로 해당 멤버유저를 조회할 수 있다. ")
    @Test
    void findByGroupSeqsAndSearchKey() {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User u2 = User.create("2222", "유저2", "id2", "imageUrl2");
        User u3 = User.create("3333", "유저3", "id3", "imageUrl3");
        User u4 = User.create("4444", "유저4", "id4", "imageUrl4");
        User u5 = User.create("5555", "유저5", "id5", "imageUrl5");
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
        List<GroupMember> members = groupMemberRepository.findByGroupSeqsAndSearchKey(
            List.of(ug1.getGroupSeq(), ug2.getGroupSeq()), "유저");

        // then
        assertThat(members)
            .extracting("group.groupName", "member.userSeq", "member.userName")
            .containsExactlyInAnyOrder(
                tuple("그룹명1", users.get(1).getUserSeq(), "유저2"),
                tuple("그룹명1", users.get(2).getUserSeq(), "유저3"),
                tuple("그룹명1", users.get(3).getUserSeq(), "유저4"),
                tuple("그룹명2", users.get(3).getUserSeq(), "유저4"),
                tuple("그룹명2", users.get(4).getUserSeq(), "유저5")
            );
    }

    @DisplayName("그룹 주인과 멤버로 그룹 멤버를 삭제한다. ")
    @Test
    @Transactional
    void deleteByGroup_OwnerAndMember() {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User u2 = User.create("2222", "유저2", "id2", "imageUrl2");
        User u3 = User.create("3333", "유저3", "id3", "imageUrl3");
        User u4 = User.create("4444", "유저4", "id4", "imageUrl4");
        User u5 = User.create("5555", "유저5", "id5", "imageUrl5");
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
        groupMemberRepository.deleteByGroup_OwnerAndMember(u1, u4);

        // then
        assertThat(groupMemberRepository.findAll())
            .extracting("group.groupName", "member.userSeq", "member.userName")
            .containsExactlyInAnyOrder(
                tuple("그룹명1", users.get(1).getUserSeq(), "유저2"),
                tuple("그룹명1", users.get(2).getUserSeq(), "유저3"),
                tuple("그룹명2", users.get(4).getUserSeq(), "유저5")
            );
    }

}
