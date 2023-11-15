package com.pppppp.amadda.user.service;

import com.pppppp.amadda.friend.entity.Friend;
import com.pppppp.amadda.friend.repository.FriendRepository;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.FriendErrorCode;
import com.pppppp.amadda.global.entity.exception.errorcode.UserErrorCode;
import com.pppppp.amadda.global.service.ImageService;
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
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final TokenProvider tokenProvider;
    private final ImageService imageService;


    public User getUserInfoBySeq(Long userSeq) {
        return findUserByUserSeq(userSeq)
            .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
    }

    public User getUserInfoByKakaoId(String kakaoId) {
        return findUserByKakaoId(kakaoId)
                .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
    }

    public UserReadResponse getUserResponse(Long userSeq) {
        return UserReadResponse.of(getUserInfoBySeq(userSeq));
    }

    public boolean checkIsInited(String kakaoId) {
        try {
            getUserInfoByKakaoId(kakaoId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public void saveUser(UserInitRequest request) {
        String fileName = request.kakaoId() + "_"
            + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"))
            + ".jpg";
        String s3Url = imageService.saveKakaoImgInS3(request.imageUrl(), fileName);

        if(findUserWithExactId(request.userId()).isPresent())
            throw new RestApiException(UserErrorCode.USER_ID_DUPLICATED);

        User u = User.create(request.kakaoId(), request.userName(),
            request.userId(), s3Url);
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

    public UserRelationResponse getUserInfoAndIsFriend(Long mySeq, Long targetSeq) {
        User target = getUserInfoBySeq(targetSeq);
        User owner = getUserInfoBySeq(mySeq);
        boolean isFriend = isFriend(owner, target);
        return UserRelationResponse.of(target, isFriend);
    }

    public UserRelationResponse getUserInfoAndIsFriend(Long userSeq, String searchKey) {
        User result = findUserWithExactId(searchKey).orElse(null);
        if (result == null) {
            return UserRelationResponse.notFound();
        }

        User owner = findUserByUserSeq(userSeq)
            .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
        boolean isFriend = isFriend(owner, result);
        return UserRelationResponse.of(result, isFriend);
    }

    public boolean isFriend(User owner, User target) {
        boolean chk1 = findTargetUserInFriend(owner, target).isPresent();
        boolean chk2 = findTargetUserInFriend(target, owner).isPresent();
        if (chk1 ^ chk2) {
            throw new RestApiException(FriendErrorCode.FRIEND_RELATION_DAMAGED);
        }
        return chk1 && chk2;
    }

    public UserIdCheckResponse chkId(UserIdCheckRequest request) {
        boolean isDup = findUserWithExactId(request.userId()).isPresent();
        boolean isValid = isValidId(request.userId());
        return UserIdCheckResponse.of(isDup, isValid);
    }

    private boolean isValidId(String id) {
        String regex = "^[^A-Z!@#$%^&*()_+={}|\\[\\]:\";'<>?,.\\s\\u3131-\\uD79D]{1,20}$";
        return Pattern.matches(regex, id);
    }

    public UserNameCheckResponse chkName(UserNameCheckRequest request) {
        boolean isValid = isValidName(request.userName());
        return UserNameCheckResponse.of(isValid);
    }

    private boolean isValidName(String name) {
        String regex = "^[^A-Z!@#$%^&*()_+={}|\\[\\]\\\\:\";'<>?,./\\s]{1,20}$";
        return Pattern.matches(regex, name);
    }

    @Transactional
    public void deleteUserInfo(Long userSeq) {
        User u = getUserInfoBySeq(userSeq);
        imageService.deleteImgInS3(u.getImageUrl());
        u.delete();
    }

    // =============== 레포지토리에 직접 접근하는 메소드들 ===============

    private Optional<User> findUserByUserSeq(Long userSeq) {
        return Optional.ofNullable(userSeq).flatMap(userRepository::findById);
    }

    private Optional<User> findUserByKakaoId(String kakaoId) {
        return userRepository.findByKakaoId(kakaoId);
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
        Long userSeq = getUserInfoByKakaoId(request.kakaoId()).getUserSeq();
        List<String> tokens = tokenProvider.createTokens(userSeq);
        boolean isInited = checkIsInited(request.kakaoId());

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
