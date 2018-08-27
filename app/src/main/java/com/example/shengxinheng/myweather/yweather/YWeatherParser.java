package com.example.shengxinheng.myweather.yweather;

import android.util.Xml;

import com.example.shengxinheng.myweather.datamodel.Weather;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class YWeatherParser {

    private XmlPullParser parser;
    private Weather weather;

    public YWeatherParser(){
        parser = Xml.newPullParser();
    }

    public Weather parse(InputStream in){
        weather = new Weather();
        try {
            readQuery(in);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return weather;
    }

    private void readQuery(InputStream in) throws XmlPullParserException, IOException {
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in, null);
        parser.nextTag();
        parser.require(XmlPullParser.START_TAG, null, "query");
        while(parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("results")) {
                readResults();
            } else {
                skip(parser);
            }
        }
    }

    private void readResults() throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "results");
        while(parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("channel")) {
                readChannel();
            } else {
                skip(parser);
            }
        }
    }

    private void readChannel() throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "channel");
        while(parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("yweather:wind")) {
                weather.setWind(YParserUtil.YParseWind(parser));
            } else if(name.equals("yweather:atmosphere")){
                weather.setAtmosphere(YParserUtil.YParseAtmosphere(parser));
            } else if(name.equals("yweather:location")){
                weather.setLocation(YParserUtil.YParseLocation(parser));
            } else if(name.equals("yweather:astronomy")){
                weather.setAstronomy(YParserUtil.YParseAstronomy(parser));
            }else if(name.equals("item")){
                readItem();
            }
            else {
                skip(parser);
            }
        }
    }

    private void readItem() throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "item");
        while(parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("yweather:condition")) {
                weather.setCondition(YParserUtil.YParseCondition(parser));
            } else if (name.equals("yweather:forecast")) {
                weather.addForecast(YParserUtil.YParseForecast(parser));
            }else {
                skip(parser);
            }
        }
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
