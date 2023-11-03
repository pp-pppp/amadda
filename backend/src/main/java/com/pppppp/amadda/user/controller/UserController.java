package com.pppppp.amadda.user.controller;

import com.pppppp.amadda.global.dto.ApiResponse;
import com.pppppp.amadda.user.dto.request.UserInitRequest;
import com.pppppp.amadda.user.dto.request.UserJwtRequest;
import com.pppppp.amadda.user.dto.request.UserRefreshRequest;
import com.pppppp.amadda.user.dto.response.UserJwtInitResponse;
import com.pppppp.amadda.user.dto.response.UserJwtResponse;
import com.pppppp.amadda.user.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public ApiResponse<?> getTokenAfterLogin(@Valid @RequestBody UserJwtRequest request) {
        UserJwtInitResponse response = userService.getTokensAndCheckInit(request);
        return ApiResponse.ok(response);
    }

    @PostMapping("/login")
    public ApiResponse signupUser(@Valid @RequestBody UserInitRequest request) {
        userService.saveUser(request);
        return ApiResponse.ok("사용자 등록 완료");
    }

    @GetMapping("/refresh")
    public ApiResponse<?> getRefreshedTokens(@Valid @RequestBody UserRefreshRequest request) {
        Long userSeq = 0L;
        try {
            UserJwtResponse response = userService.getNewTokens(request, userSeq);
            return ApiResponse.ok(response);
        } catch (Exception e) {
            return ApiResponse.of(HttpStatus.UNAUTHORIZED, "re-login","재로그인 필요. ");
        }
    }

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
