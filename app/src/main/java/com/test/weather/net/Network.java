package com.test.weather.net;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.test.weather.net.pojo.PhotoPOJO;
import com.test.weather.net.pojo.PhotosetPOJO;
import com.test.weather.net.pojo.WeatherInfo;
import com.test.weather.net.pojo.WeatherPOJO;

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

    protected String requestedCity, photoUrl;
    protected long cityPhotoFlickrID;

    //Used to communicate with activity
    protected ResultListener resultListener;

    protected Context context;

    //Gson classes
    protected GsonBuilder gsonBuilder;
    protected Gson gson;

    //POJO to keep in it JSON response from Weather API
    protected WeatherPOJO weatherPOJO;


    public Network() {
        // Initializing Gson library
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

    }

    //Called always when MainActivity requests for an update
    @Override
    public void requestUpdate(String cityName, final ResultListener resultListener) {
        requestedCity = cityName;
        this.resultListener = resultListener;

        // Constructing GET requests
        String weatherUrlRequest = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + requestedCity + "&mode=json&units=metric&cnt=5";
        String flickrAlbumUrlRequest = "https://api.flickr.com/services/rest/?method=flickr.photosets.getPhotos&api_key=7f69c27c9e7735a155869d29c1564af9&photoset_id=72157653569823650&user_id=133807839%40N03&format=json&nojsoncallback=1";

        // This request filling WeatherPOJO object with data from Weather API
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



        // This request filling PhotosetPOJO object from Flickr. PhotosetPOJO it's a class with information
        // about all photos in album, and containing name of city and its photo ID for each item in Flickr album
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
                    // Ok, we have photo with requested city, in this method we creating new request with photoID
                    // and retrieving photo's URL
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

        // adding this both request to Volley queue
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

    // Here converting information from WeatherPOJO into easier to understand WeatherInfo
    private ArrayList<WeatherInfo> getWeatherInformation() {
        ArrayList<WeatherInfo> weatherInformation= new ArrayList<WeatherInfo>();

        for (WeatherPOJO.List list : weatherPOJO.getList()){

            Date date = new Date(list.getDt() * 1000);

            String day = new SimpleDateFormat("EEE").format(date);

            WeatherInfo weatherInfo = new WeatherInfo(
                    day,
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
