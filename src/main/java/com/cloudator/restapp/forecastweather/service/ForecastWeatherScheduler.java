package com.cloudator.restapp.forecastweather.service;

import com.cloudator.restapp.forecastweather.model.City;
import com.cloudator.restapp.forecastweather.model.ForecastWeatherMetrics;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ForecastWeatherScheduler {

    private final ForecastWeatherService forecastWeatherService;
    private final ForecastWeatherStore forecastWeatherStore;

    public ForecastWeatherScheduler(ForecastWeatherService forecastWeatherService,
                                    ForecastWeatherStore forecastWeatherStore) {
        this.forecastWeatherService = forecastWeatherService;
        this.forecastWeatherStore = forecastWeatherStore;
    }

    /**
     * Scheduler to get data from provider
     * It runs every 5 mins and gets the data from forecast service and stores results into memory like Redis
     */
    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void forecastWeatherRetrieveScheduler() {
        List<City> cities = forecastWeatherStore.findAllCities();

        if (!cities.isEmpty()) {
            List<ForecastWeatherMetrics> forecastWeatherMetrics = cities
                    .stream()
                    .map(forecastWeatherService::getForecastWeatherMetrics)
                    .collect(Collectors.toList());
            if (!forecastWeatherMetrics.isEmpty()) {
                forecastWeatherStore.saveAllForecastWeatherMetrics(forecastWeatherMetrics);
            }
        }
    }

}
