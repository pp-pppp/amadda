package com.pppppp.amadda.alarm.service;

import com.pppppp.amadda.alarm.repository.AlarmConfigRepository;
import com.pppppp.amadda.alarm.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final AlarmConfigRepository alarmConfigRepository;

}
