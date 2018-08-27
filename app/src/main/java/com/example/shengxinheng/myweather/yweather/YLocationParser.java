package com.example.shengxinheng.myweather.yweather;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class YLocationParser {
    private XmlPullParser parser;
    private List locations;

    public YLocationParser(){
        parser = Xml.newPullParser();
    }

    public List parse(InputStream in){
        locations = new ArrayList();
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
        return locations;
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
            if (name.equals("place")) {
                readPlace();
            } else {
                skip(parser);
            }
        }
    }

    private void readPlace() throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "place");
        String woeid = "";
        String place = "";
        String fullname = "";
        while(parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("woeid")) {
                woeid = readText(parser);
            } else if(name.equals("name")){
                place = readText(parser);
            }else if(name.equals("country")){
                fullname = readText(parser).concat(fullname);
            }else if(name.equals("admin1")){
                fullname = readText(parser).concat(", ").concat(fullname);
            }else if(name.equals("admin2")){
                fullname = readText(parser).concat(", ").concat(fullname);
            }else if(name.equals("admin3")){
                String admin3 = readText(parser);
                if(admin3 != null && !admin3.equals(""))
                    fullname = readText(parser).concat(", ").concat(fullname);
                fullname = place.concat(", ").concat(fullname);
                locations.add(new YLocation(woeid, fullname));
            }
            else {
                skip(parser);
            }
        }
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
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
