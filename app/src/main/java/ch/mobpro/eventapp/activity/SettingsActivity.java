package ch.mobpro.eventapp.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import ch.mobpro.eventapp.R;
import ch.mobpro.eventapp.fragment.SettingsFragment;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private final String deviceLocation = "deviceLocation";
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar mActionBarToolbar = findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle(R.string.title_activity_settings);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        listener = this::toggleMapFragment;
        preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    private void toggleMapFragment(SharedPreferences preferences, String key) {
        if (deviceLocation.equals(key)) {
            boolean deviceLocationValue = preferences.getBoolean(deviceLocation, false);
            if (deviceLocationValue) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.map, new SupportMapFragment());
                ft.commit();
            } else {
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                if (mapFragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.map, mapFragment, null)
//                            .remove(mapFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        }
    }

}