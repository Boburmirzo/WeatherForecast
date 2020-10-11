package com.cloudator.restapp.forecastweather.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Bobur on 11.10.2020
 */
public class ForecastWeatherMetrics implements Serializable, Integrity {

    private static final long serialVersionUID = 3230403280527814469L;

    /**
     * City for which temperature we are checking
     */
    private final City city;

    /**
     * List of temperatures which exceeds limit
     */
    private List<Magnitude> dailyTemperatures;

    public ForecastWeatherMetrics(City city) {
        this.city = city;
    }

    public City getCity() {
        return city;
    }

    public List<Magnitude> getDailyTemperatures() {
        return dailyTemperatures;
    }

    public void setDailyTemperatures(List<Magnitude> dailyTemperatures) {
        this.dailyTemperatures = dailyTemperatures;
    }

    @JsonIgnore
    @Override
    public boolean isValid() {
        return dailyTemperatures != null && !dailyTemperatures.isEmpty();
    }

    @Override
    public String toString() {
        return "ForecastWeatherMetrics{" + "city=" + city +
                ", dailyTemperatures=" + dailyTemperatures +
                '}';
    }
}
