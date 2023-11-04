package com.pppppp.amadda.alarm.service;

import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmFriendAccept;
import com.pppppp.amadda.alarm.dto.topic.alarm.AlarmFriendRequest;
import com.pppppp.amadda.alarm.entity.KafkaTopic;
import com.pppppp.amadda.friend.entity.FriendRequest;
import com.pppppp.amadda.friend.repository.FriendRequestRepository;
import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.FriendRequestErrorCode;
import com.pppppp.amadda.global.entity.exception.errorcode.UserErrorCode;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmService {

    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final KafkaProducer kafkaProducer;

    @Transactional
    public void sendFriendRequest(Long ownerSeq, Long friendSeq) {
        User owner = getUser(ownerSeq);
        User friend = getUser(friendSeq);
        FriendRequest friendRequest = getFriendRequest(owner, friend);

        AlarmFriendRequest value = AlarmFriendRequest.create(friendRequest.getRequestSeq(),
            owner.getUserSeq(), owner.getUserName());

        kafkaProducer.sendAlarm(KafkaTopic.ALARM_FRIEND_REQUEST, friend.getUserSeq(), value);
    }

    @Transactional
    public void sendFriendAccept(Long ownerSeq, Long friendSeq) {
        User owner = getUser(ownerSeq);
        User friend = getUser(friendSeq);

        AlarmFriendAccept value = AlarmFriendAccept.create(friend.getUserSeq(),
            friend.getUserName());

        kafkaProducer.sendAlarm(KafkaTopic.ALARM_FRIEND_ACCEPT, owner.getUserSeq(), value);
    }

    private User getUser(Long ownerSeq) {
        return userRepository.findByUserSeq(ownerSeq)
            .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
    }

    private FriendRequest getFriendRequest(User owner, User friend) {
        return friendRequestRepository.findByOwnerAndFriend(owner, friend)
            .orElseThrow(() -> new RestApiException(
                FriendRequestErrorCode.FRIEND_REQUEST_NOT_FOUND));
    }

}
