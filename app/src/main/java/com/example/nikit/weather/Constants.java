package com.example.nikit.weather;

/**
 * Created by nikit on 02.02.2017.
 */

public class Constants {
    public static final String TEMP_MEASURE = "temp_measure";
    public static final String CITY_ID = "city_id";
    public static final String WEATHER_API_KEY="fb932f11d172ebff38ca77f59cd8e63b";
    public static final String URL_WEATHER_API = "http://api.openweathermap.org/data/2.5/forecast/city?id=%1$s&APPID="+WEATHER_API_KEY;
    public static final String URL_WEATHER_IMAGE = "http://openweathermap.org/img/w/%1$s.png";
    public static final int RESULT_CODE_CITY_ID_CHANGED = 7777;
    public static final int RESULT_CODE_TEMP_MEASURE_CHANGED = 7778;
    public static final String EXTRAS_WEATHER_ID_KEY = "weather_id";
}
