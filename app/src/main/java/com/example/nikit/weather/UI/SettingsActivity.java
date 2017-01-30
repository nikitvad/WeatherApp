package com.example.nikit.weather.UI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.example.nikit.weather.R;

/**
 * Created by nikit on 20.01.2017.
 */

public class SettingsActivity extends PreferenceActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener{

    private int mResultCode;


    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.preferences);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
    }

    @Override
    public void onResume(){
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        setResult(mResultCode);
        finish();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("city_id")) {
            mResultCode = MainActivity.CITY_ID_CHANGED;
        }else if(mResultCode!= MainActivity.CITY_ID_CHANGED && key.equals("temp_measure")){
            mResultCode = MainActivity.TEMP_MEASURE_CHANGED;
        }
    }
}
