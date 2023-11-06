package com.pppppp.amadda.friend.repository;

import com.pppppp.amadda.friend.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    List<GroupMember> findAllByGroup_GroupSeq(Long groupSeq);
    void deleteByGroup_GroupSeqAndMember_UserSeq(Long groupSeq, Long userSeq);
}
