package com.pppppp.amadda.user.controller;

import com.pppppp.amadda.global.dto.ApiResponse;
import com.pppppp.amadda.global.util.TokenProvider;
import com.pppppp.amadda.user.dto.request.UserIdCheckRequest;
import com.pppppp.amadda.user.dto.request.UserInitRequest;
import com.pppppp.amadda.user.dto.request.UserJwtRequest;
import com.pppppp.amadda.user.dto.request.UserNameCheckRequest;
import com.pppppp.amadda.user.dto.request.UserRefreshRequest;
import com.pppppp.amadda.user.dto.response.UserAccessResponse;
import com.pppppp.amadda.user.dto.response.UserIdCheckResponse;
import com.pppppp.amadda.user.dto.response.UserJwtInitResponse;
import com.pppppp.amadda.user.dto.response.UserJwtResponse;
import com.pppppp.amadda.user.dto.response.UserNameCheckResponse;
import com.pppppp.amadda.user.dto.response.UserReadResponse;
import com.pppppp.amadda.user.dto.response.UserRelationResponse;
import com.pppppp.amadda.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    @GetMapping("/login")
    public ApiResponse<UserJwtInitResponse> getTokenAfterLogin(
        @Valid @RequestBody UserJwtRequest request) {
        UserJwtInitResponse response = userService.getTokensAndCheckInit(request);
        return ApiResponse.ok(response);
    }

    @PostMapping("/login")
    public ApiResponse<String> signupUser(@Valid @RequestBody UserInitRequest request) {
        userService.saveUser(request);
        return ApiResponse.ok("회원가입에 성공했습니다.");
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
            return ApiResponse.of(HttpStatus.UNAUTHORIZED, "re-login", "다시 로그인해주세요.");
        }
    }

    @DeleteMapping
    public ApiResponse deleteUser(HttpServletRequest http) {
        Long userSeq = tokenProvider.getUserSeq(http);
        userService.deleteUserInfo(userSeq);
        return ApiResponse.ok("유저 탈퇴 완료");
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
    public ApiResponse<UserIdCheckResponse> checkId(
        @Valid @RequestBody UserIdCheckRequest request) {
        UserIdCheckResponse response = userService.chkId(request);
        return ApiResponse.ok(response);
    }

    @PostMapping("/check/name")
    public ApiResponse<UserNameCheckResponse> checkName(
        @Valid @RequestBody UserNameCheckRequest request) {
        UserNameCheckResponse response = userService.chkName(request);
        return ApiResponse.ok(response);
    }

}
