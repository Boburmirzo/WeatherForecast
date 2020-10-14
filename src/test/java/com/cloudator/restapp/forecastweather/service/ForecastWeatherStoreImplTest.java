package com.cloudator.restapp.forecastweather.service;


import com.cloudator.restapp.forecastweather.model.City;
import com.cloudator.restapp.forecastweather.model.ForecastWeatherMetrics;
import com.cloudator.restapp.forecastweather.model.Magnitude;
import com.cloudator.restapp.forecastweather.repository.dao.ForecastWeatherDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.cloudator.restapp.forecastweather.model.TemperatureEnum.CELSIUS;
import static org.mockito.Mockito.when;

/**
 * Created by Bobur on 14.10.2020
 */
@RunWith(SpringRunner.class)
public class ForecastWeatherStoreImplTest {

    @Spy
    @InjectMocks
    private ForecastWeatherStoreImpl forecastWeatherStore;

    @Mock
    private ForecastWeatherDao forecastWeatherDao;

    @Mock
    private ResourceLoader resourceLoader;


    @Test
    public void testGetForecastWeatherMetricsForGivenCityId() {
        City city = new City();
        city.setId(4517009L);
        city.setName("London");
        city.setLimitTemperature(10);

        Magnitude magnitude = new Magnitude(CELSIUS.getCode(), 24);
        magnitude.setFrom(LocalDateTime.now());
        magnitude.setTo(LocalDateTime.now());
        List<Magnitude> magnitudes = new ArrayList<>();
        magnitudes.add(magnitude);

        ForecastWeatherMetrics forecastWeatherMetric = new ForecastWeatherMetrics(city);
        forecastWeatherMetric.setDailyTemperatures(magnitudes);

        when(forecastWeatherDao.findByCiyId(city.getId())).thenReturn(forecastWeatherMetric);

        ForecastWeatherMetrics forecastWeatherMetricsFound = forecastWeatherStore.getForecastWeatherMetricsForCity(city);

        Assert.assertNotNull(forecastWeatherMetricsFound);
    }

    @Test
    public void testGetForecastWeatherMetricsForCityIdNull() throws IOException {
        City city = new City();
        city.setName("London");
        city.setLimitTemperature(10);

        Magnitude magnitude = new Magnitude(CELSIUS.getCode(), 24);
        magnitude.setFrom(LocalDateTime.now());
        magnitude.setTo(LocalDateTime.now());
        List<Magnitude> magnitudes = new ArrayList<>();
        magnitudes.add(magnitude);

        ForecastWeatherMetrics forecastWeatherMetric = new ForecastWeatherMetrics(city);
        forecastWeatherMetric.setDailyTemperatures(magnitudes);

        String mockFile = "[\n" +
                "  {\n" +
                "    \"id\": 2950159,\n" +
                "    \"name\": \"Berlin\",\n" +
                "    \"isoCountryCode\": \"DE\",\n" +
                "    \"limitTemperature\": 10,\n" +
                "    \"coord\": {\n" +
                "      \"lon\": 13.41053,\n" +
                "      \"lat\": 52.524368\n" +
                "    }\n" +
                "  }\n" +
                "]";
        InputStream is = new ByteArrayInputStream(mockFile.getBytes());

        Resource mockResource = Mockito.mock(Resource.class);
        Mockito.when(mockResource.getInputStream()).thenReturn(is);

        when(forecastWeatherDao.findByCiyId(null)).thenReturn(forecastWeatherMetric);
        when(resourceLoader.getResource(Mockito.anyString())).thenReturn(mockResource);


        ForecastWeatherMetrics forecastWeatherMetricsFound = forecastWeatherStore.getForecastWeatherMetricsForCity(city);

        Assert.assertNotNull(forecastWeatherMetricsFound);
    }

    @Test
    public void testFindAllCities() throws IOException {
        String mockFile = "[\n" +
                "  {\n" +
                "    \"id\": 2950159,\n" +
                "    \"name\": \"Berlin\",\n" +
                "    \"isoCountryCode\": \"DE\",\n" +
                "    \"limitTemperature\": 10,\n" +
                "    \"coord\": {\n" +
                "      \"lon\": 13.41053,\n" +
                "      \"lat\": 52.524368\n" +
                "    }\n" +
                "  }\n" +
                "]";
        InputStream is = new ByteArrayInputStream(mockFile.getBytes());

        Resource mockResource = Mockito.mock(Resource.class);
        Mockito.when(mockResource.getInputStream()).thenReturn(is);

        when(resourceLoader.getResource(Mockito.anyString())).thenReturn(mockResource);

        List<City> cities = forecastWeatherStore.findAllCities();

        Assert.assertEquals(1, cities.size());
    }

}
