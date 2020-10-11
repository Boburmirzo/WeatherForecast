package com.cloudator.restapp.forecastweather.service;

import com.cloudator.restapp.forecastweather.model.City;
import com.cloudator.restapp.forecastweather.model.ForecastWeatherMetrics;
import com.cloudator.restapp.forecastweather.model.Magnitude;
import com.cloudator.restapp.forecastweather.repository.dao.ForecastWeatherDao;
import com.cloudator.restapp.forecastweather.service.dto.WeatherData;
import junit.framework.TestCase;
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
import java.util.function.Predicate;

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
        city.setLimitTemperature(10.0);

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


//        TestCase.assertEquals(new Magnitude("Celsius", 0), forecastWeatherMetrics.getDailyTemperature());
//        assertEquals(new Magnitude("Celsius", 25), forecastWeatherMetrics.getNightlyTemperatureAverage());
//        assertEquals(new Magnitude("hPa", 950), forecastWeatherMetrics.getPressureAverage());

    }

    /**
     * Validates the getTemperatureAverageMagnitude method for daily records
     */
    @Test
    public void testGetTemperatureAverageMagnitudeForDaily() throws Exception {

        //given conditions
        City city = new City();
        city.setId(3647637L);
        WeatherData weatherData = new WeatherData();
        WeatherData.Forecast forecast = new WeatherData.Forecast();
        weatherData.setForecast(forecast);

        WeatherData.Forecast.Time time = new WeatherData.Forecast.Time();
        time.setFrom(LocalDateTime.of(2018, Month.OCTOBER, 14, 6, 0));
        time.setTo(LocalDateTime.of(2018, Month.OCTOBER, 14, 9, 0));
        forecast.getTime().add(time);
        WeatherData.Forecast.Time.Temperature temperature = new WeatherData.Forecast.Time.Temperature();
        temperature.setUnit("celsius");
        temperature.setValue(new BigDecimal("20"));
        time.setTemperature(temperature);

        WeatherData.Forecast.Time time2 = new WeatherData.Forecast.Time();
        time2.setFrom(LocalDateTime.of(2018, Month.OCTOBER, 14, 9, 0));
        time2.setTo(LocalDateTime.of(2018, Month.OCTOBER, 14, 12, 0));
        forecast.getTime().add(time2);
        WeatherData.Forecast.Time.Temperature temperature2 = new WeatherData.Forecast.Time.Temperature();
        temperature2.setUnit("celsius");
        temperature2.setValue(new BigDecimal("30"));
        time2.setTemperature(temperature2);

        WeatherData.Forecast.Time time3 = new WeatherData.Forecast.Time();
        time3.setFrom(LocalDateTime.of(2018, Month.OCTOBER, 14, 12, 0));
        time3.setTo(LocalDateTime.of(2018, Month.OCTOBER, 14, 15, 0));
        forecast.getTime().add(time3);
        WeatherData.Forecast.Time.Temperature temperature3 = new WeatherData.Forecast.Time.Temperature();
        temperature3.setUnit("celsius");
        temperature3.setValue(new BigDecimal("10"));
        time3.setTemperature(temperature3);

        WeatherData.Forecast.Time time4 = new WeatherData.Forecast.Time();
        time4.setFrom(LocalDateTime.of(2018, Month.OCTOBER, 14, 15, 0));
        time4.setTo(LocalDateTime.of(2018, Month.OCTOBER, 14, 18, 0));
        forecast.getTime().add(time4);
        WeatherData.Forecast.Time.Temperature temperature4 = new WeatherData.Forecast.Time.Temperature();
        temperature4.setUnit("celsius");
        temperature4.setValue(new BigDecimal("20"));
        time4.setTemperature(temperature4);

        LocalDate nowPlusThreeDays = LocalDate.of(2018, Month.OCTOBER, 17);
        Predicate<WeatherData.Forecast.Time> limitDays = tim -> (tim.getFrom().toLocalDate().isBefore(nowPlusThreeDays));

        //Calculating the dailyTemperatureAverageMagnitude
        Predicate<WeatherData.Forecast.Time> dailyPredicate = tim2 -> (tim2.getFrom().getHour() >= AbstractSourceProvider.START_DAILY_HOUR
                && tim2.getFrom().getHour() < AbstractSourceProvider.END_DAILY_HOUR
                && tim2.getTo().getHour() <= AbstractSourceProvider.END_DAILY_HOUR
                && tim2.getTo().getHour() > AbstractSourceProvider.START_DAILY_HOUR);

//        //validate
//        Magnitude magnitude = systemUnderTest.getTemperatureAverageMagnitude(weatherData, dailyPredicate, limitDays);
//        assertNotNull(magnitude);
//        assertNotNull(magnitude.getUnit());
//        assertEquals(new Magnitude("Celsius", 20), magnitude);
    }

    /**
     * Validates the getTemperatureAverageMagnitude method for nightly records
     */
    @Test
    public void testGetTemperatureAverageMagnitudeForNightly() throws Exception {
        //given conditions
        City city = new City();
        city.setId(3647637L);
        WeatherData weatherData = new WeatherData();
        WeatherData.Forecast forecast = new WeatherData.Forecast();
        weatherData.setForecast(forecast);

        WeatherData.Forecast.Time time = new WeatherData.Forecast.Time();
        time.setFrom(LocalDateTime.of(2018, Month.OCTOBER, 14, 18, 0));
        time.setTo(LocalDateTime.of(2018, Month.OCTOBER, 14, 21, 0));
        forecast.getTime().add(time);
        WeatherData.Forecast.Time.Temperature temperature = new WeatherData.Forecast.Time.Temperature();
        temperature.setUnit("celsius");
        temperature.setValue(new BigDecimal("10"));
        time.setTemperature(temperature);

        WeatherData.Forecast.Time time2 = new WeatherData.Forecast.Time();
        time2.setFrom(LocalDateTime.of(2018, Month.OCTOBER, 14, 21, 0));
        time2.setTo(LocalDateTime.of(2018, Month.OCTOBER, 14, 0, 0));
        forecast.getTime().add(time2);
        WeatherData.Forecast.Time.Temperature temperature2 = new WeatherData.Forecast.Time.Temperature();
        temperature2.setUnit("celsius");
        temperature2.setValue(new BigDecimal("10"));
        time2.setTemperature(temperature2);

        WeatherData.Forecast.Time time3 = new WeatherData.Forecast.Time();
        time3.setFrom(LocalDateTime.of(2018, Month.OCTOBER, 14, 0, 0));
        time3.setTo(LocalDateTime.of(2018, Month.OCTOBER, 14, 3, 0));
        forecast.getTime().add(time3);
        WeatherData.Forecast.Time.Temperature temperature3 = new WeatherData.Forecast.Time.Temperature();
        temperature3.setUnit("celsius");
        temperature3.setValue(new BigDecimal("20"));
        time3.setTemperature(temperature3);

        WeatherData.Forecast.Time time4 = new WeatherData.Forecast.Time();
        time4.setFrom(LocalDateTime.of(2018, Month.OCTOBER, 14, 3, 0));
        time4.setTo(LocalDateTime.of(2018, Month.OCTOBER, 14, 6, 0));
        forecast.getTime().add(time4);
        WeatherData.Forecast.Time.Temperature temperature4 = new WeatherData.Forecast.Time.Temperature();
        temperature4.setUnit("celsius");
        temperature4.setValue(new BigDecimal("0"));
        time4.setTemperature(temperature4);

        LocalDate nowPlusThreeDays = LocalDate.of(2018, Month.OCTOBER, 17);
        Predicate<WeatherData.Forecast.Time> limitDays = tim -> (tim.getFrom().toLocalDate().isBefore(nowPlusThreeDays));

        //Calculating the nightlyTemperatureAverageMagnitude
        Predicate<WeatherData.Forecast.Time> nightlyPredicate = tim -> tim.getTo().getHour()
                <= AbstractSourceProvider.START_DAILY_HOUR || tim.getFrom().getHour() >= AbstractSourceProvider.END_DAILY_HOUR;

//        //validate
//        Magnitude magnitude = systemUnderTest.getTemperatureAverageMagnitude(weatherData, nightlyPredicate, limitDays);
//        assertNotNull(magnitude);
//        assertNotNull(magnitude.getUnit());
//        assertEquals(new Magnitude("Celsius", 10), magnitude);


    }

    /**
     * Validates the getPressureAverageMagnitude method for records generated in different days
     */
    @Test
    public void testGetPressureAverageMagnitude() throws Exception {
        WeatherData weatherData = new WeatherData();
        WeatherData.Forecast forecast = new WeatherData.Forecast();
        weatherData.setForecast(forecast);

        WeatherData.Forecast.Time time = new WeatherData.Forecast.Time();
        time.setFrom(LocalDateTime.of(2018, Month.OCTOBER, 14, 18, 0));
        time.setTo(LocalDateTime.of(2018, Month.OCTOBER, 14, 21, 0));
        forecast.getTime().add(time);

        WeatherData.Forecast.Time.Pressure pressure = new WeatherData.Forecast.Time.Pressure();
        pressure.setUnit("hPa");
        pressure.setValue(new BigDecimal("900"));
        time.setPressure(pressure);

        WeatherData.Forecast.Time time2 = new WeatherData.Forecast.Time();
        time2.setFrom(LocalDateTime.of(2018, Month.OCTOBER, 15, 6, 0));
        time2.setTo(LocalDateTime.of(2018, Month.OCTOBER, 15, 9, 0));
        forecast.getTime().add(time2);

        WeatherData.Forecast.Time.Pressure pressure2 = new WeatherData.Forecast.Time.Pressure();
        pressure2.setUnit("hPa");
        pressure2.setValue(new BigDecimal("1200"));
        time2.setPressure(pressure2);

        WeatherData.Forecast.Time time3 = new WeatherData.Forecast.Time();
        time3.setFrom(LocalDateTime.of(2018, Month.OCTOBER, 16, 21, 0));
        time3.setTo(LocalDateTime.of(2018, Month.OCTOBER, 16, 0, 0));
        forecast.getTime().add(time3);

        WeatherData.Forecast.Time.Pressure pressure3 = new WeatherData.Forecast.Time.Pressure();
        pressure3.setUnit("hPa");
        pressure3.setValue(new BigDecimal("1500"));
        time3.setPressure(pressure3);

        LocalDate nowPlusThreeDays = LocalDate.of(2018, Month.OCTOBER, 17);
        Predicate<WeatherData.Forecast.Time> limitDays = tim -> (tim.getFrom().toLocalDate().isBefore(nowPlusThreeDays));

//        //validate
//        Magnitude magnitude = systemUnderTest.getPressureAverageMagnitude(weatherData, limitDays);
//        assertNotNull(magnitude);
//        assertNotNull(magnitude.getUnit());
//        assertEquals(new Magnitude("hPa", 1200), magnitude);

    }
}
