package com.nita22.app.nitaweather;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {

    public static void handleWeatherResponse(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray weatherArray = jsonObject.getJSONArray("weather");
            JSONObject countyInfo = weatherArray.getJSONObject(0);
            String publishTime = countyInfo.getString("last_update");
            String[] publishTimeArray = publishTime.split("[T,+]");
            String publishTime1 = publishTimeArray[1];
            String cityName = countyInfo.getString("city_name");

            JSONObject nowWeatherInfo = countyInfo.getJSONObject("now");
            String weatherCode = nowWeatherInfo.getString("code");
            Log.e("Utility", weatherCode);

            String bodyTemp = nowWeatherInfo.getString("feels_like");
            String windDirection = nowWeatherInfo.getString("wind_direction");
            String windSpeed = nowWeatherInfo.getString("wind_speed");
            String humidity = nowWeatherInfo.getString("humidity");
            String weatherDesp = nowWeatherInfo.getString("text");
            String currentTemp = nowWeatherInfo.getString("temperature");
            JSONObject airQuality = nowWeatherInfo.getJSONObject("air_quality");
            JSONObject airquality = airQuality.getJSONObject("city");
            String quality = airquality.getString("quality");
            JSONArray futureArray = countyInfo.getJSONArray("future");

            JSONObject futureWeatherInfo0 = futureArray.getJSONObject(0);
            String day0temp1 = futureWeatherInfo0.getString("low");
            String day0temp2 = futureWeatherInfo0.getString("high");

            JSONObject futureWeatherInfo1 = futureArray.getJSONObject(1);
            String day1temp1 = futureWeatherInfo1.getString("low");
            String day1temp2 = futureWeatherInfo1.getString("high");
            String daytext1 = futureWeatherInfo1.getString("text");
            String date1 = futureWeatherInfo1.getString("day");

            JSONObject futureWeatherInfo2 = futureArray.getJSONObject(2);
            String day2temp1 = futureWeatherInfo2.getString("low");
            String day2temp2 = futureWeatherInfo2.getString("high");
            String daytext2 = futureWeatherInfo2.getString("text");
            String date2 = futureWeatherInfo2.getString("day");

            JSONObject futureWeatherInfo3 = futureArray.getJSONObject(3);
            String day3temp1 = futureWeatherInfo3.getString("low");
            String day3temp2 = futureWeatherInfo3.getString("high");
            String daytext3 = futureWeatherInfo3.getString("text");
            String date3 = futureWeatherInfo3.getString("day");

            JSONObject futureWeatherInfo4 = futureArray.getJSONObject(4);
            String day4temp1 = futureWeatherInfo4.getString("low");
            String day4temp2 = futureWeatherInfo4.getString("high");
            String daytext4 = futureWeatherInfo4.getString("text");
            String date4 = futureWeatherInfo4.getString("day");

            JSONObject futureWeatherInfo5 = futureArray.getJSONObject(5);
            String day5temp1 = futureWeatherInfo5.getString("low");
            String day5temp2 = futureWeatherInfo5.getString("high");
            String daytext5 = futureWeatherInfo5.getString("text");
            String date5 = futureWeatherInfo5.getString("day");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putBoolean("city_selected", true);
            editor.putString("city_name", cityName);
            editor.putString("current_temp", currentTemp);
            editor.putString("temp1", day0temp1);
            editor.putString("temp2", day0temp2);
            editor.putString("weather_desp", weatherDesp);
            editor.putString("publish_time", publishTime1);
            editor.putString("current_date", sdf.format(new Date()));
            editor.putString("bodyTemp", bodyTemp);
            editor.putString("windDirection", windDirection);
            editor.putString("windSpeed", windSpeed);
            editor.putString("humidity", humidity);
            editor.putString("quality", quality);
            editor.putString("weatherCode", weatherCode);

            editor.putString("day1temp1", day1temp1);
            editor.putString("day1temp2", day1temp2);
            editor.putString("daytext1", daytext1);
            editor.putString("date1", date1);

            editor.putString("day2temp1", day2temp1);
            editor.putString("day2temp2", day2temp2);
            editor.putString("daytext2", daytext2);
            editor.putString("date2", date2);

            editor.putString("day3temp1", day3temp1);
            editor.putString("day3temp2", day3temp2);
            editor.putString("daytext3", daytext3);
            editor.putString("date3", date3);

            editor.putString("day4temp1", day4temp1);
            editor.putString("day4temp2", day4temp2);
            editor.putString("daytext4", daytext4);
            editor.putString("date4", date4);

            editor.putString("day5temp1", day5temp1);
            editor.putString("day5temp2", day5temp2);
            editor.putString("daytext5", daytext5);
            editor.putString("date5", date5);

            editor.commit();


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
