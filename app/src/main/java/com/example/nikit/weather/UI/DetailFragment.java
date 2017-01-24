package com.example.nikit.weather.UI;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikit.weather.Database.WeatherDbHelper;
import com.example.nikit.weather.R;
import com.example.nikit.weather.Weather.Weather;
import com.squareup.picasso.Picasso;

/**
 * Created by nikit on 23.01.2017.
 */

public class DetailFragment extends Fragment {

    private TextView tvWeatherDate;
    private ImageView ivWeatherIcon;
    SQLiteDatabase db;
    WeatherDbHelper dbHelper;
    private Context context;

    private Weather weather;
    public static final String WEATHER_IMAGE_URL = "http://openweathermap.org/img/w/%1$s.png";

    public DetailFragment(){}

    public void setContext(Context context){
        this.context=context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.details_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivWeatherIcon = (ImageView) view.findViewById(R.id.iv_fragment_weather_image);
        tvWeatherDate = (TextView) view.findViewById(R.id.tv_fragment_weather_date);

    }


    public void updateContent(long weatherId){

        new LoadWeatherAsyncTask().execute(weatherId);
    }

    private class LoadWeatherAsyncTask extends AsyncTask<Long, Void, Void> {
        private SQLiteDatabase db;
        private WeatherDbHelper dbHelper;


        @Override
        protected void onPostExecute(Void aVoid) {

            String imageUrl = String.format(WEATHER_IMAGE_URL, weather.getWeatherIconId());
            Picasso.with(context).load(imageUrl).into(ivWeatherIcon);
            tvWeatherDate.setText(weather.getWeatherDescription());
            Log.d("weatherwww", weather.toString());
            db.close();


        }

        @Override
        protected void onPreExecute() {

            dbHelper = new WeatherDbHelper(context);
            db = dbHelper.getReadableDatabase();


        }

        @Override
        protected Void doInBackground(Long... params) {

           weather = dbHelper.getWeatherById(db, params[0]);

            return null;
        }
    }

}
