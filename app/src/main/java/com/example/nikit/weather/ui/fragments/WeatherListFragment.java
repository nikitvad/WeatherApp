package com.example.nikit.weather.ui.fragments;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nikit.weather.database.WeatherDbHelper;
import com.example.nikit.weather.R;
import com.example.nikit.weather.entity.OpenWeatherFetch;
import com.example.nikit.weather.entity.Weather;
import com.example.nikit.weather.weatherAdapter.WeatherAdapter;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherListFragment extends Fragment {

    private RecyclerView rvWeatherList;
    private WeatherAdapter myAdapter;
    private ArrayList<Weather> weatherList;
    private Context context;

    private OnFragmentInteractionListener mListener;
    private OnLoadErrorListener errorListener;

    private boolean loadFromInternet = false;


    public WeatherListFragment() {

        weatherList = new ArrayList<>();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        rvWeatherList = (RecyclerView)view.findViewById(R.id.rv_weather_list);

        myAdapter = new WeatherAdapter(weatherList);
        myAdapter.setClickListener(new WeatherAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(long weatherId) {
                if(mListener!=null){
                    mListener.onFragmentInteraction(weatherId);
                }
            }
        });

        rvWeatherList.setAdapter(myAdapter);
        rvWeatherList.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        if(context instanceof OnLoadErrorListener){
            errorListener = (OnLoadErrorListener)context;
        }else{
            throw new RuntimeException(context.toString()
                    + " must implement OnLoadErrorListener");
        }

    }


    @Override
    public void onDetach(){
        super.onDetach();
        mListener=null;
    }


    public void updateData(boolean loadDataFromInternet){
            this.loadFromInternet = loadDataFromInternet;
            new LoadWeatherFromDbAsyncTask().execute();

    }


    public void loadWeatherFromInternet(){
        new FetchWeatherAsyncTask().execute();
    }


    private class FetchWeatherAsyncTask extends AsyncTask<Void, Void, Void> {
        private SQLiteDatabase db;
        private WeatherDbHelper dbHelper;
        private boolean successful = false;
        ArrayList<Weather> weatherArrayList;

        @Override
        protected Void doInBackground(Void... params) {
            OpenWeatherFetch weatherFetch = new OpenWeatherFetch(context);

            weatherArrayList = new ArrayList<>();

            try {
                weatherFetch.getWeatherList(weatherArrayList, true);
                if(weatherArrayList.size()>0){
                    dbHelper.clearWeatherTable(db);
                    dbHelper.insertWeather(db, weatherArrayList);
                    successful=true;
                }
            }catch (IOException e){
                successful = false;
            }catch (JSONException e){
                successful = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            db.close();
            if(successful && weatherArrayList.size()>0) {
                myAdapter.swapData(weatherArrayList);
            }else{
                errorListener.onLoadError();
            }
        }

        @Override
        protected void onPreExecute() {
            dbHelper = new WeatherDbHelper(context);
            db = dbHelper.getWritableDatabase();
        }

    }


    private class LoadWeatherFromDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private SQLiteDatabase db;
        private WeatherDbHelper dbHelper;
        ArrayList<Weather> weatherArrayList;

        @Override
        public void onPreExecute(){
            dbHelper = new WeatherDbHelper(context);
            db = dbHelper.getReadableDatabase();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            db.close();

            myAdapter.swapData(weatherArrayList);

            if(loadFromInternet) {
                new FetchWeatherAsyncTask().execute();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            dbHelper = new WeatherDbHelper(context);
            weatherArrayList = new ArrayList<>();
            dbHelper.loadWeatherList(db, weatherArrayList);
            return null;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(long weatherId);
    }

    public interface OnLoadErrorListener{
        void onLoadError();
    }
}
