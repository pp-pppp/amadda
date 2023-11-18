package com.pppppp.amadda.alarm.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pppppp.amadda.IntegrationTestSupport;
import java.util.MissingFormatArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AlarmContentTest extends IntegrationTestSupport {

    @DisplayName("친구 신청 토픽의 메시지를 검토한다.")
    @Test
    void friend_request() {
        String expected = "정예지 님이 친구신청을 했어요.";
        String actual = AlarmContent.FRIEND_REQUEST.getMessage("정예지");
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("친구 신청 수락 토픽의 메시지를 검토한다.")
    @Test
    void friend_accept() {
        String expected = "정예지 님이 친구신청을 수락했어요.";
        String actual = AlarmContent.FRIEND_ACCEPT.getMessage("정예지");
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("일정 할당 토픽의 메시지를 검토한다.")
    @Test
    void schedule_assigned() {
        String expected = "정예지 님이 '밥먹자' 일정을 추가했어요.";
        String actual = AlarmContent.SCHEDULE_ASSIGNED.getMessage("정예지", "밥먹자");
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("댓글 멘션 토픽의 메시지를 검토한다.")
    @Test
    void mentioned() {
        String expected = "정예지 님이 '밥먹자' 일정에서 당신을 멘션했어요.";
        String actual = AlarmContent.MENTIONED.getMessage("정예지", "밥먹자");
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("일정 수정 토픽의 메시지를 검토한다.")
    @Test
    void schedule_update() {
        String expected = "'밥먹자' 일정이 수정되었어요.";
        String actual = AlarmContent.SCHEDULE_UPDATE.getMessage("밥먹자");
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("일정 예정 알림 토픽의 메시지를 검토한다.")
    @Test
    void schedule_notification() {
        String expected = "'밥먹자' 일정이 한 시간 뒤에 예정되어 있어요.";
        String actual = AlarmContent.SCHEDULE_NOTIFICATION.getMessage("밥먹자", "한 시간 뒤에");
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("친구 신청 알림 토픽 메시지 생성 시 파라미터가 부족한 경우 예외가 발생한다.")
    @Test
    void no_parameter() {
        assertThatThrownBy(AlarmContent.FRIEND_REQUEST::getMessage)
            .isInstanceOf(MissingFormatArgumentException.class);
    }

    @DisplayName("댓글 멘션 알림 토픽 메시지 생성 시 파라미터가 부족한 경우 예외가 발생한다.")
    @Test
    void not_enough_parameter() {
        assertThatThrownBy(AlarmContent.MENTIONED::getMessage)
            .isInstanceOf(MissingFormatArgumentException.class);
    }
}
