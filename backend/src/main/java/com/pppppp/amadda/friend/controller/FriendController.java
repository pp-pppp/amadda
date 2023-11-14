package com.pppppp.amadda.friend.controller;

import com.pppppp.amadda.friend.dto.request.FriendRequestRequest;
import com.pppppp.amadda.friend.dto.request.GroupCreateRequest;
import com.pppppp.amadda.friend.dto.request.GroupUpdateRequest;
import com.pppppp.amadda.friend.dto.response.FriendReadResponse;
import com.pppppp.amadda.friend.service.FriendRequestService;
import com.pppppp.amadda.friend.service.FriendService;
import com.pppppp.amadda.friend.service.GroupMemberService;
import com.pppppp.amadda.friend.service.UserGroupService;
import com.pppppp.amadda.global.dto.ApiResponse;
import com.pppppp.amadda.global.util.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friend")
@Slf4j
public class FriendController {

    private final FriendRequestService friendRequestService;
    private final UserGroupService userGroupService;
    private final GroupMemberService groupMemberService;
    private final FriendService friendService;
    private final TokenProvider tokenProvider;

    @GetMapping
    public ApiResponse<FriendReadResponse> getFriendList(
        HttpServletRequest http, @RequestParam String searchKey) {
        log.info("searchKey=" + searchKey);
        Long userSeq = tokenProvider.getUserSeq(http);
        FriendReadResponse response = friendService.searchFriends(userSeq, searchKey);
        return ApiResponse.ok(response);
    }

    @PostMapping("/request")
    public ApiResponse<String> sendFriendRequest(
            HttpServletRequest http, @Valid @RequestBody FriendRequestRequest request) {
        Long userSeq = tokenProvider.getUserSeq(http);
        friendRequestService.createFriendRequest(userSeq, request);
        return ApiResponse.ok("친구를 신청했습니다.");
    }

    @PostMapping("/request/{requestSeq}")
    public ApiResponse<String> acceptFriendRequest(
        HttpServletRequest http, @PathVariable Long requestSeq) {
        Long userSeq = tokenProvider.getUserSeq(http);
        friendRequestService.acceptFriendRequest(userSeq, requestSeq);
        return ApiResponse.ok("친구 신청을 수락했습니다.");
    }

    @PutMapping("/request/{requestSeq}")
    public ApiResponse<String> declineFriendRequest(
        HttpServletRequest http,
        @PathVariable Long requestSeq) {
        Long userSeq = tokenProvider.getUserSeq(http);
        friendRequestService.declineFriendRequest(userSeq, requestSeq);
        return ApiResponse.ok("친구 신청을 거절했습니다.");
    }

    @DeleteMapping("/{friendUserSeq}")
    public ApiResponse<String> deleteFriend(
        HttpServletRequest http,
        @PathVariable Long friendUserSeq) {
        Long userSeq = tokenProvider.getUserSeq(http);
        friendRequestService.deleteFriendAndRequest(userSeq, friendUserSeq);
        return ApiResponse.ok("친구를 삭제했습니다.");
    }

    // ==================== 그룹 관련은 아래 ====================

    @PostMapping("/group")
    public ApiResponse<String> makeGroup(
            HttpServletRequest http, @Valid @RequestBody GroupCreateRequest request) {
        Long userSeq = tokenProvider.getUserSeq(http);
        groupMemberService.isUserValid(request.userSeqs()); // 전부 존재하는 유저들인지 검증
        Long groupSeq = userGroupService.createUserGroup(userSeq, request); // 유저 그룹 만들기
        groupMemberService.createGroupMember(request, groupSeq); // 그룹 멤버 만들기
        return ApiResponse.ok("친구 그룹을 생성했습니다.");
    }

    @PutMapping("/group")
    public ApiResponse<String> editGroup(@Valid @RequestBody GroupUpdateRequest request) {
        groupMemberService.isUserValid(request.userSeqs()); // 전부 존재하는 유저들인지 검증
        groupMemberService.editGroup(request);
        return ApiResponse.ok("친구 그룹을 수정했습니다.");
    }

    @DeleteMapping("/group/{groupSeq}")
    public ApiResponse<String> deleteGroup(@PathVariable Long groupSeq) {
        userGroupService.deleteGroupAndMembers(groupSeq);
        return ApiResponse.ok("친구 그룹을 삭제했습니다.");
    }
}
