package com.pppppp.amadda.user.service;

import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.UserErrorCode;
import com.pppppp.amadda.user.dto.request.UserJwtRequest;
import com.pppppp.amadda.user.dto.response.UserJwtResponse;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;


    public User getUserInfoBySeq(Long userSeq) {
        return findUserByUserSeq(userSeq).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
    }

    public boolean checkIsInited(Long userSeq) {
        return getUserInfoBySeq(userSeq).isInited();
    }


    // =============== 레포지토리에 직접 접근하는 메소드들 ===============

    private Optional<User> findUserByUserSeq(Long userSeq) {
        return Optional.ofNullable(userSeq).flatMap(userRepository::findByUserSeq);
    }


    // =================== 유저 인증 관련 메소드들 ===================

    public UserJwtResponse getTokensAndCheckInit(UserJwtRequest request) {

        List<String> tokens = tokenProvider.createTokens(request);
        boolean isInited = checkIsInited(request.userSeq());

        return UserJwtResponse.of(
                tokens.get(0),
                tokens.get(1),
                tokens.get(2),
                isInited
        );
    }

}
