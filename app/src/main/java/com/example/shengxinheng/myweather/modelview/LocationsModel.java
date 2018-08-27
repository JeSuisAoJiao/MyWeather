package com.example.shengxinheng.myweather.modelview;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

public class LocationsModel extends ViewModel {

    private MutableLiveData<List> locations;

    public MutableLiveData<List> getLocations() {
        if(locations == null){
            locations = new MutableLiveData<>();
        }
        return locations;
    }
}
