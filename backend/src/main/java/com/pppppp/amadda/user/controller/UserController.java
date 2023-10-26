package com.pppppp.amadda.user.controller;

import com.pppppp.amadda.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    private final UserService userService;

    @DeleteMapping("")
    public void deleteUser() {
        log.info("회원 탈퇴");

    }

    @PostMapping("")
    public void initUser() {
        log.info("회원 초기 설정");

    }

    @GetMapping("")
    public void findUser(@RequestParam String searchKey) {
        log.info("전체 유저에서 친구 검색. 일치로만 검색할거임. ");
        log.info("searchKey="+searchKey);

    }

    @GetMapping("/{userSeq}")
    public void hoverUserInfo(@RequestParam Long userSeq) {
        log.info("유저 정보. 호버용. ");

    }

}
