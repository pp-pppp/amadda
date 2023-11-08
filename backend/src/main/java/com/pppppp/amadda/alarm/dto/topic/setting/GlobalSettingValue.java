package com.pppppp.amadda.alarm.dto.topic.setting;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pppppp.amadda.alarm.dto.topic.BaseTopicValue;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GlobalSettingValue extends BaseTopicValue {

    private boolean isOn;

    @JsonIgnore
    @Builder
    private GlobalSettingValue(boolean isOn) {
        this.isOn = isOn;
    }
}
