package com.cloudator.restapp.forecastweather.service;

import com.cloudator.restapp.forecastweather.model.City;
import com.cloudator.restapp.forecastweather.model.ForecastWeatherMetrics;

/**
 * Defines the contract to get ForecastWeatherMetrics from different providers
 * Created by Bobur on 11.10.2020
 */
public interface SourceProvider {

    /**
     * Gets the ForecastWeatherMetrics from a specific provider
     *
     * @param city The city parameter
     * @return a ForecastWeatherMetrics instance
     */
    ForecastWeatherMetrics getForecastWeatherMetrics(City city);
}
