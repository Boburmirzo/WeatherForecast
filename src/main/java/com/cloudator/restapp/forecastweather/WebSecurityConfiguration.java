package com.cloudator.restapp.forecastweather;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by Bobur on 11.10.2020
 */
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final Environment env;

    public WebSecurityConfiguration(Environment env) {
        this.env = env;
    }

    // Authentication : User --> Roles
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance())
                .withUser(env.getProperty("spring.security.user.name"))
                .password(env.getProperty("spring.security.user.password")).roles("USER");
    }

    // Authorization : Role -> Access
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and().authorizeRequests().antMatchers("/weather/**")
                .hasRole("USER").and().csrf().disable().headers().frameOptions().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
