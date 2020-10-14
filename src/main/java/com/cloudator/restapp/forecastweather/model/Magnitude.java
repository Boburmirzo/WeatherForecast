package com.cloudator.restapp.forecastweather.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Immutable class to model a mathematical magnitude
 * Created by Bobur on 11.10.2020.
 */
public final class Magnitude implements Serializable {

    private static final long serialVersionUID = -571080056907945480L;

    private LocalDateTime from;
    private LocalDateTime to;

    private String unit;

    private double value;

    public Magnitude() {
    }

    public Magnitude(LocalDateTime from,
                     LocalDateTime to,
                     String unit,
                     double value) {
        this.from = from;
        this.to = to;
        this.unit = unit;
        this.value = value;
    }

    public Magnitude(String unit, double value) {
        this.unit = unit;
        this.value = value;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }

    public String getUnit() {
        return unit;
    }

    public double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Magnitude magnitude = (Magnitude) o;

        if (Double.compare(magnitude.value, value) != 0) {
            return false;
        }
        return Objects.equals(unit, magnitude.unit);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = unit != null ? unit.hashCode() : 0;
        temp = Double.doubleToLongBits(value);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Magnitude{" + "unit='" + unit + '\'' +
                ", value=" + value +
                '}';
    }
}
