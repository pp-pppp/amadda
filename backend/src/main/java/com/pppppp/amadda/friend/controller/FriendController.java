package com.pppppp.amadda.friend.controller;

import com.pppppp.amadda.friend.service.FriendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friend")
@Slf4j
public class FriendController {

    private final FriendService friendService;

    @GetMapping
//    public List<FriendReadResponse> getFriendList() {
    public void getFriendList(@RequestParam String searchKey) {
        log.info("친구 목록 검색");
        log.info("searchKey="+searchKey);
    }

    @PostMapping("/request")
    public void sendFriendRequest() {
        log.info("친구 요청 보내기");

    }

    @PostMapping("/request/{requestSeq}")
    public void acceptFriendRequest(@PathVariable String requestSeq) {
        log.info("친구 요청 수락");

    }

    @PatchMapping("/request/{requestSeq}")
    public void declineFriendRequest(@PathVariable String requestSeq) {
        log.info("친구 요청 거절");

    }

    @DeleteMapping("/{friendUserSeq}")
    public void deleteFriend(@PathVariable String friendUserSeq) {
        log.info("절연하기");

    }

    // ==================== 그룹 관련은 아래 ====================


}
