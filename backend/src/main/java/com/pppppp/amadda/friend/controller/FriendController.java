package com.pppppp.amadda.friend.controller;

import com.pppppp.amadda.friend.dto.request.FriendRequestRequest;
import com.pppppp.amadda.friend.dto.response.FriendRequestResponse;
import com.pppppp.amadda.friend.service.FriendRequestService;
import com.pppppp.amadda.friend.service.FriendService;
import com.pppppp.amadda.global.dto.ApiResponse;
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

    @GetMapping
    public void getFriendList(@RequestParam String searchKey) {
        log.info("친구 목록 검색");
        log.info("searchKey="+searchKey);
    }

    @PostMapping("/request")
    public ApiResponse<FriendRequestResponse> sendFriendRequest(@Valid @RequestBody FriendRequestRequest request) {
        log.info("친구 요청 보내기");
        return ApiResponse.ok(friendRequestService.createFriendRequest(request));
    }

    @PostMapping("/request/{requestSeq}")
    public ApiResponse<FriendRequestResponse> acceptFriendRequest(@PathVariable Long requestSeq) {
        log.info("친구 요청 수락");
        // TODO 추후 JWT 토큰으로 사용자 seq 디코딩 추가
        Long userSeq = 0L; // request.getHeader("Auth");
        return ApiResponse.ok(friendRequestService.acceptFriendRequest(userSeq, requestSeq));
    }

    @PatchMapping("/request/{requestSeq}")
    public ApiResponse<FriendRequestResponse> declineFriendRequest(
            HttpServletRequest request,
            @PathVariable Long requestSeq
    ) {
        log.info("친구 요청 거절");
        // TODO 추후 JWT 토큰으로 사용자 seq 디코딩 추가
        Long userSeq = 0L; // request.getHeader("Auth");
        return ApiResponse.ok(friendRequestService.declineFriendRequest(userSeq, requestSeq));
    }

    @DeleteMapping("/{friendUserSeq}")
    public ApiResponse deleteFriend(
            HttpServletRequest request,
            @PathVariable Long friendUserSeq) {
        log.info("절연하기");
        // TODO 추후 JWT 토큰으로 사용자 seq 디코딩 추가
        Long userSeq = 0L; // request.getHeader("Auth");
        friendRequestService.deleteFriendAndRequest(userSeq, friendUserSeq);

        // TODO 추후 그룹에서 친구 찾아서 삭제하는 메소드 추가

        return ApiResponse.ok("유저 삭제 완료");
    }

    // ==================== 그룹 관련은 아래 ====================


}
