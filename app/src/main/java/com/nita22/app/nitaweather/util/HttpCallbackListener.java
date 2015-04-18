package com.nita22.app.nitaweather.util;


public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);
}
