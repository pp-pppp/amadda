package com.pppppp.amadda.friend.service;

import com.pppppp.amadda.friend.dto.request.GroupCreateRequest;
import com.pppppp.amadda.friend.dto.request.GroupUpdateRequest;
import com.pppppp.amadda.friend.entity.GroupMember;
import com.pppppp.amadda.friend.entity.UserGroup;
import com.pppppp.amadda.friend.repository.GroupMemberRepository;
import com.pppppp.amadda.friend.repository.UserGroupRepository;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.GroupErrorCode;
import com.pppppp.amadda.global.entity.exception.errorcode.UserErrorCode;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void editGroup(GroupUpdateRequest request) {

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

        for (Long mem : newMems) {
            if (hs.contains(mem)) {
                hs.remove(mem);
            } else {
                toAdd.add(
                    GroupMember.create(ug, findUserBySeq(mem))
                );
            }
        }

        List<Long> toDel = new ArrayList<>(hs);
        for (Long aLong : toDel) {
            deleteMember(request.groupSeq(), aLong);
        }

        saveGroupMembers(toAdd);
    }

    @Transactional
    public void deleteFriend(User me, User target) {
        deleteMembers(me, target);

        // 만약 그룹이 비어버리면 그룹 삭제
        findMyGroups(me.getUserSeq())
            .stream()
            .filter(group -> getMembers(group.getGroupSeq()).isEmpty())
            .forEach(this::deleteGroup);

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
        return userRepository.findById(seq)
            .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
    }

    private List<GroupMember> getMembers(Long seq) {
        return groupMemberRepository.findAllByGroup_GroupSeq(seq);
    }

    private void deleteMember(Long groupSeq, Long userSeq) {
        groupMemberRepository.deleteByGroup_GroupSeqAndMember_UserSeq(groupSeq, userSeq);
    }

    private void deleteMembers(User me, User target) {
        groupMemberRepository.deleteByGroup_OwnerAndMember(me, target);
    }

    private List<UserGroup> findMyGroups(Long userSeq) {
        return userGroupRepository.findByOwner_UserSeq(userSeq);
    }

    private void deleteGroup(UserGroup group) {
        userGroupRepository.delete(group);
    }
}
