package com.nita22.app.nitaweather;


public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);
}
