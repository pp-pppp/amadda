package com.pppppp.amadda.alarm.entity;

public class KafkaTopic {

    public static final String ALARM_FRIEND_REQUEST = "${spring.kafka.topic.alarm.friend-request}";
    public static final String ALARM_FRIEND_ACCEPT = "${spring.kafka.topic.alarm.friend-accept}";
    public static final String ALARM_SCHEDULE_ASSIGNED = "${spring.kafka.topic.alarm.schedule-assigned}";
    public static final String ALARM_MENTIONED = "${spring.kafka.topic.alarm.mentioned}";
    public static final String ALARM_SCHEDULE_UPDATE = "${spring.kafka.topic.alarm.schedule-update}";
    public static final String ALARM_SCHEDULE_NOTIFICATION = "${spring.kafka.topic.alarm.schedule-notification}";

    public static final String SETTING_GLOBAL_FRIEND_REQUEST = "setting.global.friend-request";
    public static final String SETTING_GLOBAL_FRIEND_ACCEPT = "setting.global.friend-accept";
    public static final String SETTING_GLOBAL_SCHEDULE_ASSIGNED = "setting.global.schedule-assigned";
    public static final String SETTING_GLOBAL_MENTIONED = "setting.global.mentioned";
    public static final String SETTING_GLOBAL_SCHEDULE_UPDATE = "setting.global.schedule-update";

    public static final String SETTING_LOCAL_MENTIONED = "setting.local.mentioned";
    public static final String SETTING_LOCAL_SCHEDULE_UPDATE = "setting.local.schedule-update";

    public static final String NAME_SCHEDULE = "name.schedule";
    public static final String NAME_USER = "name.user";
}
