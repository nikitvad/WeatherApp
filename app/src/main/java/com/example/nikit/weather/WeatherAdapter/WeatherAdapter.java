package com.example.nikit.weather.weatherAdapter;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nikit.weather.Constants;
import com.example.nikit.weather.R;
import com.example.nikit.weather.entity.Weather;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by nikit on 11.01.2017.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
    private ArrayList<Weather> weathers;
    private OnItemClickListener clickListener;


    public WeatherAdapter(ArrayList<Weather> weathers) {
        this.weathers = weathers;
    }


    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        WeatherViewHolder viewHolder = new WeatherViewHolder(inflater.inflate(R.layout.weather_list_item, parent, false));
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        holder.bindWeather(weathers.get(position));

    }


    @Override
    public int getItemCount() {
        return weathers.size();
    }


    public void swapData(ArrayList<Weather> newWeathers){
        if(newWeathers!=null){
            this.weathers.clear();
            this.weathers.addAll(newWeathers);
            notifyDataSetChanged();

        }
    }


    public void setClickListener(OnItemClickListener listener){
        clickListener = listener;
    }



    class WeatherViewHolder extends RecyclerView.ViewHolder{

        private TextView tvWeatherDate;
        private TextView tvWeatherTemp;
        private TextView tvWeatherHum;
        private TextView tvWeatherPres;
        private TextView tvWeatherWindSpeed;
        private ImageView ivWeatherImage;

        private SharedPreferences defaultPreferences;
        private String tempMeasure;


        public WeatherViewHolder(View itemView) {
            super(itemView);

            tvWeatherDate = (TextView)itemView.findViewById(R.id.tv_weather_date);
            ivWeatherImage = (ImageView)itemView.findViewById(R.id.iv_weather_image);
            tvWeatherTemp = (TextView) itemView.findViewById(R.id.tv_weather_item_temp);
            tvWeatherHum = (TextView) itemView.findViewById(R.id.tv_weather_item_hum);
            tvWeatherPres = (TextView) itemView.findViewById(R.id.tv_weather_item_pres);
            tvWeatherWindSpeed = (TextView) itemView.findViewById(R.id.tv_weather_item_wind_speed);

            defaultPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());

            if(clickListener!=null){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Weather weather = weathers.get(getAdapterPosition());
                        clickListener.onItemClick(weather.getId());

                    }
                });
            }
        }


        public void bindWeather(Weather weather){

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM HH:mm");
            String simpleDate = simpleDateFormat.format(weather.getDate());
            tvWeatherDate.setText(simpleDate);

            String imageUrl = String.format(Constants.URL_WEATHER_IMAGE, weather.getWeatherIconId());

            try {
                Picasso.with(itemView.getContext()).load(imageUrl).into(ivWeatherImage);

            }catch (Exception e){
                e.printStackTrace();
            }

            tempMeasure = defaultPreferences.getString(Constants.TEMP_MEASURE, null);
            int temp = (int)weather.getMainTemp();
            if(tempMeasure.equals("˚C")){
                temp-=273;
            }else if(tempMeasure.equals("˚F")){
                temp-=273;
                temp*=1.8;
                temp+=32;
            }

            tvWeatherTemp.setText(temp+tempMeasure);
            tvWeatherPres.setText(weather.getMainPressure()+"");
            tvWeatherHum.setText(weather.getMainHumidity()+"%");
            tvWeatherWindSpeed.setText(weather.getWindSpeed()+"m/s");


        }
    }



    public interface OnItemClickListener{

        void onItemClick(long weatherId);

    }

}
