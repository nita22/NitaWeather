package com.nita22.app.nitaweather;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ChooseAreaActivity extends ActionBarActivity {

    private TextView titleText;
    private Button button;
    private EditText editText;
    private ImageButton gps;
    private LocationManager locationManager;
    private String provider;
    private Location location;

    private boolean isFromWeatherActivity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("city_selected", false) && !isFromWeatherActivity) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.choose_area);
        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.edit_text);
        titleText = (TextView) findViewById(R.id.title_text);
        gps = (ImageButton) findViewById(R.id.search_location);
        gps.setClickable(true);
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                List<String> providerList = locationManager.getProviders(true);
                if (providerList.contains(LocationManager.GPS_PROVIDER)) {
                    provider = LocationManager.GPS_PROVIDER;
                } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
                    provider = LocationManager.NETWORK_PROVIDER;
                } else {
                    Toast.makeText(getApplicationContext(), "No location provider to use", Toast.LENGTH_SHORT).show();
                    return;
                }
                location = locationManager.getLastKnownLocation(provider);
                if (location != null) {
                    sendLocation();
                }
            }
        });
        titleText.setText(getString(R.string.local_weather));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = editText.getText().toString();
                if (!TextUtils.isEmpty(inputText)) {
                    Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
                    intent.putExtra("county_name", inputText);
                    startActivity(intent);
                    finish();
                    return;
                } else {
                    Toast.makeText(ChooseAreaActivity.this, "城市不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void onBackPressed() {
        if (isFromWeatherActivity) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
        }
        finish();
    }

    public void sendLocation() {
        Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
        intent.putExtra("latitude", location.getLatitude());
        intent.putExtra("longitude", location.getLongitude());
        startActivity(intent);
        finish();
    }
}
