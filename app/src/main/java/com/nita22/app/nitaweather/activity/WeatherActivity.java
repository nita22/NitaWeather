package com.nita22.app.nitaweather.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nita22.app.nitaweather.R;
import com.nita22.app.nitaweather.service.AutoUpdateService;
import com.nita22.app.nitaweather.util.HttpCallbackListener;
import com.nita22.app.nitaweather.util.HttpUtil;
import com.nita22.app.nitaweather.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherActivity extends Activity implements View.OnClickListener{

    private LinearLayout weatherInfoLayout;
    private TextView cityNameText;
    private TextView publishText;
    private TextView weatherDespText;
    private TextView temp1Text;
    private TextView temp2Text;
    private TextView currentDateText;
    private TextView currenttemp;
    private Button switchCity;
    private Button refreshWeather;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);
        weatherInfoLayout = (LinearLayout)findViewById(R.id.weather_info_layout);
        cityNameText = (TextView)findViewById(R.id.city_name);
        publishText = (TextView)findViewById(R.id.publish_text);
        weatherDespText = (TextView)findViewById(R.id.weather_desp);
        temp1Text = (TextView)findViewById(R.id.temp1);
        temp2Text = (TextView)findViewById(R.id.temp2);
        currentDateText = (TextView)findViewById(R.id.current_date);
        switchCity = (Button)findViewById(R.id.switch_city);
        refreshWeather = (Button)findViewById(R.id.refresh_weather);
        currenttemp = (TextView)findViewById(R.id.current_temp);
        switchCity.setOnClickListener(this);
        refreshWeather.setOnClickListener(this);
        String countyName = getIntent().getStringExtra("county_name");
        if(!TextUtils.isEmpty(countyName)){
            publishText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryCityCode(countyName);
        }else{
            showWeather();
        }
    }

    private void queryCityCode(String countyName){
        String address = "http://apistore.baidu.com/microservice/cityinfo?cityname=" + countyName;
        queryFromServer(address, "countyCode");
    }

    private void queryWeatherInfo(String weatherCode){
        String address = "http://apistore.baidu.com/microservice/weather?cityid=" + weatherCode;
        queryFromServer(address, "weatherCode");
    }

    private void queryFromServer(final String address, final String type){
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                if("countyCode".equals(type)){
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject countyInfo = jsonObject.getJSONObject("retData");
                        String countyCode = countyInfo.getString("cityCode");
                        queryWeatherInfo(countyCode);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }else if("weatherCode".equals(type)) {
                    Utility.handleWeatherResponse(WeatherActivity.this, response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步失败");
                    }
                });
            }
        });
    }

    private void showWeather(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(prefs.getString("city_name",""));
        currenttemp.setText(prefs.getString("current_temp", "") + "°C");
        temp1Text.setText(prefs.getString("temp1", "") + "°C");
        temp2Text.setText(prefs.getString("temp2", "") + "°C");
        weatherDespText.setText(prefs.getString("weather_desp", ""));
        publishText.setText("今天" + prefs.getString("publish_time", "") + "发布");
        currentDateText.setText(prefs.getString("current_date", ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.switch_city:
                Intent intent = new Intent(WeatherActivity.this, ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity", true);
                startActivity(intent);
                finish();
                break;
            case R.id.refresh_weather:
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String weatherCode = prefs.getString("city_name", "");
                publishText.setText("同步中...");
                if(!TextUtils.isEmpty(weatherCode)){
                    queryWeatherInfo(weatherCode);
                }
                break;
            default:
                break;
        }
    }
}
