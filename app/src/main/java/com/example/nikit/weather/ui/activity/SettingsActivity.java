package com.example.nikit.weather.ui.activity;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.nikit.weather.Constants;
import com.example.nikit.weather.R;

import java.util.List;

/**
 * Created by nikit on 20.01.2017.
 */

public class SettingsActivity extends PreferenceActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener{

    private boolean tablet;
    private int mResultCode = 0;

    private ListPreference tempMeasureListPref;
    private ListPreference cityIdListPref;



    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.preferences);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
        cityIdListPref = (ListPreference)findPreference(Constants.CITY_ID);
        tempMeasureListPref = (ListPreference)findPreference(Constants.TEMP_MEASURE);

        cityIdListPref.setSummary(cityIdListPref.getEntry());
        tempMeasureListPref.setSummary(tempMeasureListPref.getEntry());

        tablet = getResources().getBoolean(R.bool.isTablet);
        if (tablet) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

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

        setResult(mResultCode);
        finish();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(Constants.CITY_ID)) {
            mResultCode = Constants.RESULT_CODE_CITY_ID_CHANGED;
            cityIdListPref.setSummary(cityIdListPref.getEntry());

        }else if( key.equals("temp_measure") ){
                tempMeasureListPref.setSummary(tempMeasureListPref.getEntry());
                if(mResultCode!=Constants.RESULT_CODE_CITY_ID_CHANGED){
                    mResultCode=Constants.RESULT_CODE_TEMP_MEASURE_CHANGED;
            }
        }
    }
}
