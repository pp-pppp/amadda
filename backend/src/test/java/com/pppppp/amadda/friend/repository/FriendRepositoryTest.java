package com.pppppp.amadda.friend.repository;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.friend.entity.Friend;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FriendRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        friendRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("친구를 추가하고 조회한다. ")
    @Test
    void createFriend() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1", false);
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2", true);
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        Friend f = Friend.create(u1, u2);

        // when
        f = friendRepository.save(f);

        //then
        assertThat(f)
                .extracting("owner", "friend")
                .containsExactly(u1, u2);
    }

    @DisplayName("주인 유저와 친구 유저로 친구관계를 조회할 수 있다. ")
    @Test
    @Transactional
    void findFriendByOwnerAndFriend() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1", false);
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2", true);
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        Friend f = Friend.create(u1, u2);
        f = friendRepository.save(f);

        // when
        Friend friend = friendRepository.findByOwnerAndFriend(u1, u2).get();

        // then
        assertThat(friend)
                .extracting("owner", "friend")
                .containsExactly(u1, u2);
    }

    @DisplayName("친구를 삭제할 수 있다. ")
    @Test
    void deleteFriend() {
        // given
        User u1 = User.create(1111L, "유저1", "id1", "imageUrl1", false);
        User u2 = User.create(1234L, "유저2", "id2", "imageUrl2", true);
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        Friend f = Friend.create(u1, u2);
        f = friendRepository.save(f);

        // when
        friendRepository.deleteById(f.getRelationSeq());

        // then
        assertThat(friendRepository.findAll())
                .hasSize(0);
    }


}