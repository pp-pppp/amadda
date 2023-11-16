package com.pppppp.amadda.friend.repository;

import com.pppppp.amadda.friend.entity.Friend;
import com.pppppp.amadda.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    Optional<Friend> findByOwnerAndFriend(User u1, User u2);

    @Query("""
        select f, u from Friend f left join f.friend u
        where f.owner.userSeq = :ownerSeq
        and f.friend.userName like concat('%', :searchKey, '%')
        """)
    List<Friend> findByOwnerSeqAndSearchKey(@Param("ownerSeq") Long userSeq,
        @Param("searchKey") String searchKey);

}
