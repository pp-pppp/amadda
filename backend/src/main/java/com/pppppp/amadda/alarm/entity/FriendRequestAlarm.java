package com.pppppp.amadda.alarm.entity;

import com.pppppp.amadda.friend.entity.FriendRequest;
import com.pppppp.amadda.user.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("FRIEND_REQUEST")
public class FriendRequestAlarm extends Alarm {

    @ManyToOne
    @JoinColumn(name = "related_request_seq")
    private FriendRequest friendRequest;

    @Builder
    private FriendRequestAlarm(User user, String content, boolean isRead, AlarmType alarmType,
        FriendRequest friendRequest) {
        super(user, content, isRead, alarmType);
        this.friendRequest = friendRequest;
    }

    public static FriendRequestAlarm create(User user, String content, AlarmType alarmType,
        FriendRequest friendRequest) {
        return FriendRequestAlarm.builder()
            .user(user)
            .content(content)
            .isRead(false)
            .alarmType(alarmType)
            .friendRequest(friendRequest)
            .build();
    }

}
