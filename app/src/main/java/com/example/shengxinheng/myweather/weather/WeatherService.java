package com.example.shengxinheng.myweather.weather;

import android.os.AsyncTask;

import com.example.shengxinheng.myweather.datamodel.Location;
import com.example.shengxinheng.myweather.datamodel.Weather;
import com.example.shengxinheng.myweather.helper.WeatherReceiver;
import com.example.shengxinheng.myweather.yweather.YWeather;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherService{

    private final YWeather yWeather;
    private Map<Location, Weather> cachedWeather = new HashMap<>();
    private final WeatherReceiver receiver;

    public WeatherService(WeatherReceiver receiver)
    {
        yWeather = new YWeather();
        this.receiver = receiver;
    }

    public void fetchWeather(Location location) {
        Weather weather = cachedWeather.get(location);
        if(weather != null)
            receiver.receive(weather);
        else {
            HttpTask task = new HttpTask();
            task.execute(location);
        }
    }

    public void fetchWeather(android.location.Location location) {
        Weather weather = cachedWeather.get(location);
        if(weather != null)
            receiver.receive(weather);
        else {
            HttpTask task = new HttpTask();
            task.execute(location);
        }
    }

    public void fetchLocations(String aLocation) {
        HttpTask task = new HttpTask();
        task.execute(aLocation);
    }

    private class HttpTask extends AsyncTask{
        private Location location = null;

        @Override
        protected Object doInBackground(Object... objects) {
            if(objects[0] != null){
                if(objects[0] instanceof Location){
                    Weather weather = null;
                    location = (Location)objects[0];
                    try {
                        weather = yWeather.fetchWeather(location);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return weather;
                } else if(objects[0] instanceof String){
                    String city = (String)objects[0];
                    List locations = null;
                    try {
                        locations = yWeather.fetchLocations(city);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return locations;
                } else if(objects[0] instanceof android.location.Location){
                    Weather weather = null;
                    android.location.Location location = (android.location.Location)objects[0];
                    try {
                        weather = yWeather.fetchWeather(location);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return weather;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object object) {
            if(object instanceof Weather && location != null) {
                cachedWeather.put(location, (Weather)object);
            }
            receiver.receive(object);
        }
    }
}
