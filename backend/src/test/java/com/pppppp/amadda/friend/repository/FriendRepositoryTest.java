package com.pppppp.amadda.friend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.friend.entity.Friend;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User u2 = User.create("2222", "유저2", "id2", "imageUrl2");
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
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User u2 = User.create("2222", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        Friend f = Friend.create(u1, u2);
        friendRepository.save(f);

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
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User u2 = User.create("2222", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        Friend f = Friend.create(u1, u2);
        f = friendRepository.save(f);

        // when
        friendRepository.deleteById(f.getRelationSeq());

        // then
        assertThat(friendRepository.findAll())
            .hasSize(0);
    }

    @DisplayName("내 유저 seq와 검색키로 해당 유저들을 조회할 수 있다. ")
    @Test
    void findByOwnerSeqAndSearchKey() {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User user1 = userRepository.save(u1);
        User u2 = User.create("2222", "유저2", "id2", "imageUrl2");
        User user2 = userRepository.save(u2);
        User u3 = User.create("3333", "유저3", "id3", "imageUrl3");
        User user3 = userRepository.save(u3);

        Friend f1 = Friend.create(user1, user2);
        Friend f2 = Friend.create(user1, user3);
        friendRepository.saveAll(List.of(f1, f2));

        // when
        List<Friend> friends = friendRepository.findByOwnerSeqAndSearchKey(user1.getUserSeq(),
            "유저2");

        // then
        assertThat(friends)
            .extracting("friend.userName")
            .containsExactlyInAnyOrder("유저2");
    }

}
