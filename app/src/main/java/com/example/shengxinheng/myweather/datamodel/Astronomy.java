package com.example.shengxinheng.myweather.datamodel;

public class Astronomy {
    private String sunrise, sunset;

    public Astronomy(String sunrise, String sunset){
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }
}
