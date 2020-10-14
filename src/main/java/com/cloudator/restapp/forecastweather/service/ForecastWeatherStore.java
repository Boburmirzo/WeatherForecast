package com.cloudator.restapp.forecastweather.service;


import com.cloudator.restapp.forecastweather.model.City;
import com.cloudator.restapp.forecastweather.model.ForecastWeatherMetrics;
import com.cloudator.restapp.forecastweather.service.exception.BusinessException;
import com.cloudator.restapp.forecastweather.service.exception.IntegrationException;

import java.util.List;

/**
 * This service for restoring the information from memory
 * Created by Bobur on 13.10.2020.
 */
public interface ForecastWeatherStore {

    /**
     * Gets the forecast weather metrics for the given city
     *
     * @param city a City instance
     * @return a ForecastWeatherMetrics instance containing the required data
     * @throws BusinessException if any exception is found, with a message containing contextual information of the error and the root exception
     */
    ForecastWeatherMetrics getForecastWeatherMetricsForCity(City city) throws BusinessException;

    /**
     * Gets the forecast weather metrics for all cities in config
     *
     * @return a ForecastWeatherMetrics instance containing the required data
     * @throws BusinessException if any exception is found, with a message containing contextual information of the error and the root exception
     */
    List<ForecastWeatherMetrics> getForecastWeatherMetricsForAllCities() throws BusinessException;

    /**
     * Retrieves all cities in Json file
     *
     * @return list of cities available for weather forecast
     * @throws IntegrationException if any exception is found, with a message containing contextual information of the error and the root exception
     */
    List<City> findAllCities();

    /**
     * Save all forecast weather metrics
     *
     * @param forecastWeatherMetrics forecast weather metrics
     * @throws IntegrationException if any exception is found, with a message containing contextual information of the error and the root exception
     */
    void saveAllForecastWeatherMetrics(List<ForecastWeatherMetrics> forecastWeatherMetrics) throws IntegrationException;
}
