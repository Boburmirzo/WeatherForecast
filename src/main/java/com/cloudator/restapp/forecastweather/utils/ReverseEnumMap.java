package com.cloudator.restapp.forecastweather.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bobur on 11.10.2020.
 */
public class ReverseEnumMap<V extends Enum<V> & EnumConverter> {
    private Map<Byte, V> map = new HashMap<Byte, V>();

    public ReverseEnumMap(Class<V> valueType) {
        for (V v : valueType.getEnumConstants()) {
            map.put(v.getId(), v);
        }
    }

    public V get(byte num) {
        return map.get(num);
    }

    public V get(String code) {
        for (Map.Entry<Byte, V> entry : map.entrySet()) {
            if (entry == null || entry.getValue() == null) {
                return null;
            }
            if (code != null && code.equalsIgnoreCase(entry.getValue().getCode())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
