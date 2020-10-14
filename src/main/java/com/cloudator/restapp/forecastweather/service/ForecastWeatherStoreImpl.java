package com.cloudator.restapp.forecastweather.service;

import com.cloudator.restapp.forecastweather.model.City;
import com.cloudator.restapp.forecastweather.model.ForecastWeatherMetrics;
import com.cloudator.restapp.forecastweather.repository.dao.ForecastWeatherDao;
import com.cloudator.restapp.forecastweather.service.exception.BusinessException;
import com.cloudator.restapp.forecastweather.service.exception.IntegrationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Bobur on 13.10.2020.
 */

@Service
public class ForecastWeatherStoreImpl implements ForecastWeatherStore {

    private final ForecastWeatherDao forecastWeatherDao;
    private final ResourceLoader resourceLoader;

    public ForecastWeatherStoreImpl(ForecastWeatherDao forecastWeatherDao,
                                    ResourceLoader resourceLoader) {
        this.forecastWeatherDao = forecastWeatherDao;
        this.resourceLoader = resourceLoader;
    }

    /**
     * Gets the ForecastWeatherMetrics from a specific city
     *
     * @param city The city parameter
     * @return a ForecastWeatherMetrics data taken from DB
     */
    @Override
    public ForecastWeatherMetrics getForecastWeatherMetricsForCity(City city) throws BusinessException {
        Long cityId = city.getId();

        if (city.getId() == null) {
            cityId = findCityIdByNameAndCountryCode(city);
        }

        return forecastWeatherDao.findByCiyId(cityId);
    }

    @Override
    public List<ForecastWeatherMetrics> getForecastWeatherMetricsForAllCities() throws BusinessException {
        List<City> cities = findAllCities();
        if (cities == null || cities.isEmpty()) {
            return new ArrayList<>();
        }
        return forecastWeatherDao.findAllForecastWeatherMetricsForAllCities(cities.stream().map(City::getId).collect(Collectors.toList()));
    }


    /**
     * Retrieves all cities in Json file
     *
     * @return list of cities available for weather forecast
     * @throws IntegrationException if any exception is found, with a message containing contextual information of the error and the root exception
     */
    @Override
    @Cacheable(value = "allACitiesCache", unless = "#result.size() == 0")
    public List<City> findAllCities() {
        try {
            ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            Resource resource = getCityResource();
            return objectMapper.readValue(resource.getInputStream(), new TypeReference<ArrayList<City>>() {
            });

        } catch (Exception e) {
            throw new IntegrationException("Exception encountered invoking findAllCities", e);
        }
    }

    @Override
    public void saveAllForecastWeatherMetrics(List<ForecastWeatherMetrics> forecastWeatherMetrics) throws IntegrationException {
        forecastWeatherDao.saveAllForecastWeatherMetrics(forecastWeatherMetrics);
    }

    /**
     * Retrieves a city id by city name and country ISO code
     *
     * @param cityParam A city containing the name and the country ISO code
     * @return @return an instance of City
     * @throws IntegrationException if any exception is found, with a message containing contextual information of the error and the root exception
     */
    private Long findCityIdByNameAndCountryCode(City cityParam) throws IntegrationException {
        List<City> cities = findAllCities();

        List<City> nonNullCities = cities.stream()
                .filter(Objects::nonNull).collect(Collectors.toList());

        if (cityParam.getName() != null) {
            nonNullCities = nonNullCities
                    .stream()
                    .filter(city -> cityParam.getName().equals(city.getName()))
                    .collect(Collectors.toList());
        }

        if (cityParam.getIsoCountryCode() != null) {
            nonNullCities = nonNullCities
                    .stream()
                    .filter(city -> cityParam.getIsoCountryCode().equals(city.getIsoCountryCode()))
                    .collect(Collectors.toList());
        }

        City retrievedCity = nonNullCities.stream().findFirst().orElse(null);
        cityParam.setId(retrievedCity == null ? null : retrievedCity.getId());

        return cityParam.getId();
    }


    /**
     * The resource with the city info from a file given by the API provider
     *
     * @return a Resource instance
     */
    public Resource getCityResource() {
        return resourceLoader.getResource("classpath:static/city.list.json");
    }
}
