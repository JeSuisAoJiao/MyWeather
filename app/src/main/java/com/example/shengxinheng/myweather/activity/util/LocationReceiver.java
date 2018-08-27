package com.example.shengxinheng.myweather.activity.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import com.example.shengxinheng.myweather.helper.Constants;

public class LocationReceiver extends ResultReceiver {

    public LocationReceiver(Handler handler) {
        super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        String address = resultData.getString(Constants.RESULT_DATA_KEY);
    }
}
