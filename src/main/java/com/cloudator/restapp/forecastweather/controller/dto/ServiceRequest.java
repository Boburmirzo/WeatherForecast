package com.cloudator.restapp.forecastweather.controller.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * Generic DTO to use as the base class for each request to a service
 */
public class ServiceRequest<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Contains the value that is being sent to the service
     */
    private T parameter;

    /**
     * Default constructor without arguments
     */
    public ServiceRequest() {
    }


    /**
     * Creates a new instance defining the authentication token and the warpped value
     *
     * @param parameter Object to be transported by the request instance
     */
    public ServiceRequest(T parameter) {
        this.parameter = parameter;
    }

    public T getParameter() {
        return parameter;
    }

    public void setParameter(T parameter) {
        this.parameter = parameter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServiceRequest<?> that = (ServiceRequest<?>) o;

        return Objects.equals(parameter, that.parameter);

    }

    @Override
    public int hashCode() {
        return parameter != null ? parameter.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ServiceRequest{" + "parameter=" + parameter +
                '}';
    }
}
