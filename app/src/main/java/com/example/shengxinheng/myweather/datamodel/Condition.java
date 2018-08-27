package com.example.shengxinheng.myweather.datamodel;

public class Condition {
    private String date, temperature, desc;

    public Condition(String date, String temperature, String desc){
        this.date = date;
        this.desc = desc;
        this.temperature = temperature;
    }

    public String getDate() {
        return date;
    }

    public String getDesc() {
        return desc;
    }

    public String getTemperature() {
        return temperature;
    }
}
