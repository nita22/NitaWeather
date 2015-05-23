package com.nita22.app.nitaweather;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {

    public static String handleLocation(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject result = jsonObject.getJSONObject("result");
            JSONObject addressComponent = result.getJSONObject("addressComponent");
            String cityName = addressComponent.getString("city");
            return cityName;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String handlecitycode(String response) {
        String woeId;
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject query = jsonObject.getJSONObject("query");
            JSONObject results = query.getJSONObject("results");
            if (query.getString("count").equals("1")) {
                JSONObject place = results.getJSONObject("place");
                woeId = place.getString("woeid");
            } else {
                JSONArray places = results.getJSONArray("place");
                JSONObject place = places.getJSONObject(0);
                woeId = place.getString("woeid");
            }
            return woeId;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void handleWeatherResponse(Context context, String response) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(response));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
            SimpleDateFormat predict = new SimpleDateFormat("dd MMM yyyy", Locale.US);
            SimpleDateFormat predict1 = new SimpleDateFormat("MMM dd");

            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

            int eventType = parser.next();
            int i = 0;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && "yweather:location".equals(parser.getName())) {
                    String city = parser.getAttributeValue(null, "city");
                    editor.putString("city_name", city);
                } else if (eventType == XmlPullParser.START_TAG && "yweather:condition".equals(parser.getName())) {
                    String text = parser.getAttributeValue(null, "text");
                    String code = parser.getAttributeValue(null, "code");
                    String temp = parser.getAttributeValue(null, "temp");
                    String date = parser.getAttributeValue(null, "date");
                    editor.putBoolean("city_selected", true);
                    editor.putString("current_temp", temp);
                    editor.putString("weather_desp", text);
                    String[] array = date.split("C");
                    String date1 = array[0];
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy KK:mm aa", Locale.US);
                    Date publishTime = dateFormat.parse(date1);
                    SimpleDateFormat sdf1 = new SimpleDateFormat("KK:mm aa");
                    editor.putString("publish_time", sdf1.format(publishTime));
                    editor.putString("current_date", sdf.format(new Date()));
                    editor.putString("weatherCode", code);
                } else if (eventType == XmlPullParser.START_TAG && "yweather:atmosphere".equals(parser.getName())) {
                    String humidity = parser.getAttributeValue(null, "humidity");
                    editor.putString("humidity", humidity);
                } else if (eventType == XmlPullParser.START_TAG && "yweather:wind".equals(parser.getName())) {
                    String windSpeed = parser.getAttributeValue(null, "speed");
                    String windDirection = parser.getAttributeValue(null, "direction");
                    editor.putString("windSpeed", windSpeed);
                    editor.putString("windDirection", windDirection);
                } else if (eventType == XmlPullParser.START_TAG && "yweather:forecast".equals(parser.getName())) {
                    switch (i) {
                        case 0:
                            String low = parser.getAttributeValue(null, "low");
                            String high = parser.getAttributeValue(null, "high");
                            editor.putString("day0temp1", low);
                            editor.putString("day0temp2", high);
                            i++;
                            break;
                        case 1:
                            String date1 = parser.getAttributeValue(null, "date");
                            Date publishTime1 = predict.parse(date1);
                            String low1 = parser.getAttributeValue(null, "low");
                            String high1 = parser.getAttributeValue(null, "high");
                            String text1 = parser.getAttributeValue(null, "text");
                            editor.putString("day1temp1", low1);
                            editor.putString("day1temp2", high1);
                            editor.putString("daytext1", text1);
                            editor.putString("date1", predict1.format(publishTime1));
                            i++;
                            break;
                        case 2:
                            String date2 = parser.getAttributeValue(null, "date");
                            Date publishTime2 = predict.parse(date2);
                            String low2 = parser.getAttributeValue(null, "low");
                            String high2 = parser.getAttributeValue(null, "high");
                            String text2 = parser.getAttributeValue(null, "text");
                            editor.putString("day2temp1", low2);
                            editor.putString("day2temp2", high2);
                            editor.putString("daytext2", text2);
                            editor.putString("date2", predict1.format(publishTime2));
                            i++;
                            break;
                        case 3:
                            String date3 = parser.getAttributeValue(null, "date");
                            Date publishTime3 = predict.parse(date3);
                            String low3 = parser.getAttributeValue(null, "low");
                            String high3 = parser.getAttributeValue(null, "high");
                            String text3 = parser.getAttributeValue(null, "text");
                            editor.putString("day3temp1", low3);
                            editor.putString("day3temp2", high3);
                            editor.putString("daytext3", text3);
                            editor.putString("date3", predict1.format(publishTime3));
                            i++;
                            break;
                        case 4:
                            String date4 = parser.getAttributeValue(null, "date");
                            Date publishTime4 = predict.parse(date4);
                            String low4 = parser.getAttributeValue(null, "low");
                            String high4 = parser.getAttributeValue(null, "high");
                            String text4 = parser.getAttributeValue(null, "text");
                            editor.putString("day4temp1", low4);
                            editor.putString("day4temp2", high4);
                            editor.putString("daytext4", text4);
                            editor.putString("date4", predict1.format(publishTime4));
                            i = 0;
                            break;
                    }
                }
                eventType = parser.next();
            }
            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

