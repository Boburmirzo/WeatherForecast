package com.cloudator.restapp.forecastweather.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by Bobur on 11.10.2020
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Limit implements Serializable {

    private static final long serialVersionUID = 8483342465856521524L;

    /**
     * Temperature limit as an input
     */
    private Double limitTemperature;

    /**
     * Limit day to get data for specific days, by default it is 5 days
     */
    private Integer limitDay;

    public Double getLimitTemperature() {
        return limitTemperature;
    }

    public void setLimitTemperature(Double limitTemperature) {
        this.limitTemperature = limitTemperature;
    }

    public Integer getLimitDay() {
        return limitDay;
    }

    public void setLimitDay(Integer limitDay) {
        this.limitDay = limitDay;
    }
}
