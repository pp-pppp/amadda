package com.pppppp.amadda.friend.service;

import com.pppppp.amadda.alarm.service.AlarmService;
import com.pppppp.amadda.friend.dto.request.FriendRequestRequest;
import com.pppppp.amadda.friend.dto.response.FriendRequestResponse;
import com.pppppp.amadda.friend.entity.FriendRequest;
import com.pppppp.amadda.friend.entity.FriendRequestStatus;
import com.pppppp.amadda.friend.repository.FriendRequestRepository;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.FriendRequestErrorCode;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.service.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserService userService;
    private final FriendService friendService;
    private final GroupMemberService groupMemberService;
    private final AlarmService alarmService;

    @Transactional
    public FriendRequestResponse createFriendRequest(FriendRequestRequest request) {

        Long userSeq = request.userSeq();
        Long targetSeq = request.targetSeq();
        User u1 = userService.getUserInfoBySeq(userSeq);
        User u2 = userService.getUserInfoBySeq(targetSeq);

        // 이미 요청된(REQUESTED) 혹은 친구 상태(ACCEPTED)라면 예외처리
        FriendRequest chk = findFriendRequestByUserAndFriend(u1, u2)
            .orElse(findFriendRequestByUserAndFriend(u2, u1).orElse(null));
        if (chk != null && (isRequested(chk) || isAccepted(chk))) {
            throw new RestApiException(FriendRequestErrorCode.FRIEND_REQUEST_INVALID);
        }

        FriendRequest fr = FriendRequest.create(u1, u2);
        fr = friendRequestRepository.save(fr);

        alarmService.sendFriendRequest(u1.getUserSeq(), u2.getUserSeq());

        return FriendRequestResponse.of(fr);
    }

    @Transactional
    public void declineFriendRequest(Long userSeq, Long requestSeq) {

        FriendRequest chk = findFriendRequestBySeq(requestSeq).orElse(null);

        // userSeq == chk.friendSeq && REQUESTED 상태일때만 허용
        if (chk != null && isTarget(userSeq, chk) && isRequested(chk)) {
            chk.updateStatus(FriendRequestStatus.DECLINED);
            alarmService.readFriendRequestAlarm(requestSeq);
            deleteFriendRequestBySeq(chk.getRequestSeq());
        } else {
            throw new RestApiException(FriendRequestErrorCode.FRIEND_REQUEST_INVALID);
        }
    }

    @Transactional
    public FriendRequestResponse acceptFriendRequest(Long userSeq, Long requestSeq) {

        FriendRequest chk = findFriendRequestBySeq(requestSeq).orElse(null);

        // userSeq == chk.friendSeq && REQUESTED && 이미 친구가 아닌 상태일때만 허용
        if (chk != null) {
            User owner = chk.getOwner();
            User friend = chk.getFriend();
            if (isTarget(userSeq, chk) && isRequested(chk)
                && !friendService.isAlreadyFriends(owner, friend)) {

                chk.updateStatus(FriendRequestStatus.ACCEPTED);
                friendService.createFriend(FriendRequestResponse.of(chk));

                alarmService.sendFriendAccept(owner.getUserSeq(), friend.getUserSeq());
                alarmService.readFriendRequestAlarm(requestSeq);

                return FriendRequestResponse.of(chk);
            } else {
                throw new RestApiException(FriendRequestErrorCode.FRIEND_REQUEST_INVALID);
            }
        } else {
            throw new RestApiException(FriendRequestErrorCode.FRIEND_REQUEST_INVALID);
        }
    }

    @Transactional
    public void deleteFriendAndRequest(Long userSeq, Long friendSeq) {

        User u1 = userService.getUserInfoBySeq(userSeq);
        User u2 = userService.getUserInfoBySeq(friendSeq);

        FriendRequest chk = findFriendRequestByUserAndFriend(u1, u2)
            .orElse(findFriendRequestByUserAndFriend(u2, u1).orElse(null));
        if (chk == null) {
            throw new RestApiException(FriendRequestErrorCode.FRIEND_REQUEST_NOT_FOUND);
        }

        deleteFriendRequestBySeq(chk.getRequestSeq()); // 친구 신청 내역 삭제
        groupMemberService.deleteFriend(u1, u2); // 내 그룹에서 삭제
        friendService.deleteFriends(u1, u2); // 친구에서 삭제
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
