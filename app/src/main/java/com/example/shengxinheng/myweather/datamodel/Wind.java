package com.example.shengxinheng.myweather.datamodel;

public class Wind {
    private String speed;
    private int direction;

    public Wind(String speed, String direction){
        this.speed = speed;
        this.direction = Integer.valueOf(direction);
    }

    public String getSpeed() {
        return speed;
    }

    public String getDirection() {
        if(direction == 0){
            return "-";
        } else if(direction < 45){
            return "N-NE";
        } else if(direction == 45){
            return "NE";
        } else if(direction < 90){
            return "E-NE";
        } else if(direction == 90){
            return "E";
        } else if(direction < 135){
            return "E-SE";
        } else if(direction == 135){
            return "SE";
        } else if(direction < 180){
            return "S-SE";
        } else if(direction == 180){
            return "S";
        } else if(direction < 225){
            return "S-SW";
        } else if(direction == 225){
            return "SW";
        } else if(direction < 270){
            return "W-SW";
        } else if(direction == 270){
            return "W";
        } else if(direction < 315){
            return "W-NW";
        } else if(direction == 315){
            return "NW";
        } else if(direction < 360){
            return "N-NW";
        } else if(direction == 360){
            return "N";
        } else {
            return "Null";
        }
    }
}
