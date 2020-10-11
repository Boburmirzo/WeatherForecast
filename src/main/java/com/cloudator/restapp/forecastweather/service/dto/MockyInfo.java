package com.cloudator.restapp.forecastweather.service.dto;

import java.util.List;

/**
 * Created by Bobur on 11.10.2020
 */
public class MockyInfo {
    private int count;
    private List<MagnitudeInfo> list;

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private MockyCity city;

    public MockyCity getCity() {
        return this.city;
    }

    public void setCity(MockyCity city) {
        this.city = city;
    }

    public List<MagnitudeInfo> getList() {
        return this.list;
    }

    public void setList(List<MagnitudeInfo> list) {
        this.list = list;
    }
}
