package com.pppppp.amadda.alarm.dto.topic.setting;

import com.pppppp.amadda.alarm.dto.topic.BaseTopicValue;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GlobalSettingValue extends BaseTopicValue {

    private boolean isOn;

    @Builder
    private GlobalSettingValue(boolean isOn) {
        this.isOn = isOn;
    }
}
