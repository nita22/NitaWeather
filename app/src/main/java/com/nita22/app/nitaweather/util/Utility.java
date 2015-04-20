package com.nita22.app.nitaweather.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {

    public static void handleWeatherResponse(Context context, String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("retData");
            String cityName = weatherInfo.getString("city");
            String temp1 = weatherInfo.getString("l_tmp");
            String temp2 = weatherInfo.getString("h_tmp");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("time");
            String currentTemp = weatherInfo.getString("temp");
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
