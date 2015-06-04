package com.test.weather.net;

/**
 * Created by User on 04.06.2015.
 */
public interface NetworkInterface {

    void requestUpdate(String cityName);

    void requestUpdate(int longitude, int latitude);





}
