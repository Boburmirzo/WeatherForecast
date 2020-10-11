package com.cloudator.restapp.forecastweather.utils;

/**
 * Created by Bobur on 11.10.2020.
 */
public interface EnumConverter<E extends Enum<E> & EnumConverter<E>> {

    byte getId();

    E convert(byte val);

    String getCode();

    E convert(String code);
}
