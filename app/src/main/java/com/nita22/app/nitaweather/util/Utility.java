package com.nita22.app.nitaweather.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {

    public static void handleWeatherResponse(Context context, String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray weatherArray = jsonObject.getJSONArray("weather");
            JSONObject countyInfo = weatherArray.getJSONObject(0);
            String publishTime = countyInfo.getString("last_update");
            String cityName = countyInfo.getString("city_name");
            JSONObject nowWeatherInfo = countyInfo.getJSONObject("now");
            String weatherDesp = nowWeatherInfo.getString("text");
            String currentTemp = nowWeatherInfo.getString("temperature");
            JSONArray futureArray = countyInfo.getJSONArray("future");
            JSONObject futureWeatherInfo = futureArray.getJSONObject(0);
            String temp1 = futureWeatherInfo.getString("low");
            String temp2 = futureWeatherInfo.getString("high");
            saveWeatherInfo(context, cityName, temp1, temp2, weatherDesp, publishTime, currentTemp);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public static void saveWeatherInfo(Context context, String cityName, String temp1, String temp2, String weatherDesp, String publishTime, String currentTemp){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("current_temp", currentTemp);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_date", sdf.format(new Date()));
        editor.commit();
    }
}
