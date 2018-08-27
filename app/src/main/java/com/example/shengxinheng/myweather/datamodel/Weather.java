package com.example.shengxinheng.myweather.datamodel;

import java.util.ArrayList;
import java.util.List;

public class Weather {
    private Location location;

    private List forecast = new ArrayList();
    private Wind wind;
    private Atmosphere atmosphere;
    private Astronomy astronomy;
    private Condition condition;

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setAstronomy(Astronomy astronomy) {
        this.astronomy = astronomy;
    }

    public Astronomy getAstronomy() {
        return astronomy;
    }

    public void setAtmosphere(Atmosphere atmosphere) {
        this.atmosphere = atmosphere;
    }

    public Atmosphere getAtmosphere() {
        return atmosphere;
    }

    public void addForecast(Forecast forecast) {
        this.forecast.add(forecast);
    }

    public List getForecast() {
        return forecast;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Wind getWind() {
        return wind;
    }
}
