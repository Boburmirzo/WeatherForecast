package com.cloudator.restapp.forecastweather.service;

import com.cloudator.restapp.forecastweather.model.City;
import com.cloudator.restapp.forecastweather.model.ForecastWeatherMetrics;
import com.cloudator.restapp.forecastweather.model.Magnitude;
import com.cloudator.restapp.forecastweather.repository.dao.ForecastWeatherDao;
import com.cloudator.restapp.forecastweather.service.dto.WeatherData;
import com.cloudator.restapp.forecastweather.service.exception.BusinessException;
import com.cloudator.restapp.forecastweather.service.exception.IntegrationException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * SourceProvider implementation to get the info from Open Weather API
 * Created by Bobur on 11.10.2020
 */
@Service("com.cloudator.restapp.forecastweather.service.OpenWeatherSourceProviderImpl")
public class OpenWeatherSourceProviderImpl extends AbstractSourceProvider {

    private static final Logger logger = LoggerFactory.getLogger(OpenWeatherSourceProviderImpl.class);

    private final ForecastWeatherDao forecastWeatherDao;

    @Value("${openweathermap.endpointByCityID}")
    private String endPoint;

    @Value("${openweathermap.appKey}")
    private String appKey;

    public OpenWeatherSourceProviderImpl(ForecastWeatherDao forecastWeatherDao) {
        this.forecastWeatherDao = forecastWeatherDao;
    }

    /**
     * Gets the ForecastWeatherMetrics from a specific provider
     *
     * @param city The city parameter
     * @return a ForecastWeatherMetrics instance
     */
    @Override
    @Cacheable(value = "cityCache", key = "#city")
    public ForecastWeatherMetrics getForecastWeatherMetrics(City city) {
        try {

            WeatherData weatherData = getForecastWeatherMetricsByCityId(city);

            ForecastWeatherMetrics forecastWeatherMetrics = new ForecastWeatherMetrics(city);
            //Circuit breaker
            if (weatherData.getForecast() == null) {
                return forecastWeatherMetrics;
            }

            LocalDate nowPlusNDays = getNowPlusNDays(city.getLimitDay());

            Predicate<WeatherData.Forecast.Time> limitDaysPredicate = time -> (time.getFrom().toLocalDate().isBefore(nowPlusNDays));

            Integer limitTemperature = getDefaultLimitTemperature(city.getLimitTemperature());

            Predicate<WeatherData.Forecast.Time> limitTemperaturePredicate = temperature -> (temperature.getTemperature().getValue().intValue() >= limitTemperature);

            logger.info("ForecastWeatherServiceImpl.day");
            List<Magnitude> dailyTemperatureMagnitudes = getDailyTemperatureList(weatherData, limitDaysPredicate, limitTemperaturePredicate);

            forecastWeatherMetrics.setDailyTemperatures(dailyTemperatureMagnitudes);

            return forecastWeatherMetrics;
        } catch (Exception e) {
            throw new BusinessException("Exception encountered invoking getForecastWeatherMetrics with param=" + city, e);
        }
    }

    protected List<Magnitude> getDailyTemperatureList(WeatherData weatherData,
                                                      Predicate<WeatherData.Forecast.Time> limitDays,
                                                      Predicate<WeatherData.Forecast.Time> limitTemperature) {
        return weatherData.getForecast().getTime()
                .stream()
                .filter(Objects::nonNull)
                .filter(limitDays)
                .filter(limitTemperature)
                .peek(time -> logger.info("from=" + time.getFrom() + ", to=" + time.getTo() + ", temperature=" + time.getTemperature().getValue()))
                .map(time -> new Magnitude(time.getFrom(),
                        time.getTo(),
                        time.getTemperature().getUnit(),
                        time.getTemperature().getValue().doubleValue()))
                .collect(Collectors.toList());
    }

    /**
     * Gets the WeatherData from the external weather provider.
     * This functionality implements the Circuit Breaker pattern to avoid affecting the client
     *
     * @param city an instance of City containing its id
     * @return a WeatherData instance
     * @throws IntegrationException
     */
    @HystrixCommand(fallbackMethod = "defaultWeatherData")
    public WeatherData getForecastWeatherMetricsByCityId(City city) throws IntegrationException {
        try {
            return restTemplate.getForObject(getUrlByCityId(city), WeatherData.class);

        } catch (Exception e) {
            throw new IntegrationException("Exception encountered invoking getForecastWeatherMetricsByCityName with param=" + city, e);
        }
    }

    /**
     * This is default WeatherData instance used in case of connection failure with the weather API provider
     *
     * @param city an instance of City containing its id
     * @return a WeatherData instance
     */
    @HystrixCommand
    private WeatherData defaultWeatherData(City city) {
        return new WeatherData();
    }

    private String getUrlByCityId(City city) {
        return endPoint + city.getId() + "&APPID=" + appKey;
    }
}
