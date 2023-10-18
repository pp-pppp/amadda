package com.pppppp.amadda.alarm.controller;

import com.pppppp.amadda.alarm.service.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alarm")
@RequiredArgsConstructor
@Slf4j
public class AlarmController {

    private final AlarmService alarmService;

    @PostMapping("/subscribe")
    public void subscribeAlarm() {
        log.info("POST /api/alarm/subscribe");
    }

    @PostMapping("/unsubscribe")
    public void unsubscribeAlarm() {
        log.info("POST /api/alarm/unsubscribe");
    }

}
