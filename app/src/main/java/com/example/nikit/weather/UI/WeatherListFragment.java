package com.example.nikit.weather.UI;


import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
    private Boolean loadFromInternet = false;


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


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(long weatherId);
    }

    private AlertDialog buildDialog(String title, String message, String buttonText, Drawable drawable){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setIcon(drawable)
                .setCancelable(false)
                .setNegativeButton(buttonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
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

            }else if(myAdapter.getItemCount()>0){

                Toast.makeText(context, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }else{

                buildDialog(getString(R.string.loading_error),
                        getString(R.string.loading_error_details),
                        "Ok",
                        context.getResources().getDrawable(R.drawable.no_network)).show();
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
}
