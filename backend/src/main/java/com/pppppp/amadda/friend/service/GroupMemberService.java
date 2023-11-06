package com.pppppp.amadda.friend.service;

import com.pppppp.amadda.friend.dto.request.GroupCreateRequest;
import com.pppppp.amadda.friend.dto.request.GroupPatchRequest;
import com.pppppp.amadda.friend.entity.GroupMember;
import com.pppppp.amadda.friend.entity.UserGroup;
import com.pppppp.amadda.friend.repository.GroupMemberRepository;
import com.pppppp.amadda.friend.repository.UserGroupRepository;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.GroupErrorCode;
import com.pppppp.amadda.global.entity.exception.errorcode.UserErrorCode;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;

    public void isUserValid(List<Long> seqs) {
        seqs.forEach(seq -> {
            User u = findUserBySeq(seq);
        });
    }

    @Transactional
    public void createGroupMember(GroupCreateRequest request, Long groupSeq) {
        UserGroup ug = getGroup(groupSeq)
                .orElseThrow(() -> new RestApiException(GroupErrorCode.GROUP_NOT_FOUND));

        request.userSeqs().stream()
                .map(this::findUserBySeq)
                .map(u -> GroupMember.create(ug, u))
                .forEach(this::saveGroupMember);
    }

    @Transactional
    public void editGroup(GroupPatchRequest request) {

        UserGroup ug = getGroup(request.groupSeq())
                .orElseThrow(() -> new RestApiException(GroupErrorCode.GROUP_NOT_FOUND));
        List<GroupMember> members = getMembers(request.groupSeq());
        List<Long> newMems = request.userSeqs();

        // 그룹 이름 수정
        ug.updateGroupName(request.groupName());

        // 그룹 포함 인원 수정
        HashSet<Long> hs = new HashSet<>();
        List<GroupMember> toAdd = new ArrayList<>();
        members.stream().forEach(u -> hs.add(u.getMember().getUserSeq()));

        for (int i = 0; i < newMems.size(); i++) {
            Long mem = newMems.get(i);
            if(hs.contains(mem)) hs.remove(mem);
            else toAdd.add(
                    GroupMember.create(ug, findUserBySeq(mem))
            );
        }

        List<Long> toDel = new ArrayList<>(hs);
        for (int i = 0; i < toDel.size(); i++) deleteMember(request.groupSeq(), toDel.get(i));

        saveGroupMembers(toAdd);
    }


    // =============== 레포지토리에 직접 접근하는 메소드들 ===============

    private GroupMember saveGroupMember(GroupMember gm) {
        return groupMemberRepository.save(gm);
    }

    private List<GroupMember> saveGroupMembers(List<GroupMember> members) {
        return groupMemberRepository.saveAll(members);
    }

    private Optional<UserGroup> getGroup(Long seq) {
        return userGroupRepository.findById(seq);
    }

    private User findUserBySeq(Long seq) {
        return userRepository.findByUserSeq(seq)
                .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
    }

    private List<GroupMember> getMembers(Long seq) {
        return groupMemberRepository.findAllByGroup_GroupSeq(seq);
    }

    private void deleteMember(Long groupSeq, Long userSeq) {
        groupMemberRepository.deleteByGroup_GroupSeqAndMember_UserSeq(groupSeq, userSeq);
    }

}
