package com.pppppp.amadda.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.pppppp.amadda.IntegrationTestSupport;
import com.pppppp.amadda.friend.entity.Friend;
import com.pppppp.amadda.friend.repository.FriendRepository;
import com.pppppp.amadda.global.entity.exception.RestApiException;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

class UserServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    @MockBean
    private ImageService imageService;

    @AfterEach
    void tearDown() {
        friendRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("userSeq로 유저를 조회할 수 있다.")
    @Test
    void getUserByUserSeq() {
        // given
        User u1 = User.create("1111L", "유저1", "id1", "imageUrl1");
        User u2 = User.create("1234L", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        // when
        User u = userService.getUserInfoBySeq(users.get(0).getUserSeq());

        // then
        assertThat(u).isNotNull();
        assertThat(u).extracting("kakaoId", "userName", "userId", "imageUrl")
            .containsExactlyInAnyOrder("1111L", "유저1", "id1", "imageUrl1");
    }

    @DisplayName("kakaoId로 유저를 조회할 수 있다.")
    @Test
    void getUserInfoByKakaoId() {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        User u2 = User.create("1234", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        // when
        User u = userService.getUserInfoByKakaoId("1111");

        // then
        assertThat(u).isNotNull();
        assertThat(u).extracting("kakaoId", "userName", "userId", "imageUrl")
            .containsExactlyInAnyOrder("1111", "유저1", "id1", "imageUrl1");
    }

    @DisplayName("userSeq로 유저 리스폰스를 가져온다. ")
    @Test
    void getUserResponse() {
        // given
        User u1 = User.create("1111L", "유저1", "id1", "imageUrl1");
        User u2 = User.create("1234L", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        // when
        UserReadResponse u = userService.getUserResponse(users.get(0).getUserSeq());

        // then
        assertThat(u).extracting("userSeq", "userName", "userId", "imageUrl")
            .containsExactlyInAnyOrder(users.get(0).getUserSeq(), "유저1", "id1", "imageUrl1");
    }

    @DisplayName("존재하지 않는 userSeq로 유저를 조회하면 예외가 발생한다. ")
    @Test
    void getUnexpectedUserByUserSeq() {
        // when // then
        assertThatThrownBy(() -> userService.getUserInfoBySeq(1111L))
            .isInstanceOf(RestApiException.class)
            .hasMessage("USER_NOT_FOUND");
    }

    @DisplayName("로그인 후 토큰이랑 isInited 값을 잘 받아온다. ")
    @Test
    void getTokensAndCheckInit() {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        List<User> users = userRepository.saveAll(List.of(u1));

        UserJwtRequest request = UserJwtRequest.builder()
            .kakaoId("1111")
            .imageUrl("url1")
            .build();

        // when
        UserJwtInitResponse response = userService.getTokensAndCheckInit(request);
        boolean at = tokenProvider.verifyToken(response.accessToken());
        boolean rt = tokenProvider.verifyToken(response.refreshToken());
        Long rak = tokenProvider.decodeRefreshAccessKey(response.refreshAccessKey());
        boolean isInited = response.isInited();

        // then
        assertThat(at).isTrue();
        assertThat(rt).isTrue();
        assertThat(rak).isEqualTo(users.get(0).getUserSeq());
        assertThat(isInited).isTrue();
    }

    @DisplayName("새로운 토큰들을 발급받는다. ")
    @Test
    void getNewTokens() {
        // given
        User u1 = User.create("1111", "유저1", "id1", "imageUrl1");
        List<User> users = userRepository.saveAll(List.of(u1));

        UserJwtRequest request = UserJwtRequest.builder()
            .kakaoId("1111")
            .imageUrl("url1")
            .build();
        UserJwtInitResponse response = userService.getTokensAndCheckInit(request);

        UserRefreshRequest r = UserRefreshRequest.builder()
            .refreshToken(response.refreshToken())
            .build();

        // when
        UserJwtResponse res = userService.getNewTokens(r, users.get(0).getUserSeq());
        boolean at = tokenProvider.verifyToken(res.accessToken());
        boolean rt = tokenProvider.verifyToken(res.refreshToken());
        Long rak = tokenProvider.decodeRefreshAccessKey(res.refreshAccessKey());

        // then
        assertThat(at).isTrue();
        assertThat(rt).isTrue();
        assertThat(rak).isEqualTo(users.get(0).getUserSeq());
    }

    @DisplayName("신규/기존 유저를 구분한다. ")
    @Test
    void checkIsInited() {
        // given
        String kakaoId = "1111";

        // when
        boolean b = userService.checkIsInited(kakaoId);

        // then
        assertThat(b).isFalse();
    }

    @DisplayName("유저를 잘 저장한다. ")
    @Test
    void saveUser() {
        // given
        UserInitRequest request = UserInitRequest.builder()
            .userId("jammminjung")
            .userName("잼민정")
            .imageUrl("https://www.readersnews.com/news/photo/202305/108912_78151_1733.jpg")
            .kakaoId("1111")
            .build();
        String fileName = "1111_"
            + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"))
            + ".jpg";
        String url = "https://amadda-bucket.s3.ap-northeast-2.amazonaws.com/" + fileName;
        given(imageService.saveKakaoImgInS3(request.imageUrl(), fileName))
                .willReturn(url);

        // when
        userService.saveUser(request);

        // then
        assertThat(userRepository.findAll()).hasSize(1)
            .extracting("userId", "userName", "imageUrl")
            .containsExactly(
                tuple("jammminjung", "잼민정", url)
            );
    }

    @DisplayName("유저id가 중복되는 경우 저장하지 않는다. ")
    @Test
    void saveUser_duplicatedUserId() {
        // given
        User u1 = User.create("1111L", "유저1", "id1", "imageUrl1");
        List<User> users = userRepository.saveAll(List.of(u1));
        UserInitRequest request = UserInitRequest.builder()
            .userId("id1")
            .userName("잼민정")
            .imageUrl("https://www.readersnews.com/news/photo/202305/108912_78151_1733.jpg")
            .kakaoId("")
            .build();

        // when // then
        assertThatThrownBy(() -> userService.saveUser(request))
            .isInstanceOf(RestApiException.class)
            .hasMessage("USER_ID_DUPLICATED");
    }

    @DisplayName("정상 토큰인 경우 ")
    @Test
    void validateUser1() {
        // given
        String token = tokenProvider.createTokens(1111L).get(0);

        // when
        UserAccessResponse response = userService.validateUser(token);

        // then
        assertThat(response)
            .extracting("isExpired", "isBroken", "refreshAccessKey")
            .containsExactly(false, false, "-");

    }

    @DisplayName("망가진 토큰인 경우 ")
    @Test
    void validateUser3() {
        // given
        String token = "broken-token";

        // when
        UserAccessResponse response = userService.validateUser(token);

        // then
        assertThat(response)
            .extracting("isExpired", "isBroken", "refreshAccessKey")
            .containsExactly(true, true, "-");
    }

    @DisplayName("내 유저seq와 검색키로 해당 유저와 그 유저와의 친구관계를 조회한다. ")
    @Test
    void getUserInfoAndIsFriend_withSearchKey() {
        // given
        User u1 = User.create("1111L", "유저1", "id1", "imageUrl1");
        User u2 = User.create("1234L", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        Friend f1 = Friend.create(u1, u2);
        Friend f2 = Friend.create(u2, u1);
        friendRepository.saveAll(List.of(f1, f2));

        // when
        UserRelationResponse response = userService.getUserInfoAndIsFriend(
            users.get(0).getUserSeq(), "id2");

        // then
        assertThat(response)
            .extracting("userSeq", "userName", "userId", "imageUrl", "isFriend")
            .containsExactlyInAnyOrder(users.get(1).getUserSeq(), "유저2", "id2", "imageUrl2", true);
    }

    @DisplayName("내 유저seq와 검색키로 해당 유저와 그 유저와의 친구관계를 조회한다. ")
    @Test
    void getUserInfoAndIsNotFriend_withSearchKey() {
        // given
        User u1 = User.create("1111L", "유저1", "id1", "imageUrl1");
        User u2 = User.create("1234L", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        // when
        UserRelationResponse response = userService.getUserInfoAndIsFriend(
            users.get(0).getUserSeq(), "id2");

        // then
        assertThat(response)
            .extracting("userSeq", "userName", "userId", "imageUrl", "isFriend")
            .containsExactlyInAnyOrder(users.get(1).getUserSeq(), "유저2", "id2", "imageUrl2", false);
    }

    @DisplayName("호버한 상대의 유저 seq로 해당 유저와 그 유저와의 친구관계를 조회한다. ")
    @Test
    void getUserInfoAndIsFriend() {
        // given
        User u1 = User.create("1111L", "유저1", "id1", "imageUrl1");
        User u2 = User.create("1234L", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        Friend f1 = Friend.create(u1, u2);
        Friend f2 = Friend.create(u2, u1);
        friendRepository.saveAll(List.of(f1, f2));

        // when
        UserRelationResponse response = userService.getUserInfoAndIsFriend(
            users.get(0).getUserSeq(), users.get(1).getUserSeq());

        // then
        assertThat(response)
            .extracting("userSeq", "userName", "userId", "imageUrl", "isFriend")
            .containsExactlyInAnyOrder(users.get(1).getUserSeq(), "유저2", "id2", "imageUrl2", true);
    }

    @DisplayName("호버한 상대의 유저 seq로 해당 유저와 그 유저와의 친구관계를 조회한다. ")
    @Test
    void getUserInfoAndIsNotFriend() {
        // given
        User u1 = User.create("1111L", "유저1", "id1", "imageUrl1");
        User u2 = User.create("1234L", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        // when
        UserRelationResponse response = userService.getUserInfoAndIsFriend(
            users.get(0).getUserSeq(), users.get(1).getUserSeq());

        // then
        assertThat(response)
            .extracting("userSeq", "userName", "userId", "imageUrl", "isFriend")
            .containsExactlyInAnyOrder(users.get(1).getUserSeq(), "유저2", "id2", "imageUrl2", false);
    }

    @DisplayName("내 유저seq와 검색키로 검색해 대상이 존재하지 않는경우 dto를 비워 리턴한다. ")
    @Test
    void getUserInfoAndIsFriend__withSearchKey_notFound() {
        // given
        User u1 = User.create("1111L", "유저1", "id1", "imageUrl1");
        User u2 = User.create("1234L", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        // when
        UserRelationResponse response = userService.getUserInfoAndIsFriend(
            users.get(0).getUserSeq(), "id3");

        // then
        assertThat(response)
            .extracting("userSeq", "userName", "userId", "imageUrl", "isFriend")
            .containsExactlyInAnyOrder(null, "", "", "", false);
    }

    @DisplayName("친구의 양방향 관계가 유효한지 검사한다. - 유효1")
    @Test
    void isFriend() {
        // given
        User u1 = User.create("1111L", "유저1", "id1", "imageUrl1");
        User u2 = User.create("1234L", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        Friend f1 = Friend.create(u1, u2);
        Friend f2 = Friend.create(u2, u1);
        friendRepository.saveAll(List.of(f1, f2));

        // when
        boolean isFriend = userService.isFriend(u1, u2);

        // then
        Assertions.assertTrue(isFriend);
    }

    @DisplayName("친구의 양방향 관계가 유효한지 검사한다. - 유효2")
    @Test
    void isNotFriend() {
        // given
        User u1 = User.create("1111L", "유저1", "id1", "imageUrl1");
        User u2 = User.create("1234L", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        // when
        boolean isFriend = userService.isFriend(u1, u2);

        // then
        Assertions.assertFalse(isFriend);
    }

    @DisplayName("친구의 양방향 관계가 유효한지 검사한다. - 안유효")
    @Test
    void isFriend_damaged() {
        // given
        User u1 = User.create("1111L", "유저1", "id1", "imageUrl1");
        User u2 = User.create("1234L", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        Friend f1 = Friend.create(u1, u2);
        friendRepository.saveAll(List.of(f1));

        // when // then
        assertThatThrownBy(() -> userService.isFriend(u1, u2))
            .isInstanceOf(RestApiException.class)
            .hasMessage("FRIEND_RELATION_DAMAGED");
    }

    @DisplayName("아이디 중복/유효 여부를 반환한다. - 중복,유효")
    @Test
    void chkId_duplicated() {
        // given
        User u1 = User.create("1111L", "유저1", "id1", "imageUrl1");
        User u2 = User.create("1234L", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        UserIdCheckRequest request = UserIdCheckRequest.builder()
            .userId("id1")
            .build();

        // when
        UserIdCheckResponse response = userService.chkId(request);

        // then
        Assertions.assertTrue(response.isDuplicated());
        Assertions.assertTrue(response.isValid());
    }

    @DisplayName("아이디 중복/유효 여부를 반환한다. - 굳")
    @Test
    void chkId_good() {
        // given
        User u1 = User.create("1111L", "유저1", "id1", "imageUrl1");
        User u2 = User.create("1234L", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        UserIdCheckRequest request = UserIdCheckRequest.builder()
            .userId("id")
            .build();

        // when
        UserIdCheckResponse response = userService.chkId(request);

        // then
        Assertions.assertTrue(response.isValid());
        Assertions.assertFalse(response.isDuplicated());
    }

    @DisplayName("아이디 중복/유효 여부를 반환한다. - 안중복, 안유효")
    @Test
    void chkId_notValid() {
        // given
        User u1 = User.create("1111L", "유저1", "id1", "imageUrl1");
        User u2 = User.create("1234L", "유저2", "id2", "imageUrl2");
        List<User> users = userRepository.saveAll(List.of(u1, u2));

        UserIdCheckRequest request = UserIdCheckRequest.builder()
            .userId("ASDF((#")
            .build();

        // when
        UserIdCheckResponse response = userService.chkId(request);

        // then
        Assertions.assertFalse(response.isDuplicated());
        Assertions.assertFalse(response.isValid());
    }

    @DisplayName("닉네임 유효 여부를 반환한다. - 유효")
    @Test
    void chkName_valid() {
        // given
        UserNameCheckRequest request = UserNameCheckRequest.builder()
            .userName("이름")
            .build();

        // when
        UserNameCheckResponse response = userService.chkName(request);

        // then
        Assertions.assertTrue(response.isValid());
    }

    @DisplayName("닉네임 유효 여부를 반환한다. - 안유효")
    @Test
    void chkName_notValid() {
        // given
        UserNameCheckRequest request = UserNameCheckRequest.builder()
            .userName("@Zㅣ존민정@")
            .build();

        // when
        UserNameCheckResponse response = userService.chkName(request);

        // then
        Assertions.assertFalse(response.isValid());
    }

    @DisplayName("회원탈퇴시 유저 정보가 초기화 된다. ")
    @Test
    @Transactional
    void deleteUserInfo() {
        // given
        User u1 = User.create("0L", "유저1", "id1", "///imageUrl1");
        User u = userRepository.save(u1);

        // when
        userService.deleteUserInfo(u.getUserSeq());

        // then
        assertThat(userRepository.findAll())
            .extracting("userSeq", "kakaoId", "userName", "userId", "imageUrl")
            .containsExactlyInAnyOrder(
                tuple(u.getUserSeq(), "", "", "", "")
            );
    }

}
