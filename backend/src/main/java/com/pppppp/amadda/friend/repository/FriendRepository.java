package com.pppppp.amadda.friend.repository;

import com.pppppp.amadda.friend.entity.Friend;
import com.pppppp.amadda.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    Optional<Friend> findByOwnerAndFriend(User u1, User u2);

}
