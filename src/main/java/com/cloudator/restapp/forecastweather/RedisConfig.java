package com.cloudator.restapp.forecastweather;

import com.cloudator.restapp.forecastweather.model.ForecastWeatherMetrics;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {

    @Bean
    public RedisTemplate<Long, ForecastWeatherMetrics> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Long, ForecastWeatherMetrics> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}
