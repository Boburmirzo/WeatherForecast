package com.cloudator.restapp.forecastweather.service;

import com.cloudator.restapp.forecastweather.model.City;
import com.cloudator.restapp.forecastweather.model.ForecastWeatherMetrics;
import com.cloudator.restapp.forecastweather.model.Magnitude;
import com.cloudator.restapp.forecastweather.model.TemperatureEnum;
import com.cloudator.restapp.forecastweather.service.dto.MagnitudeInfo;
import com.cloudator.restapp.forecastweather.service.dto.MockyInfo;
import com.cloudator.restapp.forecastweather.service.exception.IntegrationException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * SourceProvider implementation to get the info from SourceProvider
 * Created by Bobur on 11.10.2020
 */
@Service("com.cloudator.restapp.forecastweather.service.MockySourceProviderImpl")
public class MockySourceProviderImpl extends AbstractSourceProvider {

    private static final Logger logger = LoggerFactory.getLogger(MockySourceProviderImpl.class);

    @Value("${mocky.endpoint}")
    private String mockyEndPoint;

    @Override
    public ForecastWeatherMetrics getForecastWeatherMetrics(City city) {
        MockyInfo mockyInfo = getMockyInfoByCityId(city);
        return getForecastWeatherMetrics(mockyInfo, city);
    }

    private ForecastWeatherMetrics getForecastWeatherMetrics(MockyInfo mocky, City city) {

        ForecastWeatherMetrics forecastWeatherMetrics = new ForecastWeatherMetrics(city);
        //Circuit breaker
        if (CollectionUtils.isEmpty(mocky.getList())) {
            return forecastWeatherMetrics;
        }

        LocalDate nowPlusThreeDays = getNowPlusNDays(city.getLimitDay());

        Predicate<MagnitudeInfo> limitDays = magnitudeInfo -> (magnitudeInfo.getDatetime().toLocalDate().isBefore(nowPlusThreeDays));

        //Calculating the dailyTemperatureAverageMagnitude
        Predicate<MagnitudeInfo> dailyPredicate = magnitudeInfo -> (magnitudeInfo.getDatetime().getHour() >= START_DAILY_HOUR
                && magnitudeInfo.getDatetime().getHour() <= END_DAILY_HOUR);

        logger.info("ForecastWeatherServiceImpl.day");
        List<Magnitude> dailyTemperatureMagnitudes = getDailyTemperatureList(mocky, limitDays, city.getLimitTemperature());

        forecastWeatherMetrics.setDailyTemperatures(dailyTemperatureMagnitudes);

        return forecastWeatherMetrics;
    }

    protected List<Magnitude> getDailyTemperatureList(MockyInfo mockyInfo,
                                                      Predicate<MagnitudeInfo> limitDays,
                                                      Double limitTemperature) {

        return mockyInfo.getList()
                .stream()
                .filter(Objects::nonNull)
                .filter(limitDays)
                .peek(magnitudeInfo -> logger.info("dateTime=" + magnitudeInfo.getDatetime()))
                .map(MagnitudeInfo::getData)
                .filter(temperature -> temperature.getTemperature() >= limitTemperature)
                .map(temperature -> new Magnitude(TemperatureEnum.FAHRENHEIT.getCode(), temperature.getTemperature()))
                .collect(Collectors.toList());

    }

    @HystrixCommand(fallbackMethod = "defaultMockyInfo")
    public MockyInfo getMockyInfoByCityId(City city) throws IntegrationException {
        try {
            return restTemplate.getForObject(getMockyUrlByCityId(city), MockyInfo.class);
        } catch (Exception e) {
            throw new IntegrationException("Exception encountered invoking getMockyInfoByCityId with param=" + city, e);
        }
    }

    /**
     * This is default MockyInfo instance used in case of connection failure with the Mocky weather API provider
     *
     * @param city an instance of City containing its id
     * @return a MockyInfo instance
     */
    @HystrixCommand
    private MockyInfo defaultMockyInfo(City city) {
        return new MockyInfo();
    }

    private String getMockyUrlByCityId(City city) {
        return mockyEndPoint + city.getId();
    }

}
