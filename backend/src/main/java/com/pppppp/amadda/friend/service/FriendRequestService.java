package com.pppppp.amadda.friend.service;

import com.pppppp.amadda.friend.dto.request.FriendRequestRequest;
import com.pppppp.amadda.friend.dto.response.FriendRequestResponse;
import com.pppppp.amadda.friend.entity.FriendRequest;
import com.pppppp.amadda.friend.entity.FriendRequestStatus;
import com.pppppp.amadda.friend.repository.FriendRequestRepository;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.FriendErrorCode;
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
    private final FriendService friendService;

    public FriendRequestResponse createFriendRequest(FriendRequestRequest request) {

        Long userSeq = request.userSeq();
        Long targetSeq = request.targetSeq();
        User u1 = userService.getUserInfoBySeq(userSeq);
        User u2 = userService.getUserInfoBySeq(targetSeq);

        // 이미 요청된(REQUESTED) 혹은 친구 상태(ACCEPTED)라면 예외처리
        FriendRequest chk = findFriendRequestByUserAndFriend(u1, u2)
                .orElse(findFriendRequestByUserAndFriend(u2, u1).orElse(null));
        if(chk != null && (isRequested(chk) || isAccepted(chk)))
            throw new RestApiException(FriendRequestErrorCode.FRIEND_REQUEST_INVALID);

        FriendRequest fr = FriendRequest.create(u1, u2);
        fr = friendRequestRepository.save(fr);

        return FriendRequestResponse.of(fr);
    }

    public FriendRequestResponse declineFriendRequest(Long userSeq, Long requestSeq) {

        FriendRequest chk = findFriendRequestBySeq(requestSeq).orElse(null);

        // userSeq == chk.friendSeq && REQUESTED 상태일때만 허용
        if(chk != null && isTarget(userSeq, chk) && isRequested(chk)) {
            chk.updateStatus(FriendRequestStatus.DECLINED);
            return FriendRequestResponse.of(chk);
        }
        else throw new RestApiException(FriendRequestErrorCode.FRIEND_REQUEST_INVALID);
    }

    public FriendRequestResponse acceptFriendRequest(Long userSeq, Long requestSeq) {

        FriendRequest chk = findFriendRequestBySeq(requestSeq).orElse(null);

        // userSeq == chk.friendSeq && REQUESTED && 이미 친구가 아닌 상태일때만 허용
        if(chk != null && isTarget(userSeq, chk) && isRequested(chk)
                && !friendService.isAlreadyFriends(chk.getOwner(), chk.getFriend())) {

            chk.updateStatus(FriendRequestStatus.ACCEPTED);
            friendService.createFriend(FriendRequestResponse.of(chk));
            return FriendRequestResponse.of(chk);
        }
        else throw new RestApiException(FriendRequestErrorCode.FRIEND_REQUEST_INVALID);
    }

    @Transactional
    public void deleteFriendAndRequest(Long userSeq, Long friendSeq) {

        User u1 = userService.getUserInfoBySeq(userSeq);
        User u2 = userService.getUserInfoBySeq(friendSeq);

        FriendRequest chk = findFriendRequestByUserAndFriend(u1, u2)
                .orElse(findFriendRequestByUserAndFriend(u2, u1).orElse(null));
        if(chk == null) throw new RestApiException(FriendRequestErrorCode.FRIEND_REQUEST_NOT_FOUND);

        deleteFriendRequestBySeq(chk.getRequestSeq());
        friendService.deleteFriends(u1, u2);
    }


    // =============== 레포지토리에 직접 접근하는 메소드들 ===============

    public Optional<FriendRequest> findFriendRequestBySeq(Long seq) {
        return Optional.ofNullable(seq)
                .flatMap(friendRequestRepository::findById);
    }

    public Optional<FriendRequest> findFriendRequestByUserAndFriend(User u1, User u2) {
        return Optional.ofNullable(u1)
                .flatMap(user1 -> Optional.ofNullable(u2)
                        .flatMap(user2 -> friendRequestRepository.findByOwnerAndFriend(user1, user2))
                );
    }

    private void deleteFriendRequestBySeq(Long requestSeq) {
        friendRequestRepository.deleteById(requestSeq);
    }

    // =========================================================

    private boolean isRequested(FriendRequest request) {
        return request.getStatus() == FriendRequestStatus.REQUESTED;
    }
    private boolean isAccepted(FriendRequest request) {
        return request.getStatus() == FriendRequestStatus.ACCEPTED;
    }

    private boolean isTarget(Long userSeq, FriendRequest request) {
        return userSeq.equals(request.getFriend().getUserSeq());
    }
}
