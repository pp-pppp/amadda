package com.pppppp.amadda.friend.service;

import com.pppppp.amadda.friend.dto.response.*;
import com.pppppp.amadda.friend.entity.*;
import com.pppppp.amadda.friend.repository.FriendRepository;
import com.pppppp.amadda.friend.repository.GroupMemberRepository;
import com.pppppp.amadda.friend.repository.UserGroupRepository;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.FriendErrorCode;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserService userService;
    private final UserGroupRepository userGroupRepository;
    private final GroupMemberRepository groupMemberRepository;

    public List<FriendResponse> createFriend(FriendRequestResponse friendRequest) {

        // 상태가 ACCEPTED가 맞는지 확인
        if(!friendRequest.status().equals("ACCEPTED"))
            throw new RestApiException(FriendErrorCode.FRIEND_INVALID);

        Long userSeq = friendRequest.ownerSeq();
        Long targetSeq = friendRequest.friendSeq();
        User u1 = userService.getUserInfoBySeq(userSeq);
        User u2 = userService.getUserInfoBySeq(targetSeq);

        Friend f1 = saveFriend(u1, u2);
        Friend f2 = saveFriend(u2, u1);

        return List.of(FriendResponse.of(f1), FriendResponse.of(f2));
    }

    public void deleteFriends(User u1, User u2) {

        Long relationSeq1 = findFriendByOwnerAndFriend(u1, u2)
                .orElseThrow(() -> new RestApiException(FriendErrorCode.FRIEND_NOT_FOUND))
                .getRelationSeq();
        Long relationSeq2 = findFriendByOwnerAndFriend(u2, u1)
                .orElseThrow(() -> new RestApiException(FriendErrorCode.FRIEND_NOT_FOUND))
                .getRelationSeq();
        deleteFriendBySeq(relationSeq1);
        deleteFriendBySeq(relationSeq2);
    }

    public FriendReadResponse searchFriends(Long userSeq, String searchKey) {

        List<GroupResponse> groupResponses = searchInGroups(userSeq, searchKey);
        List<MemberResponse> memberResponses = searchFriendsWithKey(userSeq, searchKey)
                .stream().map(MemberResponse::of)
                .toList();

        return FriendReadResponse.of(
                groupResponses,
                memberResponses
        );
    }

    public List<GroupResponse> searchInGroups(Long userSeq, String searchKey) {

        List<UserGroup> myGroups = getMyUserGroups(userSeq); // 내 그룹들만 뽑음

        List<Long> myGroupSeqs = myGroups.stream() // 내 그룹들 seq만 뽑아 담은 리스트
                .map(UserGroup::getGroupSeq)
                .toList();

        // 내 그룹에 해당하면서 검색키에 해당하는 멤버들만 뽑음
        List<GroupMember> searchedGroupMembers = getMySearchedGroupMembers(myGroupSeqs, searchKey);

        Set<Long> newHS = searchedGroupMembers.stream() // 검색에 해당하는 그룹 seq만 뽑아 담은 쎗
                .map(mem -> mem.getGroup().getGroupSeq())
                .collect(Collectors.toSet());

        return myGroups.stream()
                .filter(group -> newHS.contains(group.getGroupSeq())) // 검색에서 걸러진 그룹들 여기서도 걸러주기
                .map(group -> {
                    List<MemberResponse> mems = searchedGroupMembers.stream()
                            .filter(mem -> mem.getGroup().getGroupSeq() == group.getGroupSeq())
                            .map(MemberResponse::of)
                            .toList();
                    return GroupResponse.of(group, mems);
                })
                .toList();
    }



    // =============== 레포지토리에 직접 접근하는 메소드들 ===============
    private Friend saveFriend(User u1, User u2) {
        return friendRepository.save(Friend.create(u1, u2));
    }

    public Optional<Friend> findFriendByOwnerAndFriend(User u1, User u2) {
        return friendRepository.findByOwnerAndFriend(u1, u2);
    }

    private void deleteFriendBySeq(Long relationSeq) {
        friendRepository.deleteById(relationSeq);
    }

    private List<UserGroup> getMyUserGroups(Long userSeq) {
        return userGroupRepository.findByOwner_UserSeq(userSeq);
    }

    private List<GroupMember> getMySearchedGroupMembers(List<Long> myGroupSeqs, String searchKey) {
        return groupMemberRepository.findByGroupSeqsAndSearchKey(myGroupSeqs, searchKey);
    }

    private List<Friend> searchFriendsWithKey(Long userSeq, String searchKey) {
        return friendRepository.findByOwnerSeqAndSearchKey(userSeq, searchKey);
    }

    // =========================================================

    public boolean isAlreadyFriends(User u1, User u2) {
        return findFriendByOwnerAndFriend(u1, u2).isPresent();
    }

}
