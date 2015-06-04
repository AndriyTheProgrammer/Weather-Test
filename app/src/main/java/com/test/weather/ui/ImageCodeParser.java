package com.test.weather.ui;

import com.test.weather.R;

/**
 * Created by User on 04.06.2015.
 */
public class ImageCodeParser {


    /**
     * @param code Image code from http://openweathermap.org/
     * @return Mipmap id;
     */
    public static int parse(String code){
        return R.mipmap.sun;
    }

}
