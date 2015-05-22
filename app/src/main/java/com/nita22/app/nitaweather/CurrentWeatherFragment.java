package com.nita22.app.nitaweather;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private TextView temp1;
    private TextView currentDateText;
    private TextView currenttemp;
    private TextView humidity;
    private TextView wind;
    private SwipeRefreshLayout swipeLayout;
    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.weather_layout, container, false);
        swipeLayout = (SwipeRefreshLayout) myview.findViewById(R.id.swipe_container);
        weatherInfoLayout = (LinearLayout) myview.findViewById(R.id.weather_info_layout);
        cityNameText = (TextView) myview.findViewById(R.id.city_name);
        publishText = (TextView) myview.findViewById(R.id.publish_text);
        weatherDespText = (TextView) myview.findViewById(R.id.weather_desp);
        temp1 = (TextView) myview.findViewById(R.id.temp);
        currentDateText = (TextView) myview.findViewById(R.id.current_date);
        currenttemp = (TextView) myview.findViewById(R.id.current_temp);
        humidity = (TextView) myview.findViewById(R.id.humidity);
        wind = (TextView) myview.findViewById(R.id.wind);
        imageView = (ImageView) myview.findViewById(R.id.image);

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
            queryCityCode(countyName);
        } else {
            showWeather();
        }
        showWeather();
        return myview;
    }

    private void queryCityCode(String countyName) {
        try {
            countyName = URLEncoder.encode(countyName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String address = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.places%20where%20text%3D%22" + countyName + "\"&format=json";
        queryFromServer(address, "countyCode");
    }

    private void queryWeatherInfo(String countyCode) {
        String address = "http://weather.yahooapis.com/forecastrss?w=" + countyCode + "&u=c";
        queryFromServer(address, "countyName");
    }

    private void queryFromServer(final String address, final String type) {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                if ("countyCode".equals(type)) {
                    String cityCode = Utility.handlecitycode(response);
                    queryWeatherInfo(cityCode);
                } else if ("countyName".equals(type)) {
                    Utility.handleWeatherResponse(getActivity(), response);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
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
        Log.e("CurrentWeatherFragment", prefs.getString("city_name", ""));
        Log.e("CurrentWeatherFragment", prefs.getString("current_temp", ""));
        Log.e("CurrentWeatherFragment", prefs.getString("day0temp1", ""));
        Log.e("CurrentWeatherFragment", prefs.getString("day0temp2", ""));
        Log.e("CurrentWeatherFragment", prefs.getString("weather_desp", ""));
        Log.e("CurrentWeatherFragment", prefs.getString("publish_time", ""));
        Log.e("CurrentWeatherFragment", prefs.getString("current_date", ""));
        Log.e("CurrentWeatherFragment", prefs.getString("humidity", ""));
        Log.e("CurrentWeatherFragment", prefs.getString("windSpeed", ""));
        Log.e("CurrentWeatherFragment", prefs.getString("weatherCode", ""));
        currenttemp.setText(prefs.getString("current_temp", "") + "°");
        temp1.setText(prefs.getString("day0temp1", "") + "°" + "~  " + prefs.getString("day0temp2", "") + "°");
        weatherDespText.setText(prefs.getString("weather_desp", ""));
        publishText.setText(getString(R.string.update_time) + prefs.getString("publish_time", ""));
        currentDateText.setText(prefs.getString("current_date", ""));
        humidity.setText("降水概率:" + prefs.getString("humidity", "") + "%");
        wind.setText("风速:" + prefs.getString("windSpeed", "") + "km/h");
        showImage(prefs.getString("weatherCode", "3200"));
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

    public void showImage(String weatherCode) {
        switch (weatherCode) {
            case "0":
                imageView.setImageResource(R.drawable.weather36);
                break;
            case "1":
                imageView.setImageResource(R.drawable.weather35);
                break;
            case "2":
                imageView.setImageResource(R.drawable.weather34);
                break;
            case "3":
                imageView.setImageResource(R.drawable.weather18);
                break;
            case "4":
                imageView.setImageResource(R.drawable.weather16);
                break;
            case "5":
                imageView.setImageResource(R.drawable.weather20);
                break;
            case "6":
                imageView.setImageResource(R.drawable.weather20);
                break;
            case "7":
                imageView.setImageResource(R.drawable.weather20);
                break;
            case "8":
                imageView.setImageResource(R.drawable.weather19);
                break;
            case "9":
                imageView.setImageResource(R.drawable.weather13);
                break;
            case "10":
                imageView.setImageResource(R.drawable.weather19);
                break;
            case "11":
                imageView.setImageResource(R.drawable.weather10);
                break;
            case "12":
                imageView.setImageResource(R.drawable.weather10);
                break;
            case "13":
                imageView.setImageResource(R.drawable.weather21);
                break;
            case "14":
                imageView.setImageResource(R.drawable.weather22);
                break;
            case "15":
                imageView.setImageResource(R.drawable.weather23);
                break;
            case "16":
                imageView.setImageResource(R.drawable.weather24);
                break;
            case "17":
                imageView.setImageResource(R.drawable.weather12);
                break;
            case "18":
                imageView.setImageResource(R.drawable.weather12);
                break;
            case "19":
                imageView.setImageResource(R.drawable.weather26);
                break;
            case "20":
                imageView.setImageResource(R.drawable.weather30);
                break;
            case "21":
                imageView.setImageResource(R.drawable.weather31);
                break;
            case "22":
                imageView.setImageResource(R.drawable.weather27);
                break;
            case "23":
                imageView.setImageResource(R.drawable.weather33);
                break;
            case "24":
                imageView.setImageResource(R.drawable.weather32);
                break;
            case "25":
                imageView.setImageResource(R.drawable.weather37);
                break;
            case "26":
                imageView.setImageResource(R.drawable.weather04);
                break;
            case "27":
                imageView.setImageResource(R.drawable.weather08);
                break;
            case "28":
                imageView.setImageResource(R.drawable.weather07);
                break;
            case "29":
                imageView.setImageResource(R.drawable.weather06);
                break;
            case "30":
                imageView.setImageResource(R.drawable.weather05);
                break;
            case "31":
                imageView.setImageResource(R.drawable.weather01);
                break;
            case "32":
                imageView.setImageResource(R.drawable.weather00);
                break;
            case "33":
                imageView.setImageResource(R.drawable.weather03);
                break;
            case "34":
                imageView.setImageResource(R.drawable.weather02);
                break;
            case "35":
                imageView.setImageResource(R.drawable.weather12);
                break;
            case "36":
                imageView.setImageResource(R.drawable.weather38);
                break;
            case "37":
                imageView.setImageResource(R.drawable.weather11);
                break;
            case "38":
                imageView.setImageResource(R.drawable.weather11);
                break;
            case "39":
                imageView.setImageResource(R.drawable.weather11);
                break;
            case "40":
                imageView.setImageResource(R.drawable.weather10);
                break;
            case "41":
                imageView.setImageResource(R.drawable.weather24);
                break;
            case "42":
                imageView.setImageResource(R.drawable.weather10);
                break;
            case "43":
                imageView.setImageResource(R.drawable.weather24);
                break;
            case "44":
                imageView.setImageResource(R.drawable.weather07);
                break;
            case "45":
                imageView.setImageResource(R.drawable.weather11);
                break;
            case "46":
                imageView.setImageResource(R.drawable.weather21);
                break;
            case "47":
                imageView.setImageResource(R.drawable.weather11);
                break;
            case "3200":
                imageView.setImageResource(R.drawable.weather99);
                break;
            default:
                imageView.setImageResource(R.drawable.weather99);
                break;
        }
    }
}
