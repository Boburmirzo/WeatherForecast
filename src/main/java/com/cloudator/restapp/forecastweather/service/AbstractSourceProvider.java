package com.cloudator.restapp.forecastweather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

/**
 * Created by Bobur on 11.10.2020
 */
public abstract class AbstractSourceProvider implements SourceProvider {

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    protected Environment env;

    @Value("${default.limitDay}")
    private Integer defaultLimitDay;

    @Value("${default.limitTemperature}")
    private Integer defaultLimitTemperature;

    protected LocalDate getNowPlusNDays(Integer limitDate) {
        LocalDate localDate = LocalDate.now();
        return localDate.plusDays(limitDate != null ? limitDate : defaultLimitDay);
    }

    protected Integer getDefaultLimitTemperature(Integer limitTemperature) {
        return limitTemperature != null ? limitTemperature : defaultLimitTemperature;
    }
}
