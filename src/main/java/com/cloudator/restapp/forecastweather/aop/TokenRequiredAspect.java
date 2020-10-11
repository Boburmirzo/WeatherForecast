package com.cloudator.restapp.forecastweather.aop;

import com.cloudator.restapp.forecastweather.service.exception.AuthorizationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

/**
 * Created by Bobur on 11.10.2020
 */
@Aspect
@Component
public class TokenRequiredAspect {
    private static final Logger logger = LoggerFactory.getLogger(TokenRequiredAspect.class);

    @Value("${security.tokenName}")
    private String tokenName;

    @Value("${security.secretKeys}")
    private String secretKeys;

    @Value("${security.subject}")
    private String subject;

    @Before("@annotation(tokenRequired)")
    public void tokenRequiredWithAnnotation(TokenRequired tokenRequired) {
        logger.info("Before tokenRequiredWithAnnotation");
        ServletRequestAttributes reqAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = reqAttributes.getRequest();
        // checks for token in request header
        String tokenInHeader = request.getHeader(tokenName);
        if (StringUtils.isEmpty(tokenInHeader)) {
            throw new AuthorizationException("The required token is missing");
        }
        Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKeys))
                .parseClaimsJws(tokenInHeader).getBody();
        if (claims == null || claims.getSubject() == null) {
            throw new AuthorizationException("Token Error: The Claim or the subject is null");
        }
        if (!claims.getSubject().equalsIgnoreCase(subject)) {
            throw new AuthorizationException("Subject doesn't match in the token");
        }
        logger.info("After tokenRequiredWithAnnotation");
    }
}
