package com.cloudator.restapp.forecastweather.controller;

import com.cloudator.restapp.forecastweather.controller.dto.ResponseCodeEnum;
import com.cloudator.restapp.forecastweather.controller.dto.ServiceRequest;
import com.cloudator.restapp.forecastweather.controller.dto.ServiceResponse;
import com.cloudator.restapp.forecastweather.controller.exception.ExceptionHandler;
import com.cloudator.restapp.forecastweather.model.City;
import com.cloudator.restapp.forecastweather.model.ForecastWeatherMetrics;
import com.cloudator.restapp.forecastweather.model.Magnitude;
import com.cloudator.restapp.forecastweather.service.ForecastWeatherService;
import com.cloudator.restapp.forecastweather.service.SecurityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.cloudator.restapp.forecastweather.model.TemperatureEnum.CELSIUS;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


/**
 * Created by Bobur on 11.10.2020
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ForecastWeatherController.class)
public class ForecastWeatherControllerTest {

    @MockBean
    private ForecastWeatherService forecastWeatherService;

    @MockBean
    private SecurityService securityService;

    @MockBean
    private ExceptionHandler exceptionHandler;

    @Autowired
    private MockMvc mockMvc;


    private JacksonTester<ServiceResponse<String>> jsonToken;

    private JacksonTester<ServiceRequest<String>> jsonRequestString;

    private JacksonTester<ServiceRequest<City>> jsonRequestCity;

    private JacksonTester<ServiceResponse<ForecastWeatherMetrics>> jsonMetrics;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void testGenerateTokenWithoutCredentials() throws Exception {
        // given
        String token = "eyJ0eJ9.eyJ1c2VyifQ.7xH-x5mb11yt3rGzcM";
        // when
        when(securityService.generateToken(anyString(), anyLong())).thenReturn(token);
        MockHttpServletResponse response = mockMvc.perform(post("/weather/generateToken")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }

    @Test
    public void testGenerateTokenWithoutCredentialsAndWithoutCsrf() throws Exception {
        // given
        String token = "eyJ0eJ9.eyJ1c2VyifQ.7xH-x5mb11yt3rGzcM";
        // when
        when(securityService.generateToken(anyString(), anyLong())).thenReturn(token);
        MockHttpServletResponse response = mockMvc.perform(post("/weather/generateToken")
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }


    @Test
    @WithMockUser
    public void testGenerateTokenWithValidCredentials() throws Exception {
        // given
        String token = "eyJ0eJ9.eyJ1c2VyifQ.7xH-x5mb11yt3rGzcM";
        String subject = "weatherSubject";
        ServiceRequest<String> serviceRequest = new ServiceRequest<>(subject);

        // when
        when(securityService.generateToken(anyString(), anyLong())).thenReturn(token);
        MockHttpServletResponse response = mockMvc.perform(post("/weather/generateToken")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(jsonRequestString.write(serviceRequest).getJson()))
                .andReturn().getResponse();

        // then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonToken.write(new ServiceResponse(ResponseCodeEnum.SUCCESS, token)).getJson(), response.getContentAsString());
    }


    @Test
    public void testGetForecastWeatherMetricWithoutCredentials() throws Exception {
        //given
        City city = new City();
        ServiceRequest<City> serviceRequest = new ServiceRequest<>(city);
        ForecastWeatherMetrics forecastWeatherMetric = new ForecastWeatherMetrics(city);

        //when
        when(forecastWeatherService.getForecastWeatherMetrics(any(City.class))).thenReturn(forecastWeatherMetric);

        MockHttpServletResponse response = mockMvc.perform(post("/weather/data")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(jsonRequestCity.write(serviceRequest).getJson()))
                .andReturn().getResponse();

        // then
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }

    @Test
    public void testGetForecastWeatherMetricWithoutCredentialsAndWithoutCsrf() throws Exception {
        //given
        City city = new City();
        ServiceRequest<City> serviceRequest = new ServiceRequest<>(city);

        //when
        MockHttpServletResponse response = mockMvc.perform(post("/weather/data")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequestCity.write(serviceRequest).getJson()))
                .andReturn().getResponse();

        //then
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    @WithMockUser
    public void testGetForecastWeatherMetricWithInvalidCity() throws Exception {
        //given
        City city = new City();
        ServiceRequest<City> serviceRequest = new ServiceRequest<>(city);
        ForecastWeatherMetrics forecastWeatherMetric = new ForecastWeatherMetrics(city);

        //when
        when(forecastWeatherService.getForecastWeatherMetrics(any(City.class))).thenReturn(forecastWeatherMetric);

        MockHttpServletResponse response = mockMvc.perform(post("/weather/data")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(jsonRequestCity.write(serviceRequest).getJson()))
                .andReturn().getResponse();

        // then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonMetrics.write(new ServiceResponse(ResponseCodeEnum.UNSUCCESS)).getJson(),
                response.getContentAsString());

    }


    @Test
    @WithMockUser
    public void testGetForecastWeatherMetricWithValidCity() throws Exception {
        //given
        City city = new City("Berlin", "DE");
        ServiceRequest<City> serviceRequest = new ServiceRequest<>(city);
        ForecastWeatherMetrics forecastWeatherMetric = new ForecastWeatherMetrics(city);
        List<Magnitude> magnitudes = new ArrayList<>();
        magnitudes.add(new Magnitude(CELSIUS.getCode(), 23));

        forecastWeatherMetric.setDailyTemperatures(magnitudes);

        //when
        when(forecastWeatherService.getForecastWeatherMetrics(any(City.class))).thenReturn(forecastWeatherMetric);

        MockHttpServletResponse response = mockMvc.perform(post("/weather/data")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(jsonRequestCity.write(serviceRequest).getJson()))
                .andReturn().getResponse();

        // then
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(jsonMetrics.write(new ServiceResponse(ResponseCodeEnum.SUCCESS, forecastWeatherMetric)).getJson(),
                response.getContentAsString());

    }
}
