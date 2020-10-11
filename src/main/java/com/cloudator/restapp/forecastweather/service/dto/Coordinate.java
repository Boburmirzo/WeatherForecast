package com.cloudator.restapp.forecastweather.service.dto;

/**
 * Created by Bobur on 11.10.2020
 */
public class Coordinate {
    private double lat;
    private double lon;

    public double getLat() {
        return this.lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return this.lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
