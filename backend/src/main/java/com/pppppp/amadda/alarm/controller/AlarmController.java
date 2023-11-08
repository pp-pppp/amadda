package com.pppppp.amadda.alarm.controller;

import com.pppppp.amadda.alarm.dto.request.AlarmConfigRequest;
import com.pppppp.amadda.alarm.dto.response.AlarmReadResponse;
import com.pppppp.amadda.alarm.entity.AlarmConfig;
import com.pppppp.amadda.alarm.service.AlarmService;
import com.pppppp.amadda.global.dto.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alarm")
@RequiredArgsConstructor
@Slf4j
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping
    public ApiResponse<List<AlarmReadResponse>> readAlarms() {
        Long userSeq = 1L;
        List<AlarmReadResponse> alarms = alarmService.readAlarms(userSeq);
        return ApiResponse.of(HttpStatus.OK, alarms);
    }

    @PostMapping("/{alarmSeq}")
    public ApiResponse<String> readAlarm(@PathVariable(required = true) Long alarmSeq) {
        Long userSeq = 1L;
        alarmService.readAlarm(alarmSeq, userSeq);
        return ApiResponse.ok("해당 알림을 읽음 처리하였습니다.");
    }

    @PostMapping("/subscribe")
    public ApiResponse<String> subscribeAlarm(@Valid @RequestBody AlarmConfigRequest request) {
        AlarmConfig alarmConfig = alarmService.setGlobalAlarm(request, true);
        return ApiResponse.ok(
            String.format("%s 알림을 설정합니다.", alarmConfig.getAlarmType().getContent()));
    }

    @PostMapping("/unsubscribe")
    public ApiResponse<String> unsubscribeAlarm(@Valid @RequestBody AlarmConfigRequest request) {
        AlarmConfig alarmConfig = alarmService.setGlobalAlarm(request, false);
        return ApiResponse.ok(
            String.format("%s 알림이 해제되었습니다.", alarmConfig.getAlarmType().getContent()));
    }

}
