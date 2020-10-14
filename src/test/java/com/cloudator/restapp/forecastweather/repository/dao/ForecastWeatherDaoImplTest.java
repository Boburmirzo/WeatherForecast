package com.cloudator.restapp.forecastweather.repository.dao;


import com.cloudator.restapp.forecastweather.model.City;
import com.cloudator.restapp.forecastweather.model.ForecastWeatherMetrics;
import com.cloudator.restapp.forecastweather.model.Magnitude;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cloudator.restapp.forecastweather.model.TemperatureEnum.CELSIUS;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test case for validating the proper functionality of ForecastWeatherDaoImpl
 * Created by Bobur on 14.10.2020
 */
@RunWith(SpringRunner.class)
public class ForecastWeatherDaoImplTest {

    @Spy
    @InjectMocks
    private ForecastWeatherDaoImpl forecastWeatherDao;

    @Mock
    private RedisTemplate<Long, ForecastWeatherMetrics> redisTemplate;

    @Mock
    private ValueOperations<Long, ForecastWeatherMetrics> valueOperations;

    @Test
    public void testSaveAllCitiesForecastDataToMemory() {
        City city = new City();
        city.setId(4517009L);
        city.setName("London");
        city.setLimitTemperature(10);

        Magnitude magnitude = new Magnitude(CELSIUS.getCode(), 23);
        magnitude.setFrom(LocalDateTime.now());
        magnitude.setTo(LocalDateTime.now());
        List<Magnitude> magnitudes = new ArrayList<>();
        magnitudes.add(magnitude);

        ForecastWeatherMetrics forecastWeatherMetric = new ForecastWeatherMetrics(city);
        forecastWeatherMetric.setDailyTemperatures(magnitudes);

        List<ForecastWeatherMetrics> forecastWeatherMetrics = new ArrayList<>();
        forecastWeatherMetrics.add(forecastWeatherMetric);

        Map<Long, ForecastWeatherMetrics> forecastWeatherMetricsMap = new HashMap<>();
        forecastWeatherMetricsMap.put(city.getId(), forecastWeatherMetric);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        forecastWeatherDao.saveAllForecastWeatherMetrics(forecastWeatherMetrics);

        verify(redisTemplate, times(1)).opsForValue();
        verify(valueOperations, times(1)).multiSet(forecastWeatherMetricsMap);
    }

    @Test
    public void testFindForecastWeatherByCiyId() {
        City city = new City();
        city.setId(4517009L);
        city.setName("London");
        city.setLimitTemperature(10);

        Magnitude magnitude = new Magnitude(CELSIUS.getCode(), 23);
        magnitude.setFrom(LocalDateTime.now());
        magnitude.setTo(LocalDateTime.now());
        List<Magnitude> magnitudes = new ArrayList<>();
        magnitudes.add(magnitude);

        ForecastWeatherMetrics forecastWeatherMetric = new ForecastWeatherMetrics(city);
        forecastWeatherMetric.setDailyTemperatures(magnitudes);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(city.getId())).thenReturn(forecastWeatherMetric);

        ForecastWeatherMetrics forecastWeatherMetricFound = forecastWeatherDao.findByCiyId(city.getId());

        verify(redisTemplate, times(1)).opsForValue();
        verify(valueOperations, times(1)).get(city.getId());

        Assert.assertNotNull(forecastWeatherMetricFound);
        Assert.assertEquals(city.getId(), forecastWeatherMetric.getCity().getId());
    }

    @Test
    public void testFindAllForecastWeatherMetricsForAllCities() {
        // Given
        City city = new City();
        city.setId(4517009L);
        city.setName("London");
        city.setLimitTemperature(10);

        // Prepare cities
        List<City> cities = new ArrayList<>();
        cities.add(city);

        Magnitude magnitude = new Magnitude(CELSIUS.getCode(), 23);
        magnitude.setFrom(LocalDateTime.now());
        magnitude.setTo(LocalDateTime.now());
        List<Magnitude> magnitudes = new ArrayList<>();
        magnitudes.add(magnitude);

        ForecastWeatherMetrics forecastWeatherMetric = new ForecastWeatherMetrics(city);
        forecastWeatherMetric.setDailyTemperatures(magnitudes);

        List<ForecastWeatherMetrics> forecastWeatherMetrics = new ArrayList<>();
        forecastWeatherMetrics.add(forecastWeatherMetric);

        List<Long> cityIds = cities.stream().map(City::getId).collect(Collectors.toList());

        // When
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.multiGet(cityIds)).thenReturn(forecastWeatherMetrics);

        List<ForecastWeatherMetrics> forecastWeatherMetricsFound = forecastWeatherDao.findAllForecastWeatherMetricsForAllCities(cityIds);

        // Then
        Assert.assertNotNull(forecastWeatherMetricsFound);
        Assert.assertEquals(1, forecastWeatherMetricsFound.size());
    }
}
