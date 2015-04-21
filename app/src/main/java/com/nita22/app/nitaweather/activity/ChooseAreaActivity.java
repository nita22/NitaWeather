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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nita22.app.nitaweather.R;

public class ChooseAreaActivity extends Activity{

    private TextView titleText;
    private Button button;
    private EditText editText;

    private boolean isFromWeatherActivity;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getBoolean("city_selected", false) && !isFromWeatherActivity){
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        button =(Button)findViewById(R.id.button);
        editText = (EditText)findViewById(R.id.edit_text);
        titleText = (TextView)findViewById(R.id.title_text);
        titleText.setText("中国天气");
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
                }else{
                    Toast.makeText(ChooseAreaActivity.this, "城市不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onBackPressed(){
        if(isFromWeatherActivity){
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
