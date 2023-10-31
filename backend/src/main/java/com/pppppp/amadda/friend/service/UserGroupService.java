package com.pppppp.amadda.friend.service;

import com.pppppp.amadda.friend.dto.request.GroupCreateRequest;
import com.pppppp.amadda.friend.entity.GroupMember;
import com.pppppp.amadda.friend.entity.UserGroup;
import com.pppppp.amadda.friend.repository.UserGroupRepository;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserGroupService {

    private final UserGroupRepository userGroupRepository;
    private final UserService userService;

    public Long createUserGroup(GroupCreateRequest request) {
        User u = userService.getUserInfoBySeq(request.ownerSeq());
        UserGroup ug = UserGroup.create(request.groupName(), u);
        ug = saveUserGroup(ug);
        return ug.getGroupSeq();
    }


    // =============== 레포지토리에 직접 접근하는 메소드들 ===============

    private UserGroup saveUserGroup(UserGroup group) {
        return userGroupRepository.save(group);
    }

}
