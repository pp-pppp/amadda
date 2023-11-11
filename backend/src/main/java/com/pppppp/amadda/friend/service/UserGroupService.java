package com.pppppp.amadda.friend.service;

import com.pppppp.amadda.friend.dto.request.GroupCreateRequest;
import com.pppppp.amadda.friend.entity.GroupMember;
import com.pppppp.amadda.friend.entity.UserGroup;
import com.pppppp.amadda.friend.repository.GroupMemberRepository;
import com.pppppp.amadda.friend.repository.UserGroupRepository;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.GroupErrorCode;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.service.UserService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserGroupService {

    private final UserGroupRepository userGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserService userService;

    @Transactional
    public Long createUserGroup(GroupCreateRequest request) {
        User u = userService.getUserInfoBySeq(request.ownerSeq());
        UserGroup ug = UserGroup.create(request.groupName(), u);
        ug = saveUserGroup(ug);
        return ug.getGroupSeq();
    }

    @Transactional
    public void deleteGroupAndMembers(Long groupSeq) {
        UserGroup ug = getGroup(groupSeq)
            .orElseThrow(() -> new RestApiException(GroupErrorCode.GROUP_NOT_FOUND));
        List<GroupMember> members = getMembers(groupSeq);

        deleteAllMembersInGroup(members);
        deleteGroup(ug);
    }

    // =============== 레포지토리에 직접 접근하는 메소드들 ===============

    private UserGroup saveUserGroup(UserGroup group) {
        return userGroupRepository.save(group);
    }

    private Optional<UserGroup> getGroup(Long seq) {
        return userGroupRepository.findById(seq);
    }

    private List<GroupMember> getMembers(Long seq) {
        return groupMemberRepository.findAllByGroup_GroupSeq(seq);
    }

    private void deleteAllMembersInGroup(List<GroupMember> members) {
        groupMemberRepository.deleteAll(members);
    }

    private void deleteGroup(UserGroup group) {
        userGroupRepository.delete(group);
    }

}
