package com.test.weather.net;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by User on 04.06.2015.
 */
public class Network implements NetworkInterface {

    String requestedCity;
    ResultListener resultListener;
    Context context;
    GsonBuilder gsonBuilder;
    Gson gson;
    WeatherPOJO weatherPOJO;


    public Network() {
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

    }

    @Override
    public void requestUpdate(String cityName, final ResultListener resultListener) {
        requestedCity = cityName;
        this.resultListener = resultListener;

        String url = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + requestedCity + "&mode=json&units=metric&cnt=5";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                weatherPOJO = gson.fromJson(jsonObject.toString(), WeatherPOJO.class);
                if (weatherPOJO.getCod() == 200)
                resultListener.onUpdateCompleted(getPhotoURL(), getWeatherInformation());
                else resultListener.onError("Bad code returned: " + weatherPOJO.getCod());


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                resultListener.onError(volleyError.getMessage());
            }
        });

// Access the RequestQueue through your singleton class.
        VolleySingleton.getInstance(context).addToRequestQueue(jsObjRequest);

    }

    private String getPhotoURL() {
        return "http://ukrainetrek.com/images/lviv-ukraine-city-views-17.jpg";
    }

    private ArrayList<WeatherInfo> getWeatherInformation() {
        ArrayList<WeatherInfo> weatherInformation= new ArrayList<WeatherInfo>();

        for (WeatherPOJO.List list : weatherPOJO.getList()){
             Date date = new Date(list.getDt());
             SimpleDateFormat simpleDateformat = new SimpleDateFormat("E");


            WeatherInfo weatherInfo = new WeatherInfo(
                    simpleDateformat.format(date),
                    requestedCity,
                    list.getWeather()[0].getIcon(),
                    (int) list.getTemp().getMin(),
                    (int) list.getTemp().getMax(),
                    (int) list.getHumidity(),
                    (int) list.getPressure(),
                    list.getSpeed(),
                    list.getDeg()
                    );

            weatherInformation.add(weatherInfo);
        }
        return weatherInformation;
    }


}
