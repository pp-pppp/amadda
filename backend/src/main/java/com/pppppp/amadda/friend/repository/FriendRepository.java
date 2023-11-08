package com.pppppp.amadda.friend.repository;

import com.pppppp.amadda.friend.entity.Friend;
import com.pppppp.amadda.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    Optional<Friend> findByOwnerAndFriend(User u1, User u2);
    @Query("Select f, u From Friend f Left Join f.friend u " +
            "Where f.owner.userSeq = :userSeq And f.friend.userName like concat('%', :searchKey, '%')")
    List<Friend> findByOwnerSeqAndSearchKey(@Param("userSeq") Long userSeq, @Param("searchKey") String searchKey);

}
