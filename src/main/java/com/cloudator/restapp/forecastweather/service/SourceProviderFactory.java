package com.cloudator.restapp.forecastweather.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Factory to create SourceProvider instances based on the providerKey
 * Created by Bobur on 11.10.2020
 */
@Component
public class SourceProviderFactory {

    /**
     * If no source provider is found, this is used as default implementation
     */
    public static final String DEFAULT_IMPL = "com.cloudator.restapp.forecastweather.service.OpenWeatherSourceProviderImpl";

    private static final Logger logger = LoggerFactory.getLogger(SourceProviderFactory.class);

    private final ApplicationContext applicationContext;

    private final Environment env;

    public SourceProviderFactory(ApplicationContext applicationContext, Environment env) {
        this.applicationContext = applicationContext;
        this.env = env;
    }

    public SourceProvider getSourceProvider(String providerKey) {
        String fullyQualifiedClassName = getFullyQualifiedClassName(providerKey);
        return applicationContext.getBean(fullyQualifiedClassName, SourceProvider.class);
    }

    private String getFullyQualifiedClassName(String providerKey) {
        try {
            return env.getProperty(providerKey, DEFAULT_IMPL);
        } catch (Exception e) {
            logger.debug("Exception getting fullyQualifiedClassName for providerKey=" + providerKey + ", the defaul impl will be returned");
            return DEFAULT_IMPL;
        }
    }

}
