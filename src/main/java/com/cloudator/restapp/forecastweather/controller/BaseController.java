package com.cloudator.restapp.forecastweather.controller;


import com.cloudator.restapp.forecastweather.controller.dto.ResponseCodeEnum;
import com.cloudator.restapp.forecastweather.controller.dto.ServiceRequest;
import com.cloudator.restapp.forecastweather.controller.dto.ServiceResponse;
import com.cloudator.restapp.forecastweather.model.City;

import java.io.Serializable;

/**
 * Common controller class to extend any spring controller in the application
 */
public abstract class BaseController {

    protected <T> void checkParameter(ServiceRequest<T> serviceRequest) {
        if (serviceRequest == null || serviceRequest.getParameter() == null) {
            throw new IllegalArgumentException("The required parameter is null");
        }
    }

    protected void checkCityParameter(ServiceRequest<City> serviceRequest) {
        checkParameter(serviceRequest);
        City city = serviceRequest.getParameter();
        if (city.getId() == null && city.getName() == null && city.getIsoCountryCode() == null) {
            throw new IllegalArgumentException("The received City parameter is invalid=" + city);
        }
    }


    protected <T extends Serializable> void handleResponse(ServiceResponse<T> serviceResponse, T result) {
        serviceResponse.setCode(ResponseCodeEnum.SUCCESS);
        serviceResponse.setResult(result);
    }

    /**
     * Returns the name of the current method being executed
     *
     * @return a String object
     */
    protected String getMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }
}
