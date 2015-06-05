package com.test.weather.net.pojo;


import java.util.ArrayList;


/**
 * Simple POJO class, filling with Gson library
 */

public class WeatherPOJO {

    private int cod;
    private ArrayList<List> list;

    public class List {

        private long dt;
        private float pressure, humidity;
        private float speed, deg;

        private Temp temp;
        private Weather[] weather;

        public long getDt() {
            return dt;
        }

        public float getPressure() {
            return pressure;
        }

        public float getHumidity() {
            return humidity;
        }

        public float getSpeed() {
            return speed;
        }

        public float getDeg() {
            return deg;
        }

        public Temp getTemp() {
            return temp;
        }

        public Weather[] getWeather() {
            return weather;
        }

        public class Temp {
            private float min, max;

            public float getMin() {
                return min;
            }

            public float getMax() {
                return max;
            }
        }
        public class Weather{
            private int id;
            private String main, description, icon;

            public int getId() {
                return id;
            }

            public String getMain() {
                return main;
            }

            public String getDescription() {
                return description;
            }

            public String getIcon() {
                return icon;
            }
        }
    }


    public int getCod() {
        return cod;
    }

    public ArrayList<List> getList() {
        return list;
    }
}
