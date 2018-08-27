package com.example.shengxinheng.myweather.datamodel;

public class Location {
    private final String fullName;
    private final String shortName;
    private final String region;

    public Location(final String name){
        this.fullName = name;
        if(name.contains(",")) {
            shortName = name.substring(0, name.indexOf(","));
            region = name.substring(name.indexOf(",") + 1);
        } else{
            shortName = name;
            region = "";
        }
    }

    public String getFullName() {
        return fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public String getRegion() {
        return region;
    }
}
