package com.pppppp.amadda.alarm.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class KafkaTopic {

    public String ALARM_FRIEND_REQUEST;
    public String ALARM_FRIEND_ACCEPT;
    public String ALARM_SCHEDULE_ASSIGNED;
    public String ALARM_MENTIONED;
    public String ALARM_SCHEDULE_UPDATE;
    public String ALARM_SCHEDULE_NOTIFICATION;
    
    private Environment environment;

    @Autowired
    public KafkaTopic(Environment environment) {
        this.environment = environment;
        initializeConstants();
    }

    private void initializeConstants() {
        ALARM_FRIEND_REQUEST = environment.getProperty("spring.kafka.topic.alarm.friend-request");
        ALARM_FRIEND_ACCEPT = environment.getProperty("spring.kafka.topic.alarm.friend-accept");
        ALARM_SCHEDULE_ASSIGNED = environment.getProperty(
            "spring.kafka.topic.alarm.schedule-assigned");
        ALARM_MENTIONED = environment.getProperty("spring.kafka.topic.alarm.mentioned");
        ALARM_SCHEDULE_UPDATE = environment.getProperty("spring.kafka.topic.alarm.schedule-update");
        ALARM_SCHEDULE_NOTIFICATION = environment.getProperty(
            "spring.kafka.topic.alarm.schedule-notification");
    }
}
