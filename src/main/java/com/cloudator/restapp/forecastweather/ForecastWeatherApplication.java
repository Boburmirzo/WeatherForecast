package com.cloudator.restapp.forecastweather;


import io.swagger.annotations.Api;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.Filter;
import java.util.Arrays;

@SpringBootApplication
@EnableSwagger2
@EnableCaching
@EnableCircuitBreaker
public class ForecastWeatherApplication {

    /**
     * The default charset to use for parsing properties files
     */
    private static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * The single basename, following the basic ResourceBundle convention of not specifying file extension or language codes
     */
    private static final String MESSAGE_BASE_NAME = "classpath:static/messages";

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename(MESSAGE_BASE_NAME);
        messageSource.setDefaultEncoding(DEFAULT_ENCODING);
        return messageSource;
    }

    public static void main(String[] args) {
        SpringApplication.run(ForecastWeatherApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .apis(RequestHandlerSelectors.basePackage("com.cloudator.restapp.forecastweather.controller")).paths(PathSelectors.ant("/weather/*"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.POST,
                        Arrays.asList(
                                new ResponseMessageBuilder()
                                        .code(500)
                                        .message("Internal Server Error into Forecast Weather microservice")
                                        .responseModel(new ModelRef("Error"))
                                        .build(),

                                new ResponseMessageBuilder()
                                        .code(403)
                                        .message("API Request Forbidden!")
                                        .build(),

                                new ResponseMessageBuilder()
                                        .code(404)
                                        .message("Request API Not Found!")
                                        .build()

                        ));
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("Forecast Weather Microservice REST API",
                "These are weather service APIs.",
                "API 1.0", "", "", "", "");
    }

    @Bean
    public Filter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }

    @Bean
    public FilterRegistrationBean shallowEtagHeaderFilterRegistration() {
        FilterRegistrationBean result = new FilterRegistrationBean();
        result.setFilter(this.shallowEtagHeaderFilter());
        result.addUrlPatterns("/weather/*");
        result.setName("shallowEtagHeaderFilter");
        result.setOrder(1);
        return result;
    }
}
