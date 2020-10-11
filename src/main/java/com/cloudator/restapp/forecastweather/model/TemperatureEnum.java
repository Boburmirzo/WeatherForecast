package com.cloudator.restapp.forecastweather.model;

import com.cloudator.restapp.forecastweather.utils.EnumConverter;
import com.cloudator.restapp.forecastweather.utils.ReverseEnumMap;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;

/**
 * Created by Bobur on 11.10.2020
 */
@JsonSerialize(using = EnumSerializer.class)
public enum TemperatureEnum implements EnumConverter<TemperatureEnum> {
    CELSIUS(1, "Celsius"),
    KELVIN(2, "Kelvin"),
    FAHRENHEIT(3, "Fahrenheit");

    private static final ReverseEnumMap<TemperatureEnum> map = new ReverseEnumMap<>(TemperatureEnum.class);
    private final byte id;
    private final String code;

    TemperatureEnum(int id, String code) {
        this.id = (byte) id;
        this.code = code;
    }

    @Override
    public byte getId() {
        return id;
    }

    @Override
    public String getCode() {
        return code;
    }


    @Override
    public TemperatureEnum convert(byte val) {
        return getFromValue(val);
    }

    @Override
    public TemperatureEnum convert(String code) {
        return getFromCode(code);
    }


    @JsonCreator
    public static TemperatureEnum getFromValue(int val) {
        return map.get((byte) val);
    }

    @JsonCreator
    public static TemperatureEnum getFromCode(String code) {
        return map.get(code);
    }

}
