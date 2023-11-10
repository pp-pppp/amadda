package com.pppppp.amadda.user.controller;

import com.pppppp.amadda.global.dto.ApiResponse;
import com.pppppp.amadda.user.dto.request.*;
import com.pppppp.amadda.user.dto.response.*;
import com.pppppp.amadda.global.util.TokenProvider;
import com.pppppp.amadda.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final TokenProvider tokenProvider;

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

    @GetMapping("/access")
    public ApiResponse<UserAccessResponse> validateAccessToken(HttpServletRequest request) {
        String token = tokenProvider.getTokenFromCookie(request);
        UserAccessResponse response = userService.validateUser(token);
        return ApiResponse.ok(response);
    }

    @GetMapping("/refresh")
    public ApiResponse<?> getRefreshedTokens(
            HttpServletRequest http, @Valid @RequestBody UserRefreshRequest request) {
        Long userSeq = tokenProvider.getUserSeq(http);
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

    @GetMapping
    public ApiResponse<UserRelationResponse> searchUserInfoAndRelation(
            HttpServletRequest http, @RequestParam String searchKey) {
        Long userSeq = tokenProvider.getUserSeq(http);
        UserRelationResponse response = userService.getUserInfoAndIsFriend(userSeq, searchKey);
        return ApiResponse.ok(response);
    }

    @GetMapping("/my")
    public ApiResponse<UserReadResponse> getMyUserInfo(HttpServletRequest http) {
        Long userSeq = tokenProvider.getUserSeq(http);
        UserReadResponse response = userService.getUserResponse(userSeq);
        return ApiResponse.ok(response);
    }

    @GetMapping("/{userSeq}")
    public ApiResponse<UserRelationResponse> getUserInfoForHover(
            HttpServletRequest http, @PathVariable Long userSeq) {
        Long mySeq = tokenProvider.getUserSeq(http);
        UserRelationResponse response = userService.getUserInfoAndIsFriend(mySeq, userSeq);
        return ApiResponse.ok(response);
    }

    @PostMapping("/check/id")
    public ApiResponse<UserIdCheckResponse> checkId(@Valid @RequestBody UserIdCheckRequest request) {
        UserIdCheckResponse response = userService.chkId(request);
        return ApiResponse.ok(response);
    }

    @PostMapping("/check/name")
    public ApiResponse<UserNameCheckResponse> checkName(@Valid @RequestBody UserNameCheckRequest request) {
        UserNameCheckResponse response = userService.chkName(request);
        return ApiResponse.ok(response);
    }

}
