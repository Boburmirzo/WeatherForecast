package com.cloudator.restapp.forecastweather.service.dto;

/**
 * Created by Bobur on 11.10.2020
 */
public class MagnitudeData {
    private double temperature;

    private double pressure;

    public double getTemperature() {
        return this.temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getPressure() {
        return this.pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }
}
