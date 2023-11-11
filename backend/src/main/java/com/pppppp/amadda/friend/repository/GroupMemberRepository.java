package com.pppppp.amadda.friend.repository;

import com.pppppp.amadda.friend.entity.GroupMember;
import com.pppppp.amadda.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    List<GroupMember> findAllByGroup_GroupSeq(Long groupSeq);

    void deleteByGroup_GroupSeqAndMember_UserSeq(Long groupSeq, Long userSeq);

    @Query("Select m, u, g From GroupMember m Left Join m.member u Left Join m.group g " +
        "Where m.group.groupSeq IN :groupSeqs " +
        "And m.member.userName like concat('%', :searchKey, '%')")
    List<GroupMember> findByGroupSeqsAndSearchKey(
        @Param("groupSeqs") List<Long> groupSeqs, @Param("searchKey") String searchKey);

    void deleteByGroup_OwnerAndMember(User owner, User member);

}
