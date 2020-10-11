package com.cloudator.restapp.forecastweather.service;

import com.cloudator.restapp.forecastweather.model.City;
import com.cloudator.restapp.forecastweather.model.ForecastWeatherMetrics;
import com.cloudator.restapp.forecastweather.service.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by Bobur on 11.10.2020.
 */
@Service
public class ForecastWeatherServiceImpl implements ForecastWeatherService {

    /**
     * If no source provider is found, this is used as default implementation
     */
    public static final String DEFAULT_IMPL = "com.cloudator.restapp.forecastweather.service.OpenWeatherSourceProviderImpl";

    private static final Logger logger = LoggerFactory.getLogger(ForecastWeatherServiceImpl.class);

    @Autowired
    private SourceProviderFactory sourceProviderFactory;

    /**
     * Gets the forecast weather metrics for the given city
     *
     * @param city a City instance
     * @return a ForecastWeatherMetrics instance containing the required averages
     * @throws BusinessException if any exception is found, with a message containing contextual information of the error and the root exception
     */
    @Override
    public ForecastWeatherMetrics getForecastWeatherMetrics(City city) throws BusinessException {
        try {
            return sourceProviderFactory.getSourceProvider(city.getSourceProviderKey()).getForecastWeatherMetrics(city);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Exception encountered invoking getForecastWeatherMetrics with param=" + city, e);
        }
    }

}
