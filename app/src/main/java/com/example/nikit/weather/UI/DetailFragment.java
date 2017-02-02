package com.example.nikit.weather.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nikit.weather.Database.WeatherDbHelper;
import com.example.nikit.weather.R;
import com.example.nikit.weather.Weather.Weather;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;


/**
 * Created by nikit on 23.01.2017.
 */

public class DetailFragment extends Fragment {

    private TextView tvWeatherDate;
    private ImageView ivWeatherIcon;
    private TextView tvCurrTemp;
    private TextView tvMaxTemp;
    private TextView tvMinTemp;
    private TextView tvCloud;
    private TextView tvWindSpeed;
    private TextView tvPressure;
    private TextView tvHumidity;
    private Context context;
    private Weather weather;
    private SharedPreferences defaultPreferences;
    private String tempMeasure;

    public static final String WEATHER_IMAGE_URL = "http://openweathermap.org/img/w/%1$s.png";
    public static final String TEMP_MEASURE = "temp_measure";


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

        defaultPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());

        ivWeatherIcon = (ImageView) view.findViewById(R.id.iv_fragment_weather_image);
        tvWeatherDate = (TextView) view.findViewById(R.id.tv_fragment_weather_date);
        tvWeatherDate = (TextView) view.findViewById(R.id.tv_fragment_weather_date);
        tvCurrTemp = (TextView) view.findViewById(R.id.tv_detail_curr_temp);
        tvMaxTemp = (TextView) view.findViewById(R.id.tv_detail_max_temp);
        tvMinTemp = (TextView) view.findViewById(R.id.tv_detail_min_temp);
        tvCloud = (TextView) view.findViewById(R.id.tv_detail_cloud);
        tvHumidity = (TextView) view.findViewById(R.id.tv_detail_humidity);
        tvPressure = (TextView) view.findViewById(R.id.tv_detail_pressure);
        tvWindSpeed = (TextView) view.findViewById(R.id.tv_detail_wind_speed);
    }


    public void updateContent(long weatherId){

        new LoadWeatherAsyncTask().execute(weatherId);
    }


    private int tempConvert(int temp, String measure){

        if(tempMeasure.equals("˚C")){
            temp-=273;
        }else if(tempMeasure.equals("˚F")){
            temp-=273;
            temp*=1.8;
            temp+=32;
        }

        return temp;
    }


    private class LoadWeatherAsyncTask extends AsyncTask<Long, Void, Void> {
        private SQLiteDatabase db;
        private WeatherDbHelper dbHelper;


        @Override
        protected void onPostExecute(Void aVoid) {

            String imageUrl = String.format(WEATHER_IMAGE_URL, weather.getWeatherIconId());
            Picasso.with(context).load(imageUrl).into(ivWeatherIcon);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM HH:mm");
            String simpleDate = simpleDateFormat.format(weather.getDate());
            tvWeatherDate.setText(simpleDate);

            tempMeasure = defaultPreferences.getString(TEMP_MEASURE, null);

            tvCurrTemp.setText(tempConvert((int)weather.getMainTemp(), tempMeasure)+tempMeasure);
            tvMaxTemp.setText(tempConvert((int)weather.getMaxTemp(), tempMeasure)+tempMeasure);
            tvMinTemp.setText(tempConvert((int)weather.getMinTemp(), tempMeasure)+tempMeasure);
            tvCloud.setText(weather.getCloudsAll()+"%");
            tvWindSpeed.setText(weather.getWindSpeed()+"m/s");
            tvPressure.setText(weather.getMainPressure()+"");
            tvHumidity.setText(weather.getMainHumidity()+"%");
            getView().setVisibility(View.VISIBLE);

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
String s = "refracting";
            return null;
        }
    }

}
