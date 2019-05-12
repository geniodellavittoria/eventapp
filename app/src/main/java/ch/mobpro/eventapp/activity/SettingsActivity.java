package ch.mobpro.eventapp.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import ch.mobpro.eventapp.R;
import ch.mobpro.eventapp.fragment.SettingsFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class SettingsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final String deviceLocation = "deviceLocation";
    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private SharedPreferences preferences;
    private GoogleMap mMap;
    private Location mLastKnownLocation;
    private boolean mLocationPermissionGranted;
    private static final float DEFAULT_ZOOM = 12f;
    private Marker marker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar mActionBarToolbar = findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle(R.string.title_activity_settings);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        listener = this::toggleMapFragment;
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        preferences.registerOnSharedPreferenceChangeListener(listener);
        toggleMapFragment(preferences, deviceLocation);
    }

    private void toggleMapFragment(SharedPreferences preferences, String key) {
        if (deviceLocation.equals(key)) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map_settings);
            boolean deviceLocationValue = preferences.getBoolean(deviceLocation, false);
            if (!deviceLocationValue) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .show(mapFragment)
                        .addToBackStack(null)
                        .commit();
                ;
                mapFragment.getMapAsync(this);
            } else {
                if (mapFragment != null) {
                    preferences.edit().putLong(IntentConstants.CHOOSEN_LAT, 0).commit();
                    preferences.edit().putLong(IntentConstants.CHOOSEN_LONG, 0).commit();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(mapFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        updateLocationUI();
        getDeviceLocation();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            updateLocationUI();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, task -> {
                    mLastKnownLocation = (Location) task.getResult();
                    if (mLastKnownLocation != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                    }
                    mMap.setOnMapClickListener(latLng -> {
                        Toast.makeText(SettingsActivity.this, String.format("you tapped on lat:%.2f long:%.2f", latLng.latitude, latLng.longitude), Toast.LENGTH_SHORT).show();
                        if (marker != null) {
                            marker.remove();
                        }
                        LatLng pos = new LatLng(latLng.latitude, latLng.longitude);
                        String lat = String.valueOf(latLng.latitude);
                        String lng = String.valueOf(latLng.longitude);
                        preferences.edit().putString(IntentConstants.CHOOSEN_LAT, lat).apply();
                        preferences.edit().putString(IntentConstants.CHOOSEN_LONG, lng).apply();
                        marker = mMap.addMarker(new MarkerOptions().position(pos).title("your picked location"));
                    });
                });

            }
        } catch (
                SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, EventListActivity.class);
        startActivity(intent);
    }
}


