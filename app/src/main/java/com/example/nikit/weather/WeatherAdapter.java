package com.example.nikit.weather;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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


    public void setClickListener(OnItemClickListener listener){
        clickListener = listener;
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder{
        private TextView tvWeatherDate;
        private ImageView ivWeatherImage;


        public static final String WEATHER_IMAGE_URL = "http://openweathermap.org/img/w/%1$s.png";
        public WeatherViewHolder(View itemView) {
            super(itemView);

            tvWeatherDate = (TextView)itemView.findViewById(R.id.tv_weather_date);
            ivWeatherImage = (ImageView)itemView.findViewById(R.id.iv_weather_image);

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
            tvWeatherDate.setText(weather.getDate().toString());
            String imageUrl = String.format(WEATHER_IMAGE_URL, weather.getWeatherIconId());

            Log.d("imageurl", imageUrl);
            Log.d("imageurl", weather.getWeatherIconId());

            Picasso.with(itemView.getContext()).load(imageUrl).into(ivWeatherImage);

        }
    }


    public interface OnItemClickListener{

        void onItemClick(long weatherId);

    }

}
