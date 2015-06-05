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


public class MainInfoFragment extends Fragment implements View.OnClickListener {

    final static int NONE_SELECTED = -1;
    ArrayList<WeatherInfo> weeklyForecast;
    TextView tvCurrentTemp;
    ImageView imageCurrentWeather;
    View root;
    int selectedItem = NONE_SELECTED;


    ViewGroup[] forecastDaysContainers;

    LayoutInflater inflater;

    FragmentInteractionListener fragmentInteractionListener;

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
        root = inflater.inflate(R.layout.fragment_main_information, container, false);
        tvCurrentTemp = (TextView) root.findViewById(R.id.tvCurrentTemp);
        imageCurrentWeather = (ImageView) root.findViewById(R.id.imageCurrentWeather);
        forecastDaysContainers = new ViewGroup[]{
                (ViewGroup) root.findViewById(R.id.dayContainer1),
                (ViewGroup) root.findViewById(R.id.dayContainer2),
                (ViewGroup) root.findViewById(R.id.dayContainer3),
                (ViewGroup) root.findViewById(R.id.dayContainer4),
                (ViewGroup) root.findViewById(R.id.dayContainer5)
        };
        for (int i = 0; i < forecastDaysContainers.length; i++){
            forecastDaysContainers[i].setOnClickListener(this);
        }

        fragmentInteractionListener = (MainActivity) getActivity();
        return root;
    }

    public void setWeeklyForecast(ArrayList<WeatherInfo> weeklyForecast) {
        if (getActivity() != null) {
            this.weeklyForecast = weeklyForecast;


            tvCurrentTemp.setText((weeklyForecast.get(0).getMaxTemp() + weeklyForecast.get(0).getMinTemp()) / 2 + "\u00B0");

            for (int i = 1; i < forecastDaysContainers.length; i++) {
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



    @Override
    public void onClick(View v) {
        root.findViewById(v.getId()).setBackgroundResource(R.color.selected_weather);
        if (selectedItem != NONE_SELECTED) root.findViewById(selectedItem).setBackgroundResource(R.color.opacity);

        if (selectedItem == v.getId()) {
            fragmentInteractionListener.onWeatherButtonClicked(NONE_SELECTED);


        }else{
            for (int i = 0; i < forecastDaysContainers.length; i++){
                if (forecastDaysContainers[i].getId() == v.getId()){
                    fragmentInteractionListener.onWeatherButtonClicked(i);
                    selectedItem = v.getId();
                }
            }
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentInteractionListener = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        fragmentInteractionListener = null;
    }
}
