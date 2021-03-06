package com.example.nikit.weather.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.nikit.weather.Constants;
import com.example.nikit.weather.R;
import com.example.nikit.weather.ui.fragments.DetailFragment;
import com.example.nikit.weather.ui.fragments.WeatherListFragment;

public class MainActivity extends AppCompatActivity implements
        WeatherListFragment.OnFragmentInteractionListener, WeatherListFragment.OnLoadErrorListener {

    private Toolbar toolbar;
    private WeatherListFragment weatherListFragment;
    private boolean tablet;
    private String cityId;
    private SharedPreferences defaultPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        tablet = getResources().getBoolean(R.bool.isTablet);

        if (tablet) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        defaultPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        cityId = defaultPreferences.getString(Constants.CITY_ID, null);

        weatherListFragment = (WeatherListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_weather);
        weatherListFragment.setContext(getApplicationContext());

        if(cityId!=null && !cityId.equals("0")){
            weatherListFragment.updateData(true);
        }else{
            buildDialogUnspecifiedCity().show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_settings: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, 1);
                return true;
            }
            case R.id.menu_refresh: {
                cityId = defaultPreferences.getString(Constants.CITY_ID, null);
                if (!cityId.equals("0")) {
                    weatherListFragment.loadWeatherFromInternet();
                } else {
                    buildDialogUnspecifiedCity().show();
                }
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFragmentInteraction(long weatherId) {
        if(tablet){
            DetailFragment fragment =(DetailFragment) getFragmentManager().findFragmentById(R.id.fragment_weather_details);
            fragment.setContext(MainActivity.this);
            fragment.updateContent(weatherId);
        }else{
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(DetailActivity.WEATHER_ID_KEY, weatherId);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public void onLoadError() {
        buildDialog(getString(R.string.loading_error),
                getString(R.string.loading_error_details),
                "Ok",
                getResources().getDrawable(R.drawable.no_network)).show();
    }

    private AlertDialog buildDialogUnspecifiedCity(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.unspecified_city))
                .setMessage(getResources().getString(R.string.choice_city))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.settings),   new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(intent);
                    }
                });
        return builder.create();
    }

    public AlertDialog buildDialog(String title, String message, String buttonText, Drawable drawable){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Constants.RESULT_CODE_CITY_ID_CHANGED){
            weatherListFragment.updateData(true);
        }else if(resultCode == Constants.RESULT_CODE_TEMP_MEASURE_CHANGED){
            weatherListFragment.updateData(false);
        }
    }
}
