package com.nita22.app.nitaweather;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by nita22 on 2015/5/2 0002.
 */
public class WeatherFutureFragment extends Fragment {
    private View myview;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.weather_future, container, false);
        return myview;
    }
}
