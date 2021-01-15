package com.aftership.tracking.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CourierCodeEnum {
    FEDEX("FedEx"), UPS("UPS"), USPS("USPS");

    private String value;

    private CourierCodeEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getCourierCodeValue() {
        return value;
    }

    public static List<String> getValues() {
        return Arrays.stream(CourierCodeEnum.values()).map(CourierCodeEnum::getCourierCodeValue).collect(Collectors.toList());
    }

}
