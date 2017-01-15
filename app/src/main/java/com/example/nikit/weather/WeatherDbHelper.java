package com.example.nikit.weather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.nikit.weather.DataBaseContract.WeatherTable;

import java.util.Date;
import java.util.List;

/**
 * Created by nikit on 13.01.2017.
 */

public class WeatherDbHelper extends SQLiteOpenHelper {

    public WeatherDbHelper(Context context){
        super(context, DataBaseContract.DB_NAME, null, DataBaseContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WeatherTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(oldVersion != newVersion ){
            db.execSQL(WeatherTable.DELETE_TABLE);
            db.execSQL(WeatherTable.CREATE_TABLE);

            Log.d("database", "database upgraded");

        }

    }


    private ContentValues getContentValuesFormWeather(Weather weather){
        ContentValues values = new ContentValues();
        values.put(WeatherTable._ID, weather.getId());
        values.put(WeatherTable.COLUMN_NAME_MAIN_TEMP, weather.getMainTemp());
        values.put(WeatherTable.COLUMN_NAME_MIN_TEMP, weather.getMinTemp());
        values.put(WeatherTable.COLUMN_NAME_MAX_TEMP, weather.getMaxTemp());
        values.put(WeatherTable.COLUMN_NAME_PRESSURE, weather.getMainPressure());
        values.put(WeatherTable.COLUMN_NAME_HUMIDITY, weather.getMainHumidity());
        values.put(WeatherTable.COLUMN_NAME_WEATHER_MAIN, weather.getWeatherMain());
        values.put(WeatherTable.COLUMN_NAME_DESCRIPTION, weather.getWeatherDescription());
        values.put(WeatherTable.COLUMN_NAME_ICON_ID, weather.getWeatherIconId());
        values.put(WeatherTable.COLUMN_NAME_CLOUDS_ALL, weather.getCloudsAll());
        values.put(WeatherTable.COLUMN_NAME_WIND_SPEED, weather.getWindSpeed());
        values.put(WeatherTable.COLUMN_NAME_WIND_DEG, weather.getWindDeg());

        //need to check that it work?
        values.put(WeatherTable.COLUMN_NAME_DATE, weather.getDate().getTime());
        Log.d("date put", weather.getDate().getTime()+"");
        return values;

    }
    public void insertWeather(SQLiteDatabase db, Weather weather){

        ContentValues values = getContentValuesFormWeather(weather);
        db.insert(WeatherTable.TABLE_NAME, null, values);
    }

    public void insertWeather(SQLiteDatabase db, List<Weather> weatherList){
        ContentValues contentValues;
        for(int i=0; i<weatherList.size(); i++){
            contentValues=getContentValuesFormWeather(weatherList.get(i));
            db.insert(WeatherTable.TABLE_NAME, null, contentValues);
        }
    }


    public void loadWeatherList(SQLiteDatabase db, List<Weather> weatherList){
        Cursor cursor = db.query(DataBaseContract.WeatherTable.TABLE_NAME,
                DataBaseContract.WeatherTable.ARRAY_OF_COLUMN_NAMES,
                null, null, null, null, null);

        if(cursor.moveToFirst()){

            do {
                Weather weather = new Weather();
                weather.setId(cursor.getLong(cursor.getColumnIndex(WeatherTable._ID)));
                weather.setMainTemp(cursor.getDouble(cursor.getColumnIndex(WeatherTable.COLUMN_NAME_MAIN_TEMP)));
                weather.setMinTemp(cursor.getDouble(cursor.getColumnIndex(WeatherTable.COLUMN_NAME_MIN_TEMP)));
                weather.setMaxTemp(cursor.getDouble(cursor.getColumnIndex(WeatherTable.COLUMN_NAME_MAX_TEMP)));

                weather.setMainPressure(cursor.getDouble(cursor.getColumnIndex(WeatherTable.COLUMN_NAME_PRESSURE)));
                weather.setMainHumidity(cursor.getDouble(cursor.getColumnIndex(WeatherTable.COLUMN_NAME_HUMIDITY)));

                weather.setWeatherMain(cursor.getString(cursor.getColumnIndex(WeatherTable.COLUMN_NAME_WEATHER_MAIN)));
                weather.setWeatherDescription(cursor.getString(cursor.getColumnIndex(WeatherTable.COLUMN_NAME_DESCRIPTION)));

                weather.setWeatherIconId(cursor.getString(cursor.getColumnIndex(WeatherTable.COLUMN_NAME_ICON_ID)));

                weather.setCloudsAll(cursor.getInt(cursor.getColumnIndex(WeatherTable.COLUMN_NAME_CLOUDS_ALL)));
                weather.setWindSpeed(cursor.getDouble(cursor.getColumnIndex(WeatherTable.COLUMN_NAME_WIND_SPEED)));
                weather.setWindDeg(cursor.getDouble(cursor.getColumnIndex(WeatherTable.COLUMN_NAME_WIND_DEG)));

                Date date = new Date(cursor.getLong(cursor.getColumnIndex(WeatherTable.COLUMN_NAME_DATE)));
                weather.setDate(date);


                Log.d("date load", date.getTime()+"");

                weatherList.add(weather);

            }while(cursor.moveToNext());

        }

    }

    public void clearWeatherTable(SQLiteDatabase db){
        db.delete(WeatherTable.TABLE_NAME, null, null);
    }
}
