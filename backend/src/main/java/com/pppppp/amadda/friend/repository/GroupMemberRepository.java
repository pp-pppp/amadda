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

    @Query("""
        select m, u, g from GroupMember m left join m.member u left join m.group g
        where m.group.groupSeq in :groupSeqs
        and m.member.userName like concat('%', :searchKey, '%')
        """)
    List<GroupMember> findByGroupSeqsAndSearchKey(
        @Param("groupSeqs") List<Long> groupSeqs, @Param("searchKey") String searchKey);

    void deleteByGroup_OwnerAndMember(User owner, User member);

}
