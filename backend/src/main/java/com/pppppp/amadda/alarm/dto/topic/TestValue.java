package com.pppppp.amadda.alarm.dto.topic;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class TestValue extends BaseTopicValue {

    private String value;

    @JsonIgnore
    @Builder
    private TestValue(String value) {
        this.value = value;
    }
}
