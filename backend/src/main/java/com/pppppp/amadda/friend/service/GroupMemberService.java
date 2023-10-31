package com.pppppp.amadda.friend.service;

import com.pppppp.amadda.friend.dto.request.GroupCreateRequest;
import com.pppppp.amadda.friend.entity.GroupMember;
import com.pppppp.amadda.friend.entity.UserGroup;
import com.pppppp.amadda.friend.repository.GroupMemberRepository;
import com.pppppp.amadda.friend.repository.UserGroupRepository;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.GroupErrorCode;
import com.pppppp.amadda.global.entity.exception.errorcode.UserErrorCode;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import com.pppppp.amadda.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;

    public void isUserValid(GroupCreateRequest request) {
        List<Long> seqs = request.userSeqs();
        seqs.forEach(seq -> {
            User u = findUserBySeq(seq);
        });
    }

    public void createGroupMember(GroupCreateRequest request, Long groupSeq) {
        UserGroup ug = getGroup(groupSeq)
                .orElseThrow(() -> new RestApiException(GroupErrorCode.GROUP_NOT_FOUND));

        request.userSeqs().stream()
                .map(this::findUserBySeq)
                .map(u -> GroupMember.create(ug, u))
                .forEach(this::saveGroupMember);
    }


    // =============== 레포지토리에 직접 접근하는 메소드들 ===============

    private GroupMember saveGroupMember(GroupMember gm) {
        return groupMemberRepository.save(gm);
    }

    private Optional<UserGroup> getGroup(Long seq) {
        return userGroupRepository.findById(seq);
    }

    private User findUserBySeq(Long seq) {
        return userRepository.findByUserSeq(seq)
                .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
    }
}
