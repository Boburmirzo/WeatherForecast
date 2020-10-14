package com.cloudator.restapp.forecastweather.service;

import com.cloudator.restapp.forecastweather.model.City;
import com.cloudator.restapp.forecastweather.model.ForecastWeatherMetrics;
import com.cloudator.restapp.forecastweather.model.Magnitude;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.cloudator.restapp.forecastweather.model.TemperatureEnum.CELSIUS;
import static org.mockito.Mockito.when;

/**
 * Test case for validating the proper functionality of ForecastWeatherScheduler
 * Created by Bobur on 14.10.2020
 */
@RunWith(SpringRunner.class)
public class ForecastWeatherSchedulerTest {


    @Spy
    @InjectMocks
    private ForecastWeatherScheduler forecastWeatherScheduler;

    @Mock
    private ForecastWeatherService forecastWeatherService;

    @Mock
    private ForecastWeatherStore forecastWeatherStore;

    @Test
    public void forecastWeatherRetrieveScheduler() {
        //Given
        City city1 = new City();
        city1.setId(4517009L);
        city1.setName("London");
        city1.setLimitTemperature(10);

        City city2 = new City();
        city2.setId(2950159L);
        city2.setName("Berlin");
        city2.setLimitTemperature(10);

        List<City> cityList = new ArrayList<>();
        cityList.add(city1);
        cityList.add(city2);

        Magnitude magnitude = new Magnitude(CELSIUS.getCode(), 23);
        magnitude.setFrom(LocalDateTime.now());
        magnitude.setTo(LocalDateTime.now());
        List<Magnitude> magnitudes = new ArrayList<>();
        magnitudes.add(magnitude);

        //Create forecast weather metrics for two cities
        ForecastWeatherMetrics forecastWeatherMetric1 = new ForecastWeatherMetrics(city1);
        forecastWeatherMetric1.setDailyTemperatures(magnitudes);
        ForecastWeatherMetrics forecastWeatherMetric2 = new ForecastWeatherMetrics(city2);
        forecastWeatherMetric2.setDailyTemperatures(magnitudes);

        //When
        when(forecastWeatherStore.findAllCities()).thenReturn(cityList);
        when(forecastWeatherService.getForecastWeatherMetrics(city1)).thenReturn(forecastWeatherMetric1);
        when(forecastWeatherService.getForecastWeatherMetrics(city2)).thenReturn(forecastWeatherMetric2);

        //Run the scheduler manually
        forecastWeatherScheduler.forecastWeatherRetrieveScheduler();
    }
}
