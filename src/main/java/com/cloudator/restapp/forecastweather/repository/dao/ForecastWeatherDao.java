package com.cloudator.restapp.forecastweather.repository.dao;

import com.cloudator.restapp.forecastweather.model.ForecastWeatherMetrics;
import com.cloudator.restapp.forecastweather.service.exception.IntegrationException;

import java.util.List;

/**
 * Created by Bobur on 11.10.2020
 */
public interface ForecastWeatherDao {

    /**
     * Retrieves all weather metrics for a city from memory
     *
     * @return forecast weather metrics for a given city
     * @throws IntegrationException if any exception is found, with a message containing contextual information of the error and the root exception
     */
    ForecastWeatherMetrics findByCiyId(Long cityId) throws IntegrationException;

    /**
     * Save all forecast weather metrics
     *
     * @param forecastWeatherMetrics forecast weather metrics
     * @throws IntegrationException if any exception is found, with a message containing contextual information of the error and the root exception
     */
    void saveAllForecastWeatherMetrics(List<ForecastWeatherMetrics> forecastWeatherMetrics) throws IntegrationException;

    /**
     * Get forecast weather metrics for all cities
     *
     * @throws IntegrationException if any exception is found, with a message containing contextual information of the error and the root exception
     */
    List<ForecastWeatherMetrics> findAllForecastWeatherMetricsForAllCities(List<Long> cityIds) throws IntegrationException;

}
