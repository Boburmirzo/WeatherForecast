package com.cloudator.restapp.forecastweather.repository.dao;

import com.cloudator.restapp.forecastweather.model.ForecastWeatherMetrics;
import com.cloudator.restapp.forecastweather.service.exception.IntegrationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Bobur on 13.10.2020
 */
@Repository
public class ForecastWeatherDaoImpl implements ForecastWeatherDao {

    private final RedisTemplate<Long, ForecastWeatherMetrics> redisTemplate;

    public ForecastWeatherDaoImpl(RedisTemplate<Long, ForecastWeatherMetrics> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Retrieves all the cities from the underlying repository
     *
     * @return a List of Cities instances
     * @throws IntegrationException if any exception is found, with a message containing contextual information of the error and the root exception
     */
    @Override
    public ForecastWeatherMetrics findByCiyId(Long cityId) throws IntegrationException {

        if (cityId == null) {
            throw new IntegrationException("No city id is given");
        }

        try {
            return redisTemplate.opsForValue().get(cityId);

        } catch (Exception e) {
            throw new IntegrationException("Exception encountered invoking findAllCities", e);
        }
    }

    @Override
    public void saveAllForecastWeatherMetrics(List<ForecastWeatherMetrics> forecastWeatherMetrics) throws IntegrationException {
        try {
            Map<Long, ForecastWeatherMetrics> forecastWeatherMetricsMap = forecastWeatherMetrics
                    .stream()
                    .collect(Collectors
                            .toMap(forecastWeatherMetric -> forecastWeatherMetric.getCity().getId(), forecastWeatherMetric -> forecastWeatherMetric));

            redisTemplate.opsForValue().multiSet(forecastWeatherMetricsMap);
        } catch (Exception e) {
            throw new IntegrationException("Exception encountered while saving all forecast weather metrics", e);
        }
    }

    @Override
    public List<ForecastWeatherMetrics> findAllForecastWeatherMetricsForAllCities(List<Long> cityIds) throws IntegrationException {
        try {
            return redisTemplate.opsForValue().multiGet(cityIds);
        } catch (Exception e) {
            throw new IntegrationException("Exception encountered while saving all forecast weather metrics", e);
        }
    }

}
