package com.test.weather.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.test.weather.R;
import com.test.weather.net.GPSTracker;
import com.test.weather.net.ResultListener;
import com.test.weather.net.VolleySingleton;
import com.test.weather.net.WeatherInfo;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, ResultListener {

    ImageView iconLocation;
    NetworkImageView backgroundImage;
    ImageLoader mImageLoader;

    MainInfoFragment mainInfoFragment;
    FullInfoFragment fullInfoFragment;

    String backgroundPhotoUrl;

    String city;
    int longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backgroundImage = (NetworkImageView) findViewById(R.id.backgroundImage);
        iconLocation = (ImageView) findViewById(R.id.iconLocation);
        mainInfoFragment = (MainInfoFragment) getFragmentManager().findFragmentById(R.id.mainInfoFragment);
        fullInfoFragment = (FullInfoFragment) getFragmentManager().findFragmentById(R.id.fullInfoFragment);


        iconLocation.setOnClickListener(this);

        mImageLoader = VolleySingleton.getInstance(this).getImageLoader();
        updatePosition();

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

    }

    @Override
    public void onError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
