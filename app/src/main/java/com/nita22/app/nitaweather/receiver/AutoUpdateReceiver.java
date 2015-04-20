package com.nita22.app.nitaweather.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nita22.app.nitaweather.service.AutoUpdateService;

public class AutoUpdateReceiver extends BroadcastReceiver{

    public void onReceive(Context context, Intent intent){
        Intent i = new Intent(context, AutoUpdateService.class);
        context.startService(i);
    }
}
