package com.pppppp.amadda.friend.entity;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.friend.dto.request.GroupCreateRequest;
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
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

class UserGroupTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;

    @AfterEach
    void tearDown() {
        userGroupRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("그룹의 이름을 변경하고 조회한다. ")
    @Test
    @Transactional
    void updateGroupName() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1");
        List<User> users = userRepository.saveAll(List.of(u1));

        UserGroup group = UserGroup.create("group1", u1);
        userGroupRepository.save(group);

        // when
        userGroupRepository.findAll().get(0).updateGroupName("aaa");

        // then
        assertThat(userGroupRepository.findAll()).hasSize(1)
                .extracting("groupName", "owner.userSeq")
                .containsExactlyInAnyOrder(
                        tuple("aaa", 1111L)
                );
    }

}