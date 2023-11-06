package com.pppppp.amadda.alarm.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pppppp.amadda.ControllerTestSupport;
import com.pppppp.amadda.alarm.dto.request.AlarmConfigRequest;
import com.pppppp.amadda.alarm.entity.AlarmConfig;
import com.pppppp.amadda.alarm.entity.AlarmType;
import com.pppppp.amadda.alarm.service.AlarmService;
import com.pppppp.amadda.alarm.service.KafkaProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

class AlarmControllerTest extends ControllerTestSupport {

    @MockBean
    private KafkaProducer kafkaProducer;

    @MockBean
    private AlarmService alarmService;

    @DisplayName("알림 읽기")
    @Test
    void readAlarm() throws Exception {
        mockMvc.perform(
                post("/api/alarm/1")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").value("OK"));
    }

    @DisplayName("글로벌 설정이 가능한 알림에 대해 On")
    @ParameterizedTest
    @ValueSource(strings = {"FRIEND_REQUEST", "FRIEND_ACCEPT", "SCHEDULE_ASSIGNED", "MENTIONED",
        "SCHEDULE_UPDATE"})
    void setGlobalAlarmOn(String type) throws Exception {
        // given
        AlarmType alarmType = AlarmType.of(type);
        AlarmConfigRequest request = AlarmConfigRequest.builder()
            .userSeq(1L).alarmType(alarmType).build();

        // stubbing
        when(alarmService.setGlobalAlarm(any(), eq(true)))
            .thenReturn(AlarmConfig.builder().alarmType(alarmType).build());

        // when + then
        mockMvc.perform(
                post("/api/alarm/subscribe")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").value(String.format("%s 알림을 설정합니다.", alarmType.getContent())));
    }

    @DisplayName("글로벌 설정이 가능한 알림에 대해 Off")
    @ParameterizedTest
    @ValueSource(strings = {"FRIEND_REQUEST", "FRIEND_ACCEPT", "SCHEDULE_ASSIGNED", "MENTIONED",
        "SCHEDULE_UPDATE"})
    void setGlobalAlarmOff(String type) throws Exception {
        // given
        AlarmType alarmType = AlarmType.of(type);
        AlarmConfigRequest request = AlarmConfigRequest.builder()
            .userSeq(1L).alarmType(alarmType).build();

        // stubbing
        when(alarmService.setGlobalAlarm(any(), eq(false)))
            .thenReturn(AlarmConfig.builder().alarmType(alarmType).build());

        // when + then
        mockMvc.perform(
                post("/api/alarm/unsubscribe")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").value(String.format("%s 알림이 해제되었습니다.", alarmType.getContent())));
    }

}
