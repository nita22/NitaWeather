package com.nita22.app.nitaweather;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by nita22 on 2015/5/2 0002.
 */
public class WeatherFutureFragment extends Fragment {

    private View myview;
    TextView date1;
    TextView text1;
    TextView temp1;
    TextView date2;
    TextView text2;
    TextView temp2;
    TextView date3;
    TextView text3;
    TextView temp3;
    TextView date4;
    TextView text4;
    TextView temp4;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.weather_future, container, false);
        date1 = (TextView) myview.findViewById(R.id.date1);
        text1 = (TextView) myview.findViewById(R.id.text1);
        temp1 = (TextView) myview.findViewById(R.id.temp1);
        date2 = (TextView) myview.findViewById(R.id.date2);
        text2 = (TextView) myview.findViewById(R.id.text2);
        temp2 = (TextView) myview.findViewById(R.id.temp2);
        date3 = (TextView) myview.findViewById(R.id.date3);
        text3 = (TextView) myview.findViewById(R.id.text3);
        temp3 = (TextView) myview.findViewById(R.id.temp3);
        date4 = (TextView) myview.findViewById(R.id.date4);
        text4 = (TextView) myview.findViewById(R.id.text4);
        temp4 = (TextView) myview.findViewById(R.id.temp4);
        showWeatherFuture();

        return myview;
    }

    private void showWeatherFuture() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        date1.setText(prefs.getString("date1", ""));
        text1.setText(prefs.getString("daytext1", ""));
        temp1.setText(prefs.getString("day1temp1", "")+"°"+"~  "+prefs.getString("day1temp2", "")+"°");

        date2.setText(prefs.getString("date2", ""));
        text2.setText(prefs.getString("daytext2", ""));
        temp2.setText(prefs.getString("day2temp1", "")+"°"+"~  "+prefs.getString("day2temp2", "")+"°");

        date3.setText(prefs.getString("date3", ""));
        text3.setText(prefs.getString("daytext3", ""));
        temp3.setText(prefs.getString("day3temp1", "")+"°"+"~  "+prefs.getString("day3temp2", "")+"°");

        date4.setText(prefs.getString("date4", ""));
        text4.setText(prefs.getString("daytext4", ""));
        temp4.setText(prefs.getString("day4temp1", "")+"°"+"~  "+prefs.getString("day4temp2", "")+"°");
    }


}
