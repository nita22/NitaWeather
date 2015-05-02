package com.nita22.app.nitaweather;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by nita22 on 2015/5/1 0001.
 */
public class CurrentWeatherFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private View myview;
    private LinearLayout weatherInfoLayout;
    private TextView cityNameText;
    private TextView publishText;
    private TextView weatherDespText;
    private TextView temp1Text;
    private TextView temp2Text;
    private TextView currentDateText;
    private TextView currenttemp;
    private SwipeRefreshLayout swipeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.weather_layout, container, false);
        swipeLayout = (SwipeRefreshLayout) myview.findViewById(R.id.swipe_container);
        weatherInfoLayout = (LinearLayout) myview.findViewById(R.id.weather_info_layout);
        cityNameText = (TextView) myview.findViewById(R.id.city_name);
        publishText = (TextView) myview.findViewById(R.id.publish_text);
        weatherDespText = (TextView) myview.findViewById(R.id.weather_desp);
        temp1Text = (TextView) myview.findViewById(R.id.temp1);
        temp2Text = (TextView) myview.findViewById(R.id.temp2);
        currentDateText = (TextView) myview.findViewById(R.id.current_date);
        currenttemp = (TextView) myview.findViewById(R.id.current_temp);

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        String countyName = getActivity().getIntent().getStringExtra("county_name");
        if (!TextUtils.isEmpty(countyName)) {
            publishText.setText(getString(R.string.syncing));
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeatherInfo(countyName);
        } else {
            showWeather();
        }
        showWeather();
        return myview;
    }

    private void queryWeatherInfo(String countyName) {
        try {
            countyName = URLEncoder.encode(countyName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String address = "https://api.thinkpage.cn/v2/weather/all.json?city=" + countyName + "&language=zh-chs&unit=c&aqi=city&key=75HF115KA5";
        queryFromServer(address);
    }

    private void queryFromServer(final String address) {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                Utility.handleWeatherResponse(getActivity(), response);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText(getString(R.string.sync_failure));
                    }
                });
            }
        });
    }

    private void showWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        cityNameText.setText(prefs.getString("city_name", ""));
        currenttemp.setText(prefs.getString("current_temp", "") + "°C");
        temp1Text.setText(prefs.getString("temp1", "") + "°C");
        temp2Text.setText(prefs.getString("temp2", "") + "°C");
        weatherDespText.setText(prefs.getString("weather_desp", ""));
        publishText.setText(getString(R.string.update_time) + prefs.getString("publish_time", ""));
        currentDateText.setText(prefs.getString("current_date", ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String cityNameCode = prefs.getString("city_name", "");
        publishText.setText(R.string.syncing);
        if (!TextUtils.isEmpty(cityNameCode)) {
            queryWeatherInfo(cityNameCode);
        }
    }
}
