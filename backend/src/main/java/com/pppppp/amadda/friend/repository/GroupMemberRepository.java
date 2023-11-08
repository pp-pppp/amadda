package com.pppppp.amadda.friend.repository;

import com.pppppp.amadda.friend.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    List<GroupMember> findAllByGroup_GroupSeq(Long groupSeq);
    void deleteByGroup_GroupSeqAndMember_UserSeq(Long groupSeq, Long userSeq);
    @Query("Select m, u, g From GroupMember m Left Join m.member u Left Join m.group g " +
            "Where m.group.groupSeq IN :groupSeqs " +
            "And m.member.userName like concat('%', :searchKey, '%')")
    List<GroupMember> findByGroupSeqAndSearchKey(
            @Param("groupSeqs") List<Long> groupSeqs, @Param("searchKey") String searchKey);

}
