package com.pppppp.amadda.friend.controller;

import com.pppppp.amadda.alarm.service.AlarmService;
import com.pppppp.amadda.friend.dto.request.FriendRequestRequest;
import com.pppppp.amadda.friend.dto.request.GroupCreateRequest;
import com.pppppp.amadda.friend.dto.request.GroupUpdateRequest;
import com.pppppp.amadda.friend.dto.response.FriendReadResponse;
import com.pppppp.amadda.friend.dto.response.FriendRequestResponse;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friend")
@Slf4j
public class FriendController {

    private final FriendRequestService friendRequestService;
    private final UserGroupService userGroupService;
    private final GroupMemberService groupMemberService;
    private final AlarmService alarmService;
    private final FriendService friendService;
    private final TokenProvider tokenProvider;

    @GetMapping
    public ApiResponse<FriendReadResponse> getFriendList(
            HttpServletRequest http, @RequestParam String searchKey) {
        log.info("searchKey="+searchKey);
        Long userSeq = tokenProvider.getUserSeq(http);
        FriendReadResponse response = friendService.searchFriends(userSeq, searchKey);
        return ApiResponse.ok(response);
    }

    @PostMapping("/request")
    public ApiResponse sendFriendRequest(@Valid @RequestBody FriendRequestRequest request) {
        FriendRequestResponse friendRequestResponse = friendRequestService.createFriendRequest(
            request);
        alarmService.sendFriendRequest(friendRequestResponse.ownerSeq(),
            friendRequestResponse.friendSeq());
        return ApiResponse.ok("친구 신청 보내기 완료");
    }

    @PostMapping("/request/{requestSeq}")
    public ApiResponse acceptFriendRequest(
            HttpServletRequest http, @PathVariable Long requestSeq) {
        Long userSeq = tokenProvider.getUserSeq(http);

        FriendRequestResponse friendRequestResponse = friendRequestService.acceptFriendRequest(
            userSeq, requestSeq);
        alarmService.sendFriendAccept(friendRequestResponse.ownerSeq(),
            friendRequestResponse.friendSeq());
        alarmService.readFriendRequestAlarm(requestSeq);
        return ApiResponse.ok("받은 친구 신청 수락");
    }

    @PutMapping("/request/{requestSeq}")
    public ApiResponse declineFriendRequest(
            HttpServletRequest http,
            @PathVariable Long requestSeq) {
        Long userSeq = tokenProvider.getUserSeq(http);
        friendRequestService.declineFriendRequest(userSeq, requestSeq);
        alarmService.readFriendRequestAlarm(requestSeq);
        return ApiResponse.ok("받은 친구 신청 거절");
    }

    @DeleteMapping("/{friendUserSeq}")
    public ApiResponse deleteFriend(
            HttpServletRequest http,
            @PathVariable Long friendUserSeq) {
        Long userSeq = tokenProvider.getUserSeq(http);
        friendRequestService.deleteFriendAndRequest(userSeq, friendUserSeq);
        return ApiResponse.ok("유저 삭제 완료");
    }

    // ==================== 그룹 관련은 아래 ====================

    @PostMapping("/group")
    public ApiResponse makeGroup(@Valid @RequestBody GroupCreateRequest request) {

        groupMemberService.isUserValid(request.userSeqs()); // 전부 존재하는 유저들인지 검증
        Long groupSeq = userGroupService.createUserGroup(request); // 유저 그룹 만들기
        groupMemberService.createGroupMember(request, groupSeq); // 그룹 멤버 만들기
        return ApiResponse.ok("그룹 만들기 완료");
    }

    @PutMapping("/group")
    public ApiResponse editGroup(@Valid @RequestBody GroupUpdateRequest request) {
        groupMemberService.isUserValid(request.userSeqs()); // 전부 존재하는 유저들인지 검증
        groupMemberService.editGroup(request);
        return ApiResponse.ok("그룹 수정 완료");
    }

    @DeleteMapping("/group/{groupSeq}")
    public ApiResponse deleteGroup(@PathVariable Long groupSeq) {
        userGroupService.deleteGroupAndMembers(groupSeq);
        return ApiResponse.ok("그룹 삭제 완료");
    }
}
