package com.pppppp.amadda.alarm.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pppppp.amadda.ControllerTestSupport;
import com.pppppp.amadda.alarm.dto.request.AlarmConfigRequest;
import com.pppppp.amadda.alarm.entity.AlarmConfig;
import com.pppppp.amadda.alarm.entity.AlarmType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;

@EmbeddedKafka(
    partitions = 1,
    brokerProperties = {
        "listeners=PLAINTEXT://localhost:9093",
        "port=9093"
    }
)
class AlarmControllerTest extends ControllerTestSupport {

    @DisplayName("알림 목록 가져오기")
    @Test
    void readAlarms() throws Exception {
        mockMvc.perform(
                get("/api/alarm")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

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
            .andExpect(jsonPath("$.data").value("해당 알림을 읽음 처리하였습니다."));
    }

    @DisplayName("글로벌 설정이 가능한 알림에 대해 On")
    @ParameterizedTest
    @ValueSource(strings = {"FRIEND_REQUEST", "FRIEND_ACCEPT", "SCHEDULE_ASSIGNED", "MENTIONED",
        "SCHEDULE_UPDATE"})
    void setGlobalAlarmOn(String type) throws Exception {
        // given
        AlarmType alarmType = AlarmType.of(type);
        AlarmConfigRequest request = AlarmConfigRequest.builder().alarmType(alarmType).build();

        // stubbing
        given(alarmService.setGlobalAlarm(anyLong(), any(), eq(true)))
            .willReturn(AlarmConfig.builder().alarmType(alarmType).build());

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
            .andExpect(
                jsonPath("$.data").value(String.format("%s 알림을 설정합니다.", alarmType.getContent())));
    }

    @DisplayName("글로벌 설정이 가능한 알림에 대해 Off")
    @ParameterizedTest
    @ValueSource(strings = {"FRIEND_REQUEST", "FRIEND_ACCEPT", "SCHEDULE_ASSIGNED", "MENTIONED",
        "SCHEDULE_UPDATE"})
    void setGlobalAlarmOff(String type) throws Exception {
        // given
        AlarmType alarmType = AlarmType.of(type);
        AlarmConfigRequest request = AlarmConfigRequest.builder().alarmType(alarmType).build();

        // stubbing
        given(alarmService.setGlobalAlarm(anyLong(), any(), eq(false)))
            .willReturn(AlarmConfig.builder().alarmType(alarmType).build());

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
            .andExpect(
                jsonPath("$.data").value(String.format("%s 알림이 해제되었습니다.", alarmType.getContent())));
    }

}
