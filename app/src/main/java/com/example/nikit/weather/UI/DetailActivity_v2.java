package com.example.nikit.weather.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.nikit.weather.R;

/**
 * Created by nikit on 23.01.2017.
 */

public class DetailActivity_v2 extends AppCompatActivity {
    public static final String WEATHER_ID_KEY = "weather_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        long weatherId = getIntent().getExtras().getLong(WEATHER_ID_KEY);
        DetailFragment fragment = (DetailFragment) getFragmentManager().findFragmentById(R.id.fragment_details);
        fragment.setContext(DetailActivity_v2.this);
        fragment.updateContent(weatherId);

    }


}
