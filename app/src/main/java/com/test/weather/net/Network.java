package com.test.weather.net;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by User on 04.06.2015.
 */
public class Network implements NetworkInterface {

    String requestedCity, photoUrl;
    long cityPhotoFlickrID;
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

        String weatherUrlRequest = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + requestedCity + "&mode=json&units=metric&cnt=5";
        String flickrAlbumUrlRequest = "https://api.flickr.com/services/rest/?method=flickr.photosets.getPhotos&api_key=7f69c27c9e7735a155869d29c1564af9&photoset_id=72157653569823650&user_id=133807839%40N03&format=json&nojsoncallback=1";

        JsonObjectRequest weatherRequest = new JsonObjectRequest(Request.Method.GET, weatherUrlRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                weatherPOJO = gson.fromJson(jsonObject.toString(), WeatherPOJO.class);
                if (weatherPOJO.getCod() == 200)
                resultListener.onWeatherReceived(getWeatherInformation());
                else resultListener.onError("Bad code returned: " + weatherPOJO.getCod());


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                resultListener.onError(volleyError.getMessage());
            }
        });



        JsonObjectRequest albumRequest = new JsonObjectRequest(Request.Method.GET, flickrAlbumUrlRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    JSONArray jsonArray = jsonObject.getJSONObject("photoset").getJSONArray("photo");

                    PhotosetPOJO[] photosetPOJO = gson.fromJson(jsonArray.toString(), PhotosetPOJO[].class);
                    for (int i = 0; i < photosetPOJO.length; i++){
                        if (requestedCity.toLowerCase().equals(photosetPOJO[i].getTitle())){
                            cityPhotoFlickrID = photosetPOJO[i].getId();
                            break;
                        }
                        cityPhotoFlickrID = -1;
                    }
                    if (cityPhotoFlickrID == -1){
                        resultListener.onPhotoReceived(null);
                    }else
                    // if photo in database - get URL of it original
                        requestPhotoUrl();


                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                resultListener.onError(volleyError.getMessage());
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(weatherRequest);
        VolleySingleton.getInstance(context).addToRequestQueue(albumRequest);

    }


    //Generating request with photo ID, as result we getting JSON response with many image resolution URL`s
    private void requestPhotoUrl(){

        String flickrPhotosUrlRequest = "https://api.flickr.com/services/rest/?method=flickr.photos.getSizes&api_key=7f69c27c9e7735a155869d29c1564af9&photo_id=" + cityPhotoFlickrID + "&format=json&nojsoncallback=1";
        JsonObjectRequest photoRequest = new JsonObjectRequest(Request.Method.GET, flickrPhotosUrlRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    PhotoPOJO[] photoPOJOs = gson.fromJson(jsonObject.getJSONObject("sizes").getJSONArray("size").toString(), PhotoPOJO[].class);

                    for (int i = 0; i < photoPOJOs.length; i++){

                        if ("Original".equals(photoPOJOs[i].getLabel())){
                            photoUrl = photoPOJOs[i].getSource();
                            break;
                        }
                        photoUrl = null;

                    }
                    resultListener.onPhotoReceived(photoUrl);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                resultListener.onError(volleyError.getMessage());
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(photoRequest);
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
