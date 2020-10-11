package com.cloudator.restapp.forecastweather.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;

import java.time.LocalDateTime;

/**
 * Created by Bobur on 11.10.2020
 */
public class MagnitudeInfo {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss XXX", lenient = OptBoolean.TRUE)
    private LocalDateTime datetime;

    private MagnitudeData data;

    public LocalDateTime getDatetime() {
        return this.datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public MagnitudeData getData() {
        return this.data;
    }

    public void setData(MagnitudeData data) {
        this.data = data;
    }
}
