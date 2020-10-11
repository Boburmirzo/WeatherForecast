package com.cloudator.restapp.forecastweather.service.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * Created by Bobur on 11.10.2020.
 */
public class IntegrationException extends NestedRuntimeException {


    /**
     * Construct a BusinessException with the specified detail message.
     *
     * @param message Message intended for developers.
     */
    public IntegrationException(String message) {
        super(message);
    }

    /**
     * Construct a BusinessException with the specified detail message and the original exception.
     *
     * @param message           Message intended for developers.
     * @param originalException The nested exception.
     */
    public IntegrationException(String message, Throwable originalException) {
        super(message, originalException);
    }
}
