package com.pppppp.amadda.friend.controller;

import com.pppppp.amadda.alarm.service.AlarmService;
import com.pppppp.amadda.friend.dto.request.FriendRequestRequest;
import com.pppppp.amadda.friend.dto.request.GroupCreateRequest;
import com.pppppp.amadda.friend.dto.request.GroupPatchRequest;
import com.pppppp.amadda.friend.dto.response.FriendRequestResponse;
import com.pppppp.amadda.friend.dto.response.GroupPatchResponse;
import com.pppppp.amadda.friend.service.FriendRequestService;
import com.pppppp.amadda.friend.service.GroupMemberService;
import com.pppppp.amadda.friend.service.UserGroupService;
import com.pppppp.amadda.global.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    private final AlarmService alarmService;

    @GetMapping
    public void getFriendList(@RequestParam String searchKey) {
        log.info("친구 목록 검색");
        log.info("searchKey="+searchKey);
    }

    @PostMapping("/request")
    public ApiResponse sendFriendRequest(@Valid @RequestBody FriendRequestRequest request) {
        FriendRequestResponse friendRequestResponse = friendRequestService.createFriendRequest(
            request);
        alarmService.sendFriendRequest(friendRequestResponse.requestSeq(),
            friendRequestResponse.ownerSeq(), friendRequestResponse.friendSeq());
        return ApiResponse.ok("친구 신청 보내기 완료");
    }

    @PostMapping("/request/{requestSeq}")
    public ApiResponse acceptFriendRequest(@PathVariable Long requestSeq) {
        // TODO 추후 JWT 토큰으로 사용자 seq 디코딩 추가
        Long userSeq = 0L; // request.getHeader("Auth");
        FriendRequestResponse friendRequestResponse = friendRequestService.acceptFriendRequest(
            userSeq, requestSeq);
        alarmService.sendFriendAccept(friendRequestResponse.ownerSeq(),
            friendRequestResponse.friendSeq());
        return ApiResponse.ok("받은 친구 신청 수락");
    }

    @PatchMapping("/request/{requestSeq}")
    public ApiResponse declineFriendRequest(
            HttpServletRequest request,
            @PathVariable Long requestSeq) {
        // TODO 추후 JWT 토큰으로 사용자 seq 디코딩 추가
        Long userSeq = 0L; // request.getHeader("Auth");
        friendRequestService.declineFriendRequest(userSeq, requestSeq);
        return ApiResponse.ok("받은 친구 신청 거절");
    }

    @DeleteMapping("/{friendUserSeq}")
    public ApiResponse deleteFriend(
            HttpServletRequest request,
            @PathVariable Long friendUserSeq) {
        // TODO 추후 JWT 토큰으로 사용자 seq 디코딩 추가
        Long userSeq = 0L; // request.getHeader("Auth");
        friendRequestService.deleteFriendAndRequest(userSeq, friendUserSeq);

        // TODO 추후 그룹에서 친구 찾아서 삭제하는 메소드 추가

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

    @PatchMapping("/group")
    public ApiResponse editGroup(@Valid @RequestBody GroupPatchRequest request) {
        groupMemberService.isUserValid(request.userSeqs()); // 전부 존재하는 유저들인지 검증
        groupMemberService.editGroup(request);
        return ApiResponse.ok("그룹 수정 완료");
    }

}
