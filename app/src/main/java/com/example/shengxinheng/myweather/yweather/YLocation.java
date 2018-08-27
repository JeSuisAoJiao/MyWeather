package com.example.shengxinheng.myweather.yweather;

import com.example.shengxinheng.myweather.datamodel.Location;

public class YLocation extends Location{
    private final String woeid;

    public YLocation(final String woeid, final String name){
        super(name);
        this.woeid = woeid;
    }

    public String getWoeid() {
        return woeid;
    }
}
