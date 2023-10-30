package com.pppppp.amadda.friend.service;

import com.pppppp.amadda.friend.dto.request.FriendRequestRequest;
import com.pppppp.amadda.friend.dto.response.FriendRequestResponse;
import com.pppppp.amadda.friend.dto.response.FriendResponse;
import com.pppppp.amadda.friend.entity.Friend;
import com.pppppp.amadda.friend.entity.FriendRequest;
import com.pppppp.amadda.friend.entity.FriendRequestStatus;
import com.pppppp.amadda.friend.repository.FriendRepository;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.FriendErrorCode;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserService userService;

    public List<FriendResponse> createFriend(FriendRequestResponse friendRequest) {

        // 상태가 ACCEPTED가 맞는지 확인
        System.out.println(">>> "+friendRequest.status());
        if(!friendRequest.status().equals("ACCEPTED"))
            throw new RestApiException(FriendErrorCode.FRIEND_INVALID);

        Long userSeq = friendRequest.ownerSeq();
        Long targetSeq = friendRequest.friendSeq();
        User u1 = userService.getUserInfoBySeq(userSeq);
        User u2 = userService.getUserInfoBySeq(targetSeq);

        Friend f1 = saveFriend(u1, u2);
        Friend f2 = saveFriend(u2, u1);

        return List.of(FriendResponse.of(f1), FriendResponse.of(f2));
    }

    public boolean isAlreadyFriends(User u1, User u2) {
        if(findFriendByOwnerAndFriend(u1, u2).orElse(null) != null) return true;
        else return false;
    }


    // =============== 레포지토리에 직접 접근하는 메소드들 ===============

    public Friend saveFriend(User u1, User u2) {
        return friendRepository.save(Friend.create(u1, u2));
    }

    public Optional<Friend> findFriendByOwnerAndFriend(User u1, User u2) {
        return friendRepository.findByOwnerAndFriend(u1, u2);
    }

}
