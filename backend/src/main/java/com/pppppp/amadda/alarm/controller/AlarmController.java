package com.pppppp.amadda.alarm.controller;

import com.pppppp.amadda.alarm.dto.request.AlarmRequest;
import com.pppppp.amadda.alarm.service.AlarmService;
import com.pppppp.amadda.alarm.service.KafkaProducer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alarm")
@RequiredArgsConstructor
@Slf4j
public class AlarmController {

    private final KafkaProducer kafkaProducer;

    @PostMapping("/subscribe")
    public void subscribeAlarm(@Valid @RequestBody AlarmRequest request) {
        log.info("POST /api/alarm/subscribe");
        kafkaProducer.turnOnGlobalAlarm(request.alarmType(), request.userSeq());
    }

    @PostMapping("/unsubscribe")
    public void unsubscribeAlarm(@Valid @RequestBody AlarmRequest request) {
        log.info("POST /api/alarm/unsubscribe");
        kafkaProducer.turnOffGlobalAlarm(request.alarmType(), request.userSeq());
    }

}
