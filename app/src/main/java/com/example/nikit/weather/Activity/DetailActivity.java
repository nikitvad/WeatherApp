package com.example.nikit.weather.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nikit.weather.Database.WeatherDbHelper;
import com.example.nikit.weather.R;
import com.example.nikit.weather.Weather.Weather;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    public static final String WEATHER_ID_KEY = "weather_id";
    SQLiteDatabase db;
    WeatherDbHelper dbHelper;
    Weather weather;
    TextView tvWeatherDate;
    ImageView ivWeatherIcon;

    public static final String WEATHER_IMAGE_URL = "http://openweathermap.org/img/w/%1$s.png";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dbHelper = new WeatherDbHelper(this);
        Intent intent = getIntent();
        long weatherId = intent.getExtras().getLong(WEATHER_ID_KEY);
        new LoadWeatherAsyncTask().execute(weatherId);


    }


    private class LoadWeatherAsyncTask extends AsyncTask<Long, Void, Void>{

        @Override
        protected void onPostExecute(Void aVoid) {
            tvWeatherDate = (TextView) findViewById(R.id.tv_weather_date);
            ivWeatherIcon = (ImageView) findViewById(R.id.iv_weather_image);

            String imageUrl = String.format(WEATHER_IMAGE_URL, weather.getWeatherIconId());
            Picasso.with(DetailActivity.this).load(imageUrl).into(ivWeatherIcon);
            tvWeatherDate.setText(weather.getDate().toString());


        }

        @Override
        protected void onPreExecute() {
            db = dbHelper.getReadableDatabase();
        }

        @Override
        protected Void doInBackground(Long... params) {

            weather = dbHelper.getWeatherById(db, params[0]);

            return null;
        }
    }
}
