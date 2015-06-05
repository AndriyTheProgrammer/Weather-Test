package com.test.weather.ui;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.test.weather.R;
import com.test.weather.net.pojo.WeatherInfo;


public class FullInfoFragment extends Fragment {


    TextView tvMinTemp, tvMaxTemp, tvPressure, tvHumidity, tvWindForce;
    ImageView imageWindDirection;
    View root;

    // Nothing special, just information about weather
    WeatherInfo weather;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_full_info, container, false);

        tvMinTemp = (TextView) root.findViewById(R.id.tvMinTemp);
        tvMaxTemp = (TextView) root.findViewById(R.id.tvMaxTemp);
        tvPressure = (TextView) root.findViewById(R.id.tvPressure);
        tvHumidity = (TextView) root.findViewById(R.id.tvHumidity);
        tvWindForce = (TextView) root.findViewById(R.id.tvWindForce);
        imageWindDirection = (ImageView) root.findViewById(R.id.imageWindDirection);


        return root;
    }

    /**
     * Used to fill this fragment with weather information from Activity
     * @param weather weather information
     */
    public void setWeather(WeatherInfo weather) {
        this.weather = weather;
        tvMinTemp.setText(weather.getMinTemp() + "\u00B0");
        tvMaxTemp.setText(weather.getMaxTemp() + "\u00B0");
        tvPressure.setText(weather.getPressure() + "hPa");
        tvHumidity.setText(weather.getHumidity() + "%");
        tvWindForce.setText(weather.getWindSpeed() + "mph");
        imageWindDirection.setRotation(weather.getWindDirection() - 90);

    }


    // shows Fragment =)
    public void show(){
        if (getActivity() != null){
            YoYo.with(Techniques.SlideInLeft)
                    .duration(300)
                    .withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            root.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .playOn(root);
        }
    }

    // and this hides Fragment =)
    public void hide(){
        if (getActivity() != null){
            YoYo.with(Techniques.SlideOutRight)
                    .duration(300)
                    .withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            root.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .playOn(root);

        }
    }

    // guess what
    public boolean isShowing(){
        return root.getVisibility() != View.VISIBLE;
    }


}
