package com.cloudator.restapp.forecastweather.service;

import com.cloudator.restapp.forecastweather.service.exception.AuthorizationException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;


/**
 * Created by Bobur on 11.10.2020
 */
@Service
public class SecurityServiceImpl implements SecurityService {

    @Value("${security.secretKeys}")
    private String secretKeys;

    /**
     * Generates a token if the user is valid
     *
     * @param subject   The subject requires
     * @param ttlMillis expiration time in millis
     * @return a token if the user is valid, throws an exception otherwise
     * @throws AuthorizationException if any exception is found, with a message containing contextual information of the error and the root exception
     */
    @Override
    public String generateToken(String subject, long ttlMillis) throws AuthorizationException {
        try {
            if (ttlMillis <= 0) {
                throw new IllegalArgumentException("Expiry time must be greater than Zero :[" + ttlMillis + "] ");
            }
            // The JWT signature algorithm we will be using to sign the token
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKeys);
            Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
            JwtBuilder builder = Jwts.builder()
                    .setSubject(subject)
                    .signWith(signatureAlgorithm, signingKey);
            long nowMillis = System.currentTimeMillis();
            builder.setExpiration(new Date(nowMillis + ttlMillis));
            return builder.compact();
        } catch (Exception e) {
            throw new AuthorizationException("Exception encountered invoking generateToken with param=" + subject, e);
        }
    }
}
