package com.cloudator.restapp.forecastweather.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Bobur on 11.10.2020
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class City implements Serializable {

    private static final long serialVersionUID = 8483342465956521524L;

    /**
     * City ID if it is known
     */
    private Long id;

    /**
     * City name like "London", "Berlin"
     */
    private String name;

    /**
     * Country Iso code to search for forecast
     */
    private String isoCountryCode;

    /**
     * Temperature limit as an input
     */
    private Integer limitTemperature;

    /**
     * Limit day to get data for specific days, by default it is 5 days
     */
    private Integer limitDay;

    /**
     * Forecast data provider can be set in the request, by default it is https://openweathermap.org/api
     */
    private String sourceProviderKey;

    public City() {
    }

    public City(Long id) {
        this.id = id;
    }

    public City(String name, String isoCountryCode) {
        this.name = name;
        this.isoCountryCode = isoCountryCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getIsoCountryCode() {
        return isoCountryCode;
    }

    public void setIsoCountryCode(String isoCountryCode) {
        this.isoCountryCode = isoCountryCode;
    }

    public Integer getLimitTemperature() {
        return limitTemperature;
    }

    public void setLimitTemperature(Integer limitTemperature) {
        this.limitTemperature = limitTemperature;
    }

    public Integer getLimitDay() {
        return limitDay;
    }

    public void setLimitDay(Integer limitDay) {
        this.limitDay = limitDay;
    }

    public String getSourceProviderKey() {
        return sourceProviderKey;
    }

    public void setSourceProviderKey(String sourceProviderKey) {
        this.sourceProviderKey = sourceProviderKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        City city = (City) o;

        if (!Objects.equals(id, city.id)) {
            return false;
        }
        if (!Objects.equals(name, city.name)) {
            return false;
        }
        if (!Objects.equals(isoCountryCode, city.isoCountryCode)) {
            return false;
        }

        if (!Objects.equals(limitTemperature, city.limitTemperature)) {
            return false;
        }

        if (!Objects.equals(limitDay, city.limitDay)) {
            return false;
        }
        return Objects.equals(sourceProviderKey, city.sourceProviderKey);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (isoCountryCode != null ? isoCountryCode.hashCode() : 0);
        result = 31 * result + (limitTemperature != null ? limitTemperature.hashCode() : 0);
        result = 31 * result + (limitDay != null ? limitDay.hashCode() : 0);
        result = 31 * result + (sourceProviderKey != null ? sourceProviderKey.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "City{" + "id=" + id +
                ", name='" + name + '\'' +
                ", isoCountryCode='" + isoCountryCode + '\'' +
                ", limitTemperature='" + limitTemperature + '\'' +
                ", limitDay='" + limitDay + '\'' +
                ", sourceProviderKey='" + sourceProviderKey + '\'' +
                '}';
    }
}
