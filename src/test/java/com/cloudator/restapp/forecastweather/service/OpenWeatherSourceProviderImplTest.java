package com.cloudator.restapp.forecastweather.service;

import com.cloudator.restapp.forecastweather.model.City;
import com.cloudator.restapp.forecastweather.model.ForecastWeatherMetrics;
import com.cloudator.restapp.forecastweather.model.Magnitude;
import com.cloudator.restapp.forecastweather.repository.dao.ForecastWeatherDao;
import com.cloudator.restapp.forecastweather.service.dto.WeatherData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

/**
 * Created by Bobur on 11.10.2020
 */
@RunWith(SpringRunner.class)
public class OpenWeatherSourceProviderImplTest {

    @Mock
    private ForecastWeatherDao forecastWeatherDao;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    protected Environment env;

    @Mock
    private SourceProviderFactory sourceProviderFactory;

    @Spy
    @InjectMocks
    private OpenWeatherSourceProviderImpl systemUnderTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Validates the the getForecastWeatherMetrics method for scenario when the underlying service is down
     */
    @Test
    public void testGetForecastWeatherMetricsWithError() throws Exception {
        //given conditions
        City city = new City();
        city.setId(3647637L);

        //when
        when(restTemplate.getForObject(anyString(), any())).thenReturn(new WeatherData());

        //validate
        ForecastWeatherMetrics forecastWeatherMetrics = systemUnderTest.getForecastWeatherMetrics(city);
        assertNotNull(forecastWeatherMetrics);
        assertFalse(forecastWeatherMetrics.isValid());
    }

    /**
     * Validates the getForecastWeatherMetrics method for the scenario when everything works as expected
     */
    @Test
    public void testGetForecastWeatherMetricsHappyPath() throws Exception {
        //given conditions
        City city = new City();
        city.setId(3647637L);
        city.setName("Test");
        city.setLimitDay(10);
        city.setLimitTemperature(10);

        WeatherData weatherData = new WeatherData();
        WeatherData.Forecast forecast = new WeatherData.Forecast();
        weatherData.setForecast(forecast);

        WeatherData.Forecast.Time time = new WeatherData.Forecast.Time();
        time.setFrom(LocalDateTime.of(2018, Month.OCTOBER, 14, 18, 0));
        time.setTo(LocalDateTime.of(2018, Month.OCTOBER, 14, 21, 0));
        forecast.getTime().add(time);
        WeatherData.Forecast.Time.Temperature temperature = new WeatherData.Forecast.Time.Temperature();
        temperature.setUnit("celsius");
        temperature.setValue(new BigDecimal("20"));
        time.setTemperature(temperature);

        WeatherData.Forecast.Time.Pressure pressure = new WeatherData.Forecast.Time.Pressure();
        pressure.setUnit("hPa");
        pressure.setValue(new BigDecimal("900"));
        time.setPressure(pressure);

        WeatherData.Forecast.Time time2 = new WeatherData.Forecast.Time();
        time2.setFrom(LocalDateTime.of(2018, Month.OCTOBER, 14, 21, 0));
        time2.setTo(LocalDateTime.of(2018, Month.OCTOBER, 14, 0, 0));
        forecast.getTime().add(time2);
        WeatherData.Forecast.Time.Temperature temperature2 = new WeatherData.Forecast.Time.Temperature();
        temperature2.setUnit("celsius");
        temperature2.setValue(new BigDecimal("30"));
        time2.setTemperature(temperature2);

        WeatherData.Forecast.Time.Pressure pressure2 = new WeatherData.Forecast.Time.Pressure();
        pressure2.setUnit("hPa");
        pressure2.setValue(new BigDecimal("1000"));
        time2.setPressure(pressure2);


        //when
        doReturn(weatherData).when(restTemplate).getForObject(anyString(), any());
        doReturn(LocalDate.of(2018, Month.OCTOBER, 17)).when(systemUnderTest).getNowPlusNDays(5);

        //validate
        ForecastWeatherMetrics forecastWeatherMetrics = systemUnderTest.getForecastWeatherMetrics(city);
        assertNotNull(forecastWeatherMetrics);
        assertTrue(forecastWeatherMetrics.isValid());
        assertNotNull(forecastWeatherMetrics.getCity());
        assertEquals("Test", forecastWeatherMetrics.getCity().getName());
        assertEquals("Test", forecastWeatherMetrics.getCity().getName());

        List<Magnitude> magnitudes = forecastWeatherMetrics.getDailyTemperatures();
        assertEquals(2, magnitudes.size());
    }
}
