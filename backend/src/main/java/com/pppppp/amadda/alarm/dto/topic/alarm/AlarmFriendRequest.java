package com.pppppp.amadda.alarm.dto.topic.alarm;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pppppp.amadda.alarm.dto.topic.BaseTopicValue;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AlarmFriendRequest extends BaseTopicValue {

    private Long friendRequestSeq;
    private Long requestedUserSeq;
    private String requestedUserName;

    @JsonIgnore
    @Builder
    private AlarmFriendRequest(Long friendRequestSeq, Long requestedUserSeq,
        String requestedUserName) {
        this.friendRequestSeq = friendRequestSeq;
        this.requestedUserSeq = requestedUserSeq;
        this.requestedUserName = requestedUserName;
    }

    public static AlarmFriendRequest create(Long friendRequestSeq, Long requestedUserSeq,
        String requestedUserName) {
        return AlarmFriendRequest.builder()
            .friendRequestSeq(friendRequestSeq)
            .requestedUserSeq(requestedUserSeq)
            .requestedUserName(requestedUserName)
            .build();
    }
}
