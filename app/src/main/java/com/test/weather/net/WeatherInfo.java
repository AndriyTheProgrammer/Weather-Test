package com.test.weather.net;

/**
 * Created by User on 04.06.2015.
 */
public class WeatherInfo {
    private String day, city, iconCode;
    private int minTemp, maxTemp, humidity, pressure;
    private float windSpeed, windDirection;


    public WeatherInfo(String day, String city, String iconCode, int minTemp, int maxTemp, int humidity, int pressure, float windSpeed, float windDirection) {
        this.day = day;
        this.city = city;
        this.iconCode = iconCode;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
    }

    public int getPressure() {
        return pressure;
    }

    public String getIconCode() {
        return iconCode;
    }

    public String getDay() {
        return day;
    }

    public String getCity() {
        return city;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public int getHumidity() {
        return humidity;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public float getWindDirection() {
        return windDirection;
    }
}
