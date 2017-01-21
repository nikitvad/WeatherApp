package com.example.nikit.weather.Database;

import android.provider.BaseColumns;

/**
 * Created by nikit on 13.01.2017.
 */

public final class DataBaseContract {
    public static final int DB_VERSION = 6;
    public static final String DB_NAME = "WeatherDB.db";
    public static final String TEXT_TYPE = " TEXT";
    public static final String COMMA_SEP = ",";
    public static final String REAL_TYPE = " REAL";
    public static final String INTEGER_TYPE = " INTEGER";
    public static final String NUMERIC_TYPE = " BIGINT";

    public DataBaseContract(){
    }

    public static abstract class WeatherTable implements BaseColumns{
        public static final String TABLE_NAME = "WEATHER";
        public static final String COLUMN_NAME_MAIN_TEMP = "main_temp";
        public static final String COLUMN_NAME_MIN_TEMP = "min_temp";
        public static final String COLUMN_NAME_MAX_TEMP = "max_temp";
        public static final String COLUMN_NAME_PRESSURE = "pressure";
        public static final String COLUMN_NAME_HUMIDITY ="humidity";
        public static final String COLUMN_NAME_WEATHER_MAIN = "weather_main";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_ICON_ID = "icon_id";
        public static final String COLUMN_NAME_CLOUDS_ALL = "clouds_all";
        public static final String COLUMN_NAME_WIND_SPEED = "wind_speed";
        public static final String COLUMN_NAME_WIND_DEG = "wind_deg";
        public static final String COLUMN_NAME_DATE = "date";

        public static final String[] ARRAY_OF_COLUMN_NAMES = {_ID, COLUMN_NAME_MAIN_TEMP, COLUMN_NAME_MAX_TEMP, COLUMN_NAME_MIN_TEMP,
                                                                COLUMN_NAME_PRESSURE, COLUMN_NAME_HUMIDITY, COLUMN_NAME_WEATHER_MAIN,
                                                                COLUMN_NAME_DESCRIPTION, COLUMN_NAME_ICON_ID, COLUMN_NAME_CLOUDS_ALL,
                                                                COLUMN_NAME_WIND_SPEED, COLUMN_NAME_WIND_DEG, COLUMN_NAME_DATE};


        public static final String DELETE_TABLE = "DROP TABLE " + TABLE_NAME;
        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " ("+
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME_MAIN_TEMP + REAL_TYPE + COMMA_SEP +
                COLUMN_NAME_MAX_TEMP + REAL_TYPE + COMMA_SEP +
                COLUMN_NAME_MIN_TEMP + REAL_TYPE + COMMA_SEP +
                COLUMN_NAME_PRESSURE + REAL_TYPE + COMMA_SEP +
                COLUMN_NAME_HUMIDITY + REAL_TYPE + COMMA_SEP +
                COLUMN_NAME_WEATHER_MAIN + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_ICON_ID + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_CLOUDS_ALL + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_WIND_SPEED + REAL_TYPE + COMMA_SEP +
                COLUMN_NAME_WIND_DEG + REAL_TYPE + COMMA_SEP +
                COLUMN_NAME_DATE + NUMERIC_TYPE +" )";
    }
}
