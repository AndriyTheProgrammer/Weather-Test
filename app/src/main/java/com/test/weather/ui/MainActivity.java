package com.test.weather.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.test.weather.R;
import com.test.weather.net.GPSTracker;
import com.test.weather.net.Network;
import com.test.weather.net.NetworkInterface;
import com.test.weather.net.ResultListener;
import com.test.weather.net.VolleySingleton;
import com.test.weather.net.WeatherInfo;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, ResultListener {

    ImageView iconLocation;
    NetworkImageView backgroundImage;
    ImageLoader mImageLoader;
    TextView tvCity;

    MainInfoFragment mainInfoFragment;
    FullInfoFragment fullInfoFragment;

    String backgroundPhotoUrl;

    String city;
    int longitude, latitude;

    ArrayList<WeatherInfo> forecast;
    NetworkInterface networkInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backgroundImage = (NetworkImageView) findViewById(R.id.backgroundImage);
        iconLocation = (ImageView) findViewById(R.id.iconLocation);
        tvCity = (TextView) findViewById(R.id.tvCity);
        mainInfoFragment = (MainInfoFragment) getFragmentManager().findFragmentById(R.id.mainInfoFragment);
        fullInfoFragment = (FullInfoFragment) getFragmentManager().findFragmentById(R.id.fullInfoFragment);

        iconLocation.setOnClickListener(this);

        mImageLoader = VolleySingleton.getInstance(this).getImageLoader();
        networkInterface = new Network();
        updatePosition();

        networkInterface.requestUpdate("L'viv", this);


    }




    @Override
    public void onClick(View v) {


    }


    private void updatePosition(){
        GPSTracker gpsTracker = new GPSTracker(this);

        if (gpsTracker.getIsGPSTrackingEnabled())
        {
            longitude = (int) gpsTracker.getLongitude();
            latitude = (int) gpsTracker.getLatitude();

            city = gpsTracker.getLocality(this);
            tvCity.setText(city);
            Log.d("MainActivity", "Position Updated. lon: " + longitude + "; lat: " + latitude + "; city: " + city);
        }
        else
        {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }


    }




    @Override
    public void onUpdateCompleted(String photoUrl, ArrayList<WeatherInfo> weatherInfos) {
        backgroundPhotoUrl = photoUrl;
        backgroundImage.setImageUrl(photoUrl, mImageLoader);
        mainInfoFragment.setWeeklyForecast(weatherInfos);
        fullInfoFragment.setWeather(weatherInfos.get(0));

    }

    @Override
    public void onError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }



}
