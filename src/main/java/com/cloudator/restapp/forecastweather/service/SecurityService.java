package com.cloudator.restapp.forecastweather.service;

import com.cloudator.restapp.forecastweather.service.exception.AuthorizationException;

/**
 * Created by Bobur on 11.10.2020.
 */
public interface SecurityService {

    /**
     * Generates a token if the user is valid
     *
     * @param subject   The subject requires
     * @param ttlMillis expiration time in millis
     * @return a token if the user is valid, throws an exception otherwise
     * @throws AuthorizationException if any exception is found, with a message containing contextual information of the error and the root exception
     */
    String generateToken(String subject, long ttlMillis) throws AuthorizationException;
}
