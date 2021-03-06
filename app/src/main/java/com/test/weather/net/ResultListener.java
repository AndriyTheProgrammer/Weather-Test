package com.test.weather.net;

import com.test.weather.net.pojo.WeatherInfo;

import java.util.ArrayList;

/**
 * Created by User on 04.06.2015.
 */
public interface ResultListener {

    void onWeatherReceived(ArrayList<WeatherInfo> weatherInfos);

    void onPhotoReceived(String photoUrl);

    void onError(String errorMessage);


}
