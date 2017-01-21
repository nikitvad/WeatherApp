package com.example.nikit.weather.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.nikit.weather.Database.WeatherDbHelper;
import com.example.nikit.weather.Weather.OpenWeatherFetch;
import com.example.nikit.weather.R;
import com.example.nikit.weather.Weather.Weather;
import com.example.nikit.weather.Weather.WeatherAdapter;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private RecyclerView rvList;
    private ArrayList<Weather> weathers;
    private WeatherDbHelper dbHelper;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new WeatherDbHelper(this);
        weathers = new ArrayList<>();
        rvList = (RecyclerView) findViewById(R.id.rv_weather_list);

        WeatherAdapter adapter = new WeatherAdapter(weathers);
        adapter.setClickListener(new WeatherAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(long weatherId) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.WEATHER_ID_KEY, weatherId);
                startActivity(intent);
            }
        });

        rvList.setAdapter(adapter);
        rvList.setLayoutManager(new LinearLayoutManager(this));

       new LoadWeatherFromDbAsyncTask().execute();

        //new FetchWeatherAsyncTask().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_refresh:
                new FetchWeatherAsyncTask().execute();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private class FetchWeatherAsyncTask extends AsyncTask<Void, Void, Void>{
        SQLiteDatabase db;
        private boolean isSuccessful = false;

        @Override
        protected Void doInBackground(Void... params) {
            OpenWeatherFetch weatherFetch = new OpenWeatherFetch();

            ArrayList<Weather> weatherArrayList = new ArrayList<>();


            try {
                weatherFetch.getWeatherList(MainActivity.this, weatherArrayList, true);
            }catch (IOException e){
                Log.d("getWeatherList", "problem with internet connection");
                isSuccessful = false;
            }

            if(weatherArrayList.size()>0) {

                dbHelper.clearWeatherTable(db);
                dbHelper.insertWeather(db, weatherArrayList);
                weathers.clear();
                weathers.addAll(weatherArrayList);
                isSuccessful = true;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(isSuccessful) {
                super.onPostExecute(aVoid);
                rvList.getAdapter().notifyDataSetChanged();

            }else{
                Toast.makeText(MainActivity.this, "not internet connection", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }

        @Override
        protected void onPreExecute() {
            db = dbHelper.getWritableDatabase();
        }


    }


    private class LoadWeatherFromDbAsyncTask extends AsyncTask<Void, Void, Void>{
        SQLiteDatabase db;

        @Override
        public void onPreExecute(){
            db = dbHelper.getReadableDatabase();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            rvList.getAdapter().notifyDataSetChanged();
            db.close();
            new FetchWeatherAsyncTask().execute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            dbHelper.loadWeatherList(db, weathers);
            return null;
        }
    }

}
