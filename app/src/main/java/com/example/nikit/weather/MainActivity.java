package com.example.nikit.weather;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private RecyclerView rvList;
    private ArrayList<Weather> weathers;
    private WeatherDbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        dbHelper = new WeatherDbHelper(this);

        weathers = new ArrayList<>();

        rvList = (RecyclerView) findViewById(R.id.rv_weather_list);

        WeatherAdapter adapter = new WeatherAdapter(weathers);
        adapter.setClickListener(new WeatherAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(long weatherId) {
                Toast.makeText(MainActivity.this, weatherId+" id", Toast.LENGTH_SHORT).show();
            }
        });

        rvList.setAdapter(adapter);
        rvList.setLayoutManager(new LinearLayoutManager(this));

        new FetchWeatherAsyncTask().execute();
    }


    private class FetchWeatherAsyncTask extends AsyncTask<Void, Void, Void>{


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            rvList.getAdapter().notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... params) {
            OpenWeatherFetch weatherFetch = new OpenWeatherFetch();

            JSONObject object = weatherFetch.getJsonFromUrl("http://api.openweathermap.org/data/2.5/forecast/city?id=703447&APPID=fb932f11d172ebff38ca77f59cd8e63b");
            Log.d("json", object.toString());

            ArrayList<Weather> weatherArrayList = new ArrayList<>();
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            weatherFetch.getWeatherListFromJson(object, weatherArrayList, true);

            dbHelper.clearWeatherTable(db);
            dbHelper.insertWeather(db, weatherArrayList);

            dbHelper.loadWeatherList(db, weathers);


           //weatherFetch.getWeatherListFromJson(object, weathers, true);

            for(Weather weather: weathers){
                Log.d("weather", weather.toString());
            }

            return null;
        }
    }

}
