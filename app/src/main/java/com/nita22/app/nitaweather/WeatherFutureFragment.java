package com.nita22.app.nitaweather;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by nita22 on 2015/5/2 0002.
 */
public class WeatherFutureFragment extends Fragment {
    private View myview;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    ImageView imageView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.weather_future, container, false);
        textView1 = (TextView) myview.findViewById(R.id.day1);
        textView2 = (TextView) myview.findViewById(R.id.day2);
        textView3 = (TextView) myview.findViewById(R.id.day3);
        imageView = (ImageView) myview.findViewById(R.id.image);

        showWeatherFuture();

        return myview;
    }

    private void showWeatherFuture() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        textView1.setText(prefs.getString("date1", "")+"   "+prefs.getString("daytext1", "")+"   "+prefs.getString("day1temp1", "") + "°C"+"~"+prefs.getString("day1temp2", "")+ "°C");
        textView2.setText(prefs.getString("date2", "")+"   "+prefs.getString("daytext2", "")+"   "+prefs.getString("day2temp1", "") + "°C"+"~"+prefs.getString("day2temp2", "")+ "°C");
        textView3.setText(prefs.getString("date3", "")+"   "+prefs.getString("daytext3", "")+"   "+prefs.getString("day3temp1", "") + "°C"+"~"+prefs.getString("day3temp2", "")+ "°C");
        String weatherCode = prefs.getString("weatherCode", "99");
        Log.e("WeatherFutureFragment",weatherCode);
        showImage(weatherCode);
    }

    public void showImage(String weatherCode){
        switch (weatherCode){
            case "0":
                imageView.setImageResource(R.drawable.weather00);
                break;
            case "1":
                imageView.setImageResource(R.drawable.weather01);
                break;
            case "99":
                imageView.setImageResource(R.drawable.weather99);
                break;
            default:
                imageView.setImageResource(R.drawable.weather99);
                break;
        }
    }
}
