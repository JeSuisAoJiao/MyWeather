package com.example.shengxinheng.myweather.datamodel;

public class Forecast {
    private String date;
    private String day;
    private String high, low;
    private String desc;

    public Forecast(String date, String day, String high, String low, String desc){
        this.date = date;
        this.day = day;
        this.high = high;
        this.low = low;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public String getHigh() {
        return high;
    }

    public String getLow() {
        return low;
    }
}
