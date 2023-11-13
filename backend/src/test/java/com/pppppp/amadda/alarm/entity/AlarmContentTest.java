package com.pppppp.amadda.alarm.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pppppp.amadda.IntegrationTestSupport;
import java.util.MissingFormatArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AlarmContentTest extends IntegrationTestSupport {

    @DisplayName("friend_request")
    @Test
    void friend_request() {
        String expected = "정예지 님이 친구신청을 했어요.";
        String actual = AlarmContent.FRIEND_REQUEST.getMessage("정예지");
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("friend_accept")
    @Test
    void friend_accept() {
        String expected = "정예지 님이 친구신청을 수락했어요.";
        String actual = AlarmContent.FRIEND_ACCEPT.getMessage("정예지");
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("schedule_assigned")
    @Test
    void schedule_assigned() {
        String expected = "정예지 님이 '밥먹자' 일정을 추가했어요.";
        String actual = AlarmContent.SCHEDULE_ASSIGNED.getMessage("정예지", "밥먹자");
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("mentioned")
    @Test
    void mentioned() {
        String expected = "정예지 님이 '밥먹자' 일정에서 당신을 멘션했어요.";
        String actual = AlarmContent.MENTIONED.getMessage("정예지", "밥먹자");
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("schedule_update")
    @Test
    void schedule_update() {
        String expected = "'밥먹자' 일정이 수정되었어요.";
        String actual = AlarmContent.SCHEDULE_UPDATE.getMessage("밥먹자");
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("schedule_notification")
    @Test
    void schedule_notification() {
        String expected = "'밥먹자' 일정이 한 시간 뒤에 예정되어 있어요.";
        String actual = AlarmContent.SCHEDULE_NOTIFICATION.getMessage("밥먹자", "한 시간 뒤에");
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("파라미터가 1개인데 0개가 들어왔을 때")
    @Test
    void no_parameter() {
        assertThatThrownBy(AlarmContent.FRIEND_REQUEST::getMessage)
            .isInstanceOf(MissingFormatArgumentException.class);
    }

    @DisplayName("파라미터가 2개인데 1개가 들어왔을 때")
    @Test
    void not_enough_parameter() {
        assertThatThrownBy(AlarmContent.MENTIONED::getMessage)
            .isInstanceOf(MissingFormatArgumentException.class);
    }
}
