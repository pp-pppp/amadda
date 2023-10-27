package com.pppppp.amadda.friend.service;

import com.pppppp.amadda.friend.dto.request.FriendRequestRequest;
import com.pppppp.amadda.friend.dto.response.FriendRequestResponse;
import com.pppppp.amadda.friend.entity.FriendRequest;
import com.pppppp.amadda.friend.entity.FriendRequestStatus;
import com.pppppp.amadda.friend.repository.FriendRequestRepository;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.FriendRequestErrorCode;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserService userService;

    public FriendRequestResponse createRequest(FriendRequestRequest request) {

        Long userSeq = request.userSeq();
        Long targetSeq = request.targetSeq();
        User u1 = userService.getUserInfoBySeq(userSeq);
        User u2 = userService.getUserInfoBySeq(targetSeq);

        // 이미 요청된(REQUESTED) 혹은 친구 상태(ACCEPTED)라면 예외처리
        FriendRequest chk = findFriendRequestByUserAndFriend(u1, u2)
                .orElse(findFriendRequestByUserAndFriend(u2, u1).orElse(null));
        if(chk != null && (chk.getStatus() == FriendRequestStatus.REQUESTED
                        || chk.getStatus() == FriendRequestStatus.ACCEPTED))
            throw new RestApiException(FriendRequestErrorCode.FRIEND_REQUEST_INVALID);

        FriendRequest fr = FriendRequest.create(u1, u2);
        fr = friendRequestRepository.save(fr);

        return FriendRequestResponse.of(fr);
    }

    public Optional<FriendRequest> findFriendRequestByUserAndFriend(User u1, User u2) {
        return Optional.ofNullable(u1)
                .flatMap(user1 -> Optional.ofNullable(u2)
                        .flatMap(user2 -> friendRequestRepository.findByOwnerAndFriend(user1, user2))
                );
    }
}
