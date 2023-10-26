package com.pppppp.amadda.friend.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FriendRequestStatus {
    REQUESTED("친구요청 보냄"),
    ACCEPTED("친구요청 수락"),
    DECLINED("친구요청 거절");

    private final String text;
}
