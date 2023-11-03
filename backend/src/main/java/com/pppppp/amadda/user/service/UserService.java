package com.pppppp.amadda.user.service;

import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.UserErrorCode;
import com.pppppp.amadda.user.dto.request.UserInitRequest;
import com.pppppp.amadda.user.dto.request.UserJwtRequest;
import com.pppppp.amadda.user.dto.request.UserRefreshRequest;
import com.pppppp.amadda.user.dto.response.UserAccessResponse;
import com.pppppp.amadda.user.dto.response.UserJwtInitResponse;
import com.pppppp.amadda.user.dto.response.UserJwtResponse;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
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
        try{
            getUserInfoBySeq(userSeq);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void saveUser(UserInitRequest request) {
        User u = User.create(request.userSeq(), request.userName(), request.userId(), request.imageUrl());
        saveUser(u);
    }

    public UserAccessResponse validateUser(String token) {
        try {
            tokenProvider.verifyToken(token);
            return UserAccessResponse.of(false, false, "-");
        } catch (ExpiredJwtException e1) {
            Long userSeq = tokenProvider.parseUserSeq(token);
            String rak = tokenProvider.generateRefreshAccessKey(userSeq);
            return UserAccessResponse.of(true, false, rak);
        } catch (Exception e2) {
            return UserAccessResponse.of(true, true, "-");
        }
    }


    // =============== 레포지토리에 직접 접근하는 메소드들 ===============

    private Optional<User> findUserByUserSeq(Long userSeq) {
        return Optional.ofNullable(userSeq).flatMap(userRepository::findByUserSeq);
    }

    private void saveUser(User user) {
        userRepository.save(user);
    }

    // =================== 유저 인증 관련 메소드들 ===================

    public UserJwtInitResponse getTokensAndCheckInit(UserJwtRequest request) {

        List<String> tokens = tokenProvider.createTokens(request.userSeq());
        boolean isInited = checkIsInited(request.userSeq());

        return UserJwtInitResponse.of(
                tokens.get(0),
                tokens.get(1),
                tokens.get(2),
                isInited
        );
    }

    public UserJwtResponse getNewTokens(UserRefreshRequest request, Long userSeq) {
        tokenProvider.verifyToken(request.refreshToken());
        List<String> tokens = tokenProvider.createTokens(userSeq);

        return UserJwtResponse.of(
                tokens.get(0),
                tokens.get(1),
                tokens.get(2)
        );
    }

}
