package com.example.nikit.weather.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.nikit.weather.R;

public class Main2Activity extends AppCompatActivity {

    private Toolbar toolbar;
    private WeatherListFragment weatherListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        weatherListFragment = (WeatherListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_weather);
        weatherListFragment.setContext(this);
        weatherListFragment.updateContent();
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
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_refresh:
                weatherListFragment.updateContent();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
