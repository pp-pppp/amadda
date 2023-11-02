package com.pppppp.amadda.friend.repository;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.friend.entity.UserGroup;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserGroupRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private UserRepository userRepository;
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
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1", false);
        List<User> users = userRepository.saveAll(List.of(u1));
        UserGroup ug = UserGroup.create("그룹명1", u1);

        // when
        ug = userGroupRepository.save(ug);

        // then
        assertThat(ug)
                .extracting("groupName", "owner")
                .containsExactly("그룹명1", u1);
    }

}