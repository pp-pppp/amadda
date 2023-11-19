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

class UserGroupRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @AfterEach
    void tearDown() {
        groupMemberRepository.deleteAllInBatch();
        userGroupRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("유저그룹을 저장하고 조회한다. ")
    @Test
    void createUserGroup() {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        List<User> users = userRepository.saveAll(List.of(u1));
        UserGroup ug = UserGroup.create("그룹명1", u1);

        // when
        ug = userGroupRepository.save(ug);

        // then
        assertThat(ug)
            .extracting("groupName", "owner")
            .containsExactly("그룹명1", u1);
    }

    @DisplayName("리스트와 서치키로 해당 멤버들만 조회한다. ")
    @Test
    void findByGroupSeqAndSearchKey() {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User u2 = User.create("2222", "유저2", "id2", "imageUrl2");
        User u3 = User.create("3333", "유저3", "id3", "imageUrl3");
        List<User> users = userRepository.saveAll(List.of(u1, u2, u3));

        UserGroup ug = UserGroup.create("그룹명1", u1);
        ug = userGroupRepository.save(ug);

        GroupMember mem1 = GroupMember.create(ug, u2);
        GroupMember mem2 = GroupMember.create(ug, u3);
        groupMemberRepository.saveAll(List.of(mem1, mem2));

        // when
        List<GroupMember> members = groupMemberRepository.findByGroupSeqsAndSearchKey(
            List.of(ug.getGroupSeq()),
            "유저"
        );

        // then
        assertThat(members)
            .extracting("member.userSeq", "member.userName")
            .containsExactlyInAnyOrder(
                tuple(users.get(1).getUserSeq(), "유저2"),
                tuple(users.get(2).getUserSeq(), "유저3")
            );
    }

    @DisplayName("리스트와 빈 서치키로 해당 멤버들만 조회한다. ")
    @Test
    void findByGroupSeqAndNoSearchKey() {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User u2 = User.create("2222", "유저2", "id2", "imageUrl2");
        User u3 = User.create("3333", "유저3", "id3", "imageUrl3");
        List<User> users = userRepository.saveAll(List.of(u1, u2, u3));

        UserGroup ug = UserGroup.create("그룹명1", u1);
        ug = userGroupRepository.save(ug);

        GroupMember mem1 = GroupMember.create(ug, u2);
        GroupMember mem2 = GroupMember.create(ug, u3);
        groupMemberRepository.saveAll(List.of(mem1, mem2));

        // when
        List<GroupMember> members = groupMemberRepository.findByGroupSeqsAndSearchKey(
            List.of(ug.getGroupSeq()), "");

        // then
        assertThat(members)
            .extracting("member.userSeq", "member.userName")
            .containsExactlyInAnyOrder(
                tuple(users.get(1).getUserSeq(), "유저2"),
                tuple(users.get(2).getUserSeq(), "유저3")
            );

    }

}
