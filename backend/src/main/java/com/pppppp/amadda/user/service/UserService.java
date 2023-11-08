package com.pppppp.amadda.user.service;

import com.pppppp.amadda.friend.entity.Friend;
import com.pppppp.amadda.friend.repository.FriendRepository;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.FriendErrorCode;
import com.pppppp.amadda.global.entity.exception.errorcode.UserErrorCode;
import com.pppppp.amadda.user.dto.request.UserInitRequest;
import com.pppppp.amadda.user.dto.request.UserJwtRequest;
import com.pppppp.amadda.user.dto.request.UserRefreshRequest;
import com.pppppp.amadda.user.dto.response.*;
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
    private final FriendRepository friendRepository;
    private final TokenProvider tokenProvider;


    public User getUserInfoBySeq(Long userSeq) {
        return findUserByUserSeq(userSeq).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
    }

    public UserReadResponse getUserResponse(Long userSeq) {
        return UserReadResponse.of(getUserInfoBySeq(userSeq));
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
        User u = User.create(Long.parseLong(request.userSeq()), request.userName(), request.userId(), request.imageUrl());
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

    public UserRelationResponse getUserInfoAndIsFriend(Long userSeq, String searchKey) {
        User result = findUserWithExactId(searchKey).orElse(null);

        if(result == null) return UserRelationResponse.notFound();
        User owner = findUserByUserSeq(userSeq)
                .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        boolean isFriend = isFriend(owner, result);
        return UserRelationResponse.of(result, isFriend);
    }

    public boolean isFriend(User owner, User target) {
        boolean chk1 = findTargetUserInFriend(owner, target).isPresent();
        boolean chk2 = findTargetUserInFriend(target, owner).isPresent();
        if(chk1^chk2) throw new RestApiException(FriendErrorCode.FRIEND_RELATION_DAMAGED);
        return chk1 && chk2;
    }

    // =============== 레포지토리에 직접 접근하는 메소드들 ===============

    private Optional<User> findUserByUserSeq(Long userSeq) {
        return Optional.ofNullable(userSeq).flatMap(userRepository::findByUserSeq);
    }

    private void saveUser(User user) {
        userRepository.save(user);
    }

    private Optional<User> findUserWithExactId(String searchKey) {
        return userRepository.findByUserId(searchKey);
    }

    private Optional<Friend> findTargetUserInFriend(User owner, User target) {
        return friendRepository.findByOwnerAndFriend(owner, target);
    }

    // =================== 유저 인증 관련 메소드들 ===================

    public UserJwtInitResponse getTokensAndCheckInit(UserJwtRequest request) {

        Long userSeq = Long.parseLong(request.userSeq());
        List<String> tokens = tokenProvider.createTokens(userSeq);
        boolean isInited = checkIsInited(userSeq);

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
