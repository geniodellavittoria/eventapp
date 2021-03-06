package ch.mobpro.eventapp.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import ch.mobpro.eventapp.R;
import ch.mobpro.eventapp.base.BaseActivity;
import ch.mobpro.eventapp.databinding.ActivityCreateEventBinding;
import ch.mobpro.eventapp.viewmodel.CreateEventViewModel;
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

import javax.inject.Inject;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;

import static ch.mobpro.eventapp.activity.IntentConstants.*;
import static ch.mobpro.eventapp.utils.Base64BitmapUtil.getBitmapFromString;


public class CreateEventActivity extends BaseActivity<ActivityCreateEventBinding> implements OnMapReadyCallback {

    private static final float DEFAULT_ZOOM = 12f;
    private String TAG = this.getClass().getSimpleName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private CreateEventViewModel viewModel;
    private EditText pickStartTime;
    private EditText pickStartDate;
    private EditText pickEndTime;
    private EditText pickEndDate;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private LatLng mDefaultLocation = new LatLng(47.14, 8.43);
    private Marker marker;
    private CollapsingToolbarLayout mCollapsingToolbar;

    @Override
    protected int layoutRes() {
        return R.layout.activity_create_event;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CreateEventViewModel.class);
        dataBinding.setViewModel(viewModel);

        viewModel.getCreationSuccess().observe(this, this::onCreateSuccess);
        viewModel.getUpdatedEventName().observe(this, this::updateToolbarName);
        viewModel.getOnEventImageSelected().observe(this, this::onEventImageSelected);

        Toolbar toolbar = findViewById(R.id.toolbarDetail);
        toolbar.setTitle(getString(R.string.new_event));
        setSupportActionBar(toolbar);

        mCollapsingToolbar = findViewById(R.id.collapse_toolbar);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.event_default_image);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        viewModel.event.setEventImage(Base64.encodeToString(byteArray, Base64.DEFAULT));
        Palette.from(bitmap).generate(palette -> {
            int mutedColor = palette.getLightMutedColor(R.attr.colorPrimary);
            mCollapsingToolbar.setContentScrimColor(mutedColor);
        });

        pickStartTime = findViewById(R.id.editStartTime);
        pickStartTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR);
            int minute = calendar.get(Calendar.MINUTE);
            timePickerDialog = new TimePickerDialog(CreateEventActivity.this, (view, hour1, minute1) -> {
                LocalTime time = LocalTime.of(hour1, minute1);
                viewModel.event.setStartTime(time);
                pickStartTime.setText(hour1 + ":" + minute1);
            }, hour, minute, true);
            timePickerDialog.show();
        });

        pickStartDate = findViewById(R.id.editStartDate);
        pickStartDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            datePickerDialog = new DatePickerDialog(CreateEventActivity.this, (view, y, m, d) -> {
                m++;
                LocalDate date = LocalDate.of(y, m, d);
                viewModel.event.setStartDate(date);
                pickStartDate.setText(String.format("%d.%d.%d", d, m, y));
            }, year, month, day);
            datePickerDialog.show();
        });
        pickEndTime = findViewById(R.id.editEndTime);
        pickEndTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR);
            int minute = calendar.get(Calendar.MINUTE);
            timePickerDialog = new TimePickerDialog(CreateEventActivity.this, (view, hour12, minute12) -> {
                LocalTime time = LocalTime.of(hour12, minute12);
                viewModel.event.setEndTime(time);
                pickEndTime.setText(String.format("%d:%d", hour12, minute12));
            }, hour, minute, true);
            timePickerDialog.show();
        });
        pickEndDate = findViewById(R.id.editEndDate);
        pickEndDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            datePickerDialog = new DatePickerDialog(CreateEventActivity.this, (view, y, m, d) -> {
                m++;
                LocalDate date = LocalDate.of(y, m, d);
                viewModel.event.setEndDate(date);
                pickEndDate.setText(String.format("%d.%d.%d", d, m, y));
            }, year, month, day);
            datePickerDialog.show();
        });

        Spinner categorySpinner = findViewById(R.id.eventCategorySpinner);
        String[] categories = getResources().getStringArray(R.array.eventCategories);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.event.setCategory(categories[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        EditText editText = findViewById(R.id.placePicker);
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                parseToInt(editText);
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void updateToolbarName(String name) {
        if (mCollapsingToolbar == null) {
            mCollapsingToolbar = findViewById(R.id.collapse_toolbar);
        }
        mCollapsingToolbar.setTitle(name);
    }

    private void parseToInt(EditText editText) {
        int placeInt = 0;
        try {
            placeInt = Integer.parseInt(editText.getText().toString());
        } catch (NumberFormatException ex) {
            Toast.makeText(this, "please enter a number", Toast.LENGTH_SHORT).show();
        }
        viewModel.event.setPlace(placeInt);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d(TAG, "onOptionsItemSelected: " + id);
        if (id == R.id.action_save) {
            viewModel.createEvent();
        }
        return super.onOptionsItemSelected(item);
    }

    private void onCreateSuccess(boolean isSuccess) {
        if (!isSuccess) {
            Toast.makeText(this, "Could not create  event", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent();
            intent.putExtra(CREATE_EVENT_EXTRA, viewModel.eventResult);
            setResult(CREATE_EVENT, intent);
            finish();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        updateLocationUI();
        getDeviceLocation();
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            }
        }
        updateLocationUI();
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

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = (Location) task.getResult();
                        marker = mMap.addMarker(new MarkerOptions().position(mDefaultLocation).title("your location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                    mMap.setOnMapClickListener(latLng -> {
                        Toast.makeText(CreateEventActivity.this, String.format("you tapped on lat:%.2f long:%.2f", latLng.latitude, latLng.longitude), Toast.LENGTH_SHORT).show();
                        viewModel.event.setLatitude(latLng.latitude);
                        viewModel.event.setLongitude(latLng.longitude);
                        if (marker != null) {
                            marker.remove();
                        }
                        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title("your picked location"));
                    });
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void onEventImageSelected(Boolean eventImageSelected) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                InputStream dataStream = this.getContentResolver().openInputStream(data.getData());
                ImageView imageView = findViewById(R.id.header);
                viewModel.storeEventImage(dataStream);
                imageView.setImageBitmap(getBitmapFromString(viewModel.event.getEventImage()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
