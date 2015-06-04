package com.test.weather.net;

import java.util.ArrayList;

/**
 * Created by User on 04.06.2015.
 */
public interface ResultListener {

    void onUpdateCompleted(String photoUrl, ArrayList<WeatherInfo> weatherInfos);

    void onError(String errorMessage);


}
