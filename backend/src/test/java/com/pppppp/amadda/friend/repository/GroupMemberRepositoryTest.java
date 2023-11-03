package com.pppppp.amadda.friend.repository;

import static org.assertj.core.api.Assertions.assertThat;

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

}