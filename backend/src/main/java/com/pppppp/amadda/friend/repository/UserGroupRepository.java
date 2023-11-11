package com.pppppp.amadda.friend.repository;

import com.pppppp.amadda.friend.entity.UserGroup;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    List<UserGroup> findByOwner_UserSeq(Long userSeq);
}

