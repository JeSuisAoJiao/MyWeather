package com.example.shengxinheng.myweather.yweather;

import com.example.shengxinheng.myweather.datamodel.Astronomy;
import com.example.shengxinheng.myweather.datamodel.Atmosphere;
import com.example.shengxinheng.myweather.datamodel.Condition;
import com.example.shengxinheng.myweather.datamodel.Forecast;
import com.example.shengxinheng.myweather.datamodel.Location;
import com.example.shengxinheng.myweather.datamodel.Wind;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class YParserUtil {

    public static Wind YParseWind(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "yweather:wind");
        String speed = parser.getAttributeValue(null, "speed");
        String direction = parser.getAttributeValue(null, "direction");
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, null, "yweather:wind");
        return new Wind(speed, direction);
    }

    public static Forecast YParseForecast(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "yweather:forecast");
        String date = parser.getAttributeValue(null, "date");
        String day = parser.getAttributeValue(null, "day");
        String high = parser.getAttributeValue(null, "high");
        String low = parser.getAttributeValue(null, "low");
        String text = parser.getAttributeValue(null, "text");
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, null, "yweather:forecast");
        return new Forecast(date, day, high, low, text);
    }

    public static Condition YParseCondition(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "yweather:condition");
        String date = parser.getAttributeValue(null, "date");
        String temp = parser.getAttributeValue(null, "temp");
        String text = parser.getAttributeValue(null, "text");
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, null, "yweather:condition");
        return new Condition(date, temp, text);
    }

    public static Atmosphere YParseAtmosphere(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "yweather:atmosphere");
        String humidity = parser.getAttributeValue(null, "humidity");
        String visibility = parser.getAttributeValue(null, "visibility");
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, null, "yweather:atmosphere");
        return new Atmosphere(humidity, visibility);
    }

    public static Astronomy YParseAstronomy(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "yweather:astronomy");
        String sunrise = parser.getAttributeValue(null, "sunrise");
        String sunset = parser.getAttributeValue(null, "sunset");
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, null, "yweather:astronomy");
        return new Astronomy(sunrise, sunset);
    }

    public static Location YParseLocation(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "yweather:location");
        String city = parser.getAttributeValue(null, "city");
        String country = parser.getAttributeValue(null, "country");
        String region = parser.getAttributeValue(null, "region");
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, null, "yweather:location");
        return new Location(String.format("%s, %s, %s", city, region, country));
    }
}
