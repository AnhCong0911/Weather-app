package com.example.wa1;

import java.io.Serializable;

public class CityName implements Serializable {
    private String cityName;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public CityName(String cityName) {
        this.cityName = cityName;
    }
}
