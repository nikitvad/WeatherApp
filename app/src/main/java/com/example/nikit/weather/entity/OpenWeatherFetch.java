package com.example.nikit.weather.entity;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.nikit.weather.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by nikit on 10.01.2017.
 */

public class OpenWeatherFetch {

    static JSONObject jsonObject = null;
    static InputStream inputStream = null;

    static String jsonString = "";

    private Context context;


    public OpenWeatherFetch(Context context){
    this.context = context;
    }


    private JSONObject getWeatherJson() throws IOException, JSONException {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String cityId = preferences.getString(Constants.CITY_ID, null);
        String urlString = String.format(Constants.URL_WEATHER_API, cityId);

        try {

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            inputStream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            StringBuffer stringBuffer = new StringBuffer();
            while((line = reader.readLine())!=null){
                stringBuffer.append(line + "\n");
                Log.d("line", line);
            }
            inputStream.close();
            jsonString = stringBuffer.toString();

            jsonObject = new JSONObject(jsonString);

        }catch (MalformedURLException e) {
            Log.d("OpenWeatherFetch", e.toString());
            e.printStackTrace();
        }

        Log.d("jsonWeather", jsonObject.toString());
        return jsonObject;
    }


    private Weather getWeather(JSONObject jsonWeatherItem){
        Weather weather = new Weather();
        try {

            weather.setId(jsonWeatherItem.getLong("dt"));
            JSONObject jsonWeatherMain = jsonWeatherItem.getJSONObject("main");
            weather.setMainTemp(jsonWeatherMain.getDouble("temp"));
            weather.setMinTemp(jsonWeatherMain.getDouble("temp_max"));
            weather.setMaxTemp(jsonWeatherMain.getDouble("temp_max"));
            weather.setMainPressure(jsonWeatherMain.getDouble("pressure"));
            weather.setMainHumidity(jsonWeatherMain.getInt("humidity"));

            String dtTxt = jsonWeatherItem.getString("dt_txt");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            weather.setDate(simpleDateFormat.parse(dtTxt));

            JSONObject jsonWeather = jsonWeatherItem.getJSONArray("weather").getJSONObject(0);

            weather.setWeatherMain(jsonWeather.getString("main"));
            weather.setWeatherDescription(jsonWeather.getString("description"));
            weather.setWeatherIconId(jsonWeather.getString("icon"));

            JSONObject jsonWeatherClouds = jsonWeatherItem.getJSONObject("clouds");
            weather.setCloudsAll(jsonWeatherClouds.getInt("all"));

            JSONObject jsonWeatherWind = jsonWeatherItem.getJSONObject("wind");
            weather.setWindSpeed(jsonWeatherWind.getDouble("speed"));
            weather.setWindDeg(jsonWeatherWind.getDouble("deg"));

        }catch (JSONException e){
            Log.d("json", e.toString());
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        return weather;
    }


    public void getWeatherList(List<Weather> targetList, boolean clearTargetList)throws IOException, JSONException{

        if(clearTargetList){
            targetList.clear();
        }

        JSONObject jsonBody = getWeatherJson();

        JSONArray jsonArray = jsonBody.getJSONArray("list");
        Log.d("list", jsonArray.length()+"");

        for (int i = 0; i < jsonArray.length(); i++) {

            targetList.add(getWeather(jsonArray.getJSONObject(i)));
            Log.d("jsonArrayItem", jsonArray.getJSONObject(i).toString());
        }

    }
}
