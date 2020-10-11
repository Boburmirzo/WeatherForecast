package com.cloudator.restapp.forecastweather.service.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * Unchecked exception to be thrown as higher exception in order to decouple the exception management in a multi tier application.
 * Created by Bobur on 11.10.2020.
 */
public class BusinessException extends NestedRuntimeException {


    /**
     * Constructs a BusinessException with the specified detail message.
     *
     * @param message Message intended for developers.
     */
    public BusinessException(String message) {
        super(message);
    }

    /**
     * Constructs a BusinessException with the specified detail message and the original exception.
     *
     * @param message           Message intended for developers.
     * @param originalException The nested exception.
     */
    public BusinessException(String message, Throwable originalException) {
        super(message, originalException);
    }


}
