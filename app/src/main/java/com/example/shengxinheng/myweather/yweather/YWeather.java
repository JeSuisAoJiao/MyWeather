package com.example.shengxinheng.myweather.yweather;

import com.example.shengxinheng.myweather.datamodel.Location;
import com.example.shengxinheng.myweather.datamodel.Weather;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;

public class YWeather {

    private final YWebService service;
    private Map<String, String> weatherQuery = new HashMap<>();
    private Map<String, String> locationQuery = new HashMap<>();
    private static final String StatementPrefix1 = "select * from weather.forecast where woeid = %s and u = 'c'";
    private static final String UnitStatement = " and u = 'c'";
    private static final String StatementPrefix2 = "select * from geo.places where text = '%s'";
    private static final String StatementPrefix3 = "select * from weather.forecast where woeid in (SELECT woeid FROM geo.places WHERE text='(%f,%f)') and u = 'c'";

    public YWeather(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://query.yahooapis.com").build();
        service = retrofit.create(YWebService.class);
        weatherQuery.put("format", "xml");
        //weatherQuery.put("env", "store%3A%2F%2Fdatatables.org%2Falltableswithkeys");
        locationQuery.put("format", "xml");
    }

    public Weather fetchWeather(Location location) throws IOException {
        weatherQuery.put("q", String.format(StatementPrefix1, ((YLocation)location).getWoeid()));
        Response<ResponseBody> response = service.query(weatherQuery).execute();
        try(ResponseBody body = response.body()){
            InputStream in = body.byteStream();
            YWeatherParser parser = new YWeatherParser();
            Weather weather = parser.parse(in);
            weather.setLocation(location);
            return weather;
        }
    }

    public List fetchLocations(String city) throws IOException {
        locationQuery.put("q", String.format(StatementPrefix2, city));
        Response<ResponseBody> response = service.query(locationQuery).execute();
        try(ResponseBody body = response.body()){
            InputStream in = body.byteStream();
            YLocationParser parser = new YLocationParser();
            return parser.parse(in);
        }
    }

    public Weather fetchWeather(android.location.Location location) throws IOException {
        weatherQuery.put("q", String.format(StatementPrefix3, location.getLatitude(), location.getLongitude()));
        Response<ResponseBody> response = service.query(weatherQuery).execute();
        try(ResponseBody body = response.body()){
            InputStream in = body.byteStream();
            YWeatherParser parser = new YWeatherParser();
            return parser.parse(in);
        }
    }
}
