package com.pppppp.amadda.friend.repository;

import com.pppppp.amadda.friend.entity.FriendRequest;
import com.pppppp.amadda.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    Optional<FriendRequest> findByOwnerAndFriend(User owner, User friend);

}
