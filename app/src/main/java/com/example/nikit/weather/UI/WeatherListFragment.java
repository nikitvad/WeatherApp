package com.example.nikit.weather.UI;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nikit.weather.Database.WeatherDbHelper;
import com.example.nikit.weather.R;
import com.example.nikit.weather.Weather.OpenWeatherFetch;
import com.example.nikit.weather.Weather.Weather;
import com.example.nikit.weather.Weather.WeatherAdapter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherListFragment extends Fragment {

    private RecyclerView rvWeatherList;
    private ArrayList<Weather> weatherList;
    private Context context;
    private OnFragmentInteractionListener mListener;


    public WeatherListFragment() {
        // Required empty public constructor
        weatherList = new ArrayList<>();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        rvWeatherList = (RecyclerView)view.findViewById(R.id.rv_weather_list);

        WeatherAdapter adapter = new WeatherAdapter(weatherList);
        adapter.setClickListener(new WeatherAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(long weatherId) {
                if(mListener!=null){
                    mListener.onFragmentInteraction(weatherId);
                }
            }
        });
        rvWeatherList.setAdapter(adapter);
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

    }

    @Override
    public void onDetach(){
        super.onDetach();
        mListener=null;
    }
    public void updateContent(){
        new LoadWeatherFromDbAsyncTask().execute();
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(long weatherId);
    }


    private class FetchWeatherAsyncTask extends AsyncTask<Void, Void, Void> {
        private SQLiteDatabase db;
        private WeatherDbHelper dbHelper;
        private boolean isSuccessful = false;

        @Override
        protected Void doInBackground(Void... params) {
            OpenWeatherFetch weatherFetch = new OpenWeatherFetch();

            ArrayList<Weather> weatherArrayList = new ArrayList<>();


            try {
                weatherFetch.getWeatherList(context, weatherArrayList, true);
            }catch (IOException e){
                isSuccessful = false;
            }

            if(weatherArrayList.size()>0) {

                dbHelper.clearWeatherTable(db);
                dbHelper.insertWeather(db, weatherArrayList);
                weatherList.clear();
                weatherList.addAll(weatherArrayList);
                isSuccessful = true;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(isSuccessful) {
                super.onPostExecute(aVoid);
                rvWeatherList.getAdapter().notifyDataSetChanged();

            }else{
                Toast.makeText(context, "not internet connection", Toast.LENGTH_SHORT).show();
            }
            db.close();
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

        @Override
        public void onPreExecute(){
            dbHelper = new WeatherDbHelper(context);
            db = dbHelper.getReadableDatabase();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            rvWeatherList.getAdapter().notifyDataSetChanged();
            db.close();
            new FetchWeatherAsyncTask().execute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            dbHelper = new WeatherDbHelper(context);
            dbHelper.loadWeatherList(db, weatherList);
            return null;
        }
    }

}
