package com.sister.projectpjmc;

import android.app.Activity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class KmlParser {
    MapsActivity act;

    public KmlParser(MapsActivity act){
        this.act = act;
    }

    public ArrayList<HashMap<String, String>> parseXml() throws XmlPullParserException, IOException {
        StringBuffer buffer = new StringBuffer();

        InputStream ins = act.getResources().openRawResource(act.getResources().getIdentifier("takoyaki", "raw", act.getPackageName()));
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(ins, "utf-8");

        String tag = "";
        int eventType = xpp.getEventType();
        boolean isStartTag = false;
        ArrayList<HashMap<String, String>> searchContents = new ArrayList<>();
        HashMap<String, String> placeMarkContent = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    tag = xpp.getName();
                    if (tag.equals("Placemark")) {
                        placeMarkContent = new HashMap<>();
                    } else if (tag.equals("name") || tag.equals("description") || tag.equals("coordinates")) {
                        isStartTag = true;
                    }
                    break;
                case XmlPullParser.TEXT:
                    String text = xpp.getText();
                    if (isStartTag && placeMarkContent != null && tag.equals("name")) {
                        placeMarkContent.put("name", text);
//                        Log.e("text name", text);
                    } else if (isStartTag && placeMarkContent != null && tag.equals("description")) {
                        placeMarkContent.put("description", text);
                    } else if (isStartTag && placeMarkContent != null && tag.equals("coordinates")) {
                        placeMarkContent.put("coordinates", text);
//                        Log.e("text coordinates", text);
//                        Log.e("text coordinates map", placeMarkContent.toString());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    tag = xpp.getName();

                    if (tag.equals("Placemark")) {
                        searchContents.add(placeMarkContent);
//                        Log.e("text placeMarkContent", placeMarkContent.toString());
                        placeMarkContent = null;
                    } else if (tag.equals("name") || tag.equals("description") || tag.equals("coordinates")) {
                        isStartTag = false;
                    }
                    break;
            }
            eventType = xpp.next();
        }

//        Log.e("XmlParser", searchContents.toString());
        return searchContents;

    }
}
