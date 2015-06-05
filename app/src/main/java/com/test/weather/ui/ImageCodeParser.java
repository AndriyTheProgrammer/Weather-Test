package com.test.weather.ui;

import com.test.weather.R;

/**
 * Created by User on 04.06.2015.
 */
public class ImageCodeParser {


    /**
     * Used to just to convert code from Weather API into resource id
     * @param code Image code from http://openweathermap.org/
     * @return Mipmap id;
     */
    public static int parse(String code){

        switch (code){
            case "01d":
                return R.mipmap.d01;
            case "02d":
                return R.mipmap.d02;
            case "03d":
                return R.mipmap.d03;
            case "04d":
                return R.mipmap.d04;
            case "09d":
                return R.mipmap.d09;
            case "10d":
                return R.mipmap.d10;
            case "11d":
                return R.mipmap.d11;
            case "13d":
                return R.mipmap.d13;
            case "50d":
                return R.mipmap.d50;
            case "01n":
                return R.mipmap.n01;
            case "02n":
                return R.mipmap.n02;
            case "03n":
                return R.mipmap.n03;
            case "04n":
                return R.mipmap.n04;
            case "09n":
                return R.mipmap.n09;
            case "10n":
                return R.mipmap.n10;
            case "11n":
                return R.mipmap.n11;
            case "13n":
                return R.mipmap.n13;
            case "50n":
                return R.mipmap.n50;



        }

        return R.mipmap.sun;
    }

}
