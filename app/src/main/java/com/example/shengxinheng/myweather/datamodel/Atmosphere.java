package com.example.shengxinheng.myweather.datamodel;

public class Atmosphere {
    private String humidity;
    private String visibility;

    public Atmosphere(String humidity, String visibility){
        this.humidity = humidity;
        this.visibility = visibility;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getHumidity() {
        return humidity;
    }
}
