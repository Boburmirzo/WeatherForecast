package com.cloudator.restapp.forecastweather.controller;

import com.cloudator.restapp.forecastweather.controller.dto.ResponseCodeEnum;
import com.cloudator.restapp.forecastweather.controller.dto.ServiceRequest;
import com.cloudator.restapp.forecastweather.controller.dto.ServiceResponse;
import com.cloudator.restapp.forecastweather.controller.exception.ExceptionHandler;
import com.cloudator.restapp.forecastweather.model.City;
import com.cloudator.restapp.forecastweather.model.ForecastWeatherMetrics;
import com.cloudator.restapp.forecastweather.service.ForecastWeatherService;
import com.cloudator.restapp.forecastweather.service.SecurityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller represents the entry point to the Forecast Weather API
 * Created by Bobur on 11.10.2020.
 */
@RestController
@RequestMapping("/weather")
@Api(value = "/weather")
public class ForecastWeatherController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ForecastWeatherController.class);

    private final ExceptionHandler exceptionHandler;

    private final ForecastWeatherService forecastWeatherService;

    private final SecurityService securityService;

    public ForecastWeatherController(ExceptionHandler exceptionHandler,
                                     ForecastWeatherService forecastWeatherService,
                                     SecurityService securityService) {
        this.exceptionHandler = exceptionHandler;
        this.forecastWeatherService = forecastWeatherService;
        this.securityService = securityService;
    }

    /**
     * Generates the token for the given user
     *
     * @param serviceRequest An instance of ServiceRequest<String> containing the subject
     * @return a ServiceResponse<String>> containing the generated token
     */
    @PostMapping("/generateToken")
    @ApiOperation(value = "Generates the JWT", httpMethod = "POST")
    public ServiceResponse<String> generateToken(@RequestBody ServiceRequest<String> serviceRequest) {
        ServiceResponse<String> serviceResponse = new ServiceResponse<>();
        try {
            checkParameter(serviceRequest);
            long threeMinutes = 6 * 1000 * 60;
            String token = securityService.generateToken(serviceRequest.getParameter(), threeMinutes);
            handleResponse(serviceResponse, token);
        } catch (Exception e) {
            exceptionHandler.handleControllerException(serviceResponse, serviceRequest, e, getMethodName());
        }
        return serviceResponse;
    }


    /**
     * Get forecast weather info for given city
     *
     * @param serviceRequest An instance of ServiceRequest<City> containing a City instance
     * @return a ServiceResponse<ForecastWeatherMetrics>> containing the temperature data
     */
    @PostMapping("/data")
    // Token required annotation is disabled for just easily check functionality,
    // if you enable it, token is required to access to the endpoint
    // @TokenRequired
    @ApiOperation(value = "Gets the ForecastWeatherMetrics", httpMethod = "POST")
    public ServiceResponse<ForecastWeatherMetrics> getForecastWeatherMetricForTheCity(@RequestBody ServiceRequest<City> serviceRequest) {
        ServiceResponse<ForecastWeatherMetrics> serviceResponse = new ServiceResponse<>();
        try {
            checkCityParameter(serviceRequest);

            ForecastWeatherMetrics forecastWeatherMetric = forecastWeatherService.getForecastWeatherMetrics(serviceRequest.getParameter());
            serviceResponse.setCode(forecastWeatherMetric.isValid() ? ResponseCodeEnum.SUCCESS : ResponseCodeEnum.UNSUCCESS);
            serviceResponse.setResult(forecastWeatherMetric);

        } catch (Exception e) {
            exceptionHandler.handleControllerException(serviceResponse, serviceRequest, e, getMethodName());
        }
        return serviceResponse;
    }
}
