package com.example.nikit.weather.UI;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.nikit.weather.R;

/**
 * Created by nikit on 20.01.2017.
 */

public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.preferences);
    }
}
