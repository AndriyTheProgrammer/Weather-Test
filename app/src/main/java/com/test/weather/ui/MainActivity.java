package com.test.weather.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
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
import com.test.weather.net.pojo.WeatherInfo;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, FragmentInteractionListener, ResultListener {

    ImageView iconLocation;
    NetworkImageView backgroundImage;
    ImageLoader mImageLoader;
    TextView tvCity;

    MainInfoFragment mainInfoFragment;
    FullInfoFragment fullInfoFragment;

    String backgroundPhotoUrl;

    String city;


    ArrayList<WeatherInfo> forecast;
    NetworkInterface networkInterface;

    int dayToShow;
    boolean showFullInfo = false;

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
        requestUpdate(city);




    }




    @Override
    public void onClick(View v) {

        showEnterLocationDialog();
    }

    @Override
    public void onWeatherButtonClicked(int clickedDay) {
      if (clickedDay != MainInfoFragment.NONE_SELECTED){
          fullInfoFragment.setWeather(forecast.get(clickedDay));
          fullInfoFragment.show();
      }else{
          fullInfoFragment.hide();
      }

    }



    // Used to receive city from GPS/Network
    private void updatePosition(){
        GPSTracker gpsTracker = new GPSTracker(this);

        if (gpsTracker.getIsGPSTrackingEnabled())
        {
            city = gpsTracker.getLocality(this);
            tvCity.setText(city);
        }
        else
        {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }


    }

    // Requesting update for current city, second param is callback where we will get all information
    protected void requestUpdate(String city){
        networkInterface.requestUpdate(city, this);
    }




    //Dialog which allows user to enter some another city
    void showEnterLocationDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Location");
        alert.setMessage("Please enter your location");

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                city = input.getText().toString();
                tvCity.setText(city);
                requestUpdate(city);

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }



    //We received weather forecast
    @Override
    public void onWeatherReceived(ArrayList<WeatherInfo> weatherInfos) {
        forecast = weatherInfos;
        mainInfoFragment.setWeeklyForecast(weatherInfos);

    }

    //We received photo url for current city
    @Override
    public void onPhotoReceived(String photoUrl) {
        backgroundPhotoUrl = photoUrl;
        backgroundImage.setImageUrl(photoUrl, mImageLoader);
    }

    // Something went wrong, for example page not found, etc.
    @Override
    public void onError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

}
