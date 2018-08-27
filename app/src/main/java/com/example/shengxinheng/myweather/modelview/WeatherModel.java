package com.example.shengxinheng.myweather.modelview;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.shengxinheng.myweather.datamodel.Weather;

public class WeatherModel extends ViewModel {

    private MutableLiveData<Weather> weather;

    public MutableLiveData<Weather> getWeather() {
        if(weather == null){
            weather = new MutableLiveData<>();
        }
        return weather;
    }
}
