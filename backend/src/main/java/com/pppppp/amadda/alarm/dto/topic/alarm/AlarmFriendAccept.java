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
public class AlarmFriendAccept extends BaseTopicValue {

    private Long friendUserSeq;
    private String friendUserName;

    @JsonIgnore
    @Builder
    private AlarmFriendAccept(Long friendUserSeq, String friendUserName, Long relatedSeq) {
        super(relatedSeq);
        this.friendUserSeq = friendUserSeq;
        this.friendUserName = friendUserName;
    }

    @JsonIgnore
    public static AlarmFriendAccept create(Long friendUserSeq, String friendUserName,
        Long relatedSeq) {
        return AlarmFriendAccept.builder()
            .friendUserSeq(friendUserSeq)
            .friendUserName(friendUserName)
            .relatedSeq(relatedSeq)
            .build();
    }
}
