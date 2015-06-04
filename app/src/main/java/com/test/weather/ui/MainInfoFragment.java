package com.test.weather.ui;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.weather.R;
import com.test.weather.net.WeatherInfo;

import java.util.ArrayList;


public class MainInfoFragment extends Fragment {

    ArrayList<WeatherInfo> weeklyForecast;
    TextView tvCurrentTemp;
    ImageView imageCurrentWeather;

    ViewGroup[] forecastDaysContainers;

    LayoutInflater inflater;


    public MainInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        View root = inflater.inflate(R.layout.fragment_main_information, container, false);
        tvCurrentTemp = (TextView) root.findViewById(R.id.tvCurrentTemp);
        imageCurrentWeather = (ImageView) root.findViewById(R.id.imageCurrentWeather);
        forecastDaysContainers = new ViewGroup[]{
                (ViewGroup) root.findViewById(R.id.dayContainer1),
                (ViewGroup) root.findViewById(R.id.dayContainer2),
                (ViewGroup) root.findViewById(R.id.dayContainer3),
                (ViewGroup) root.findViewById(R.id.dayContainer4),
                (ViewGroup) root.findViewById(R.id.dayContainer5)
        };


        return root;
    }

    public void setWeeklyForecast(ArrayList<WeatherInfo> weeklyForecast) {
        this.weeklyForecast = weeklyForecast;


        tvCurrentTemp.setText((weeklyForecast.get(0).getMaxTemp() + weeklyForecast.get(0).getMinTemp())/2 + "\u00B0");

        for (int i = 1; i < 5; i++){
            WeatherInfo weather = weeklyForecast.get(i);
            View forecastView = inflater.inflate(R.layout.weather_item, forecastDaysContainers[i], true);
            TextView tvForecastDay = (TextView) forecastView.findViewById(R.id.tvForecastDay);
            ImageView imageForecast = (ImageView) forecastView.findViewById(R.id.imageForecast);
            TextView tvForecastMaxTemp = (TextView) forecastView.findViewById(R.id.tvForecastMaxTemp);
            TextView tvForecastMinTemp = (TextView) forecastView.findViewById(R.id.tvForecastMinTemp);
            tvForecastDay.setText(weather.getDay());
            imageForecast.setImageDrawable(getResources().getDrawable(ImageCodeParser.parse(weather.getIconCode())));
            tvForecastMaxTemp.setText(weather.getMaxTemp() + "\u00B0");
            tvForecastMinTemp.setText(weather.getMinTemp() + "\u00B0");
        }


    }
}
