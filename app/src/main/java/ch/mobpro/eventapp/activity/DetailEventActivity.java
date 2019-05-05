package ch.mobpro.eventapp.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import ch.mobpro.eventapp.R;
import ch.mobpro.eventapp.base.BaseActivity;
import ch.mobpro.eventapp.databinding.ActivityDetailEventBinding;
import ch.mobpro.eventapp.model.Event;
import ch.mobpro.eventapp.service.AuthInterceptor;
import ch.mobpro.eventapp.viewmodel.EditEventViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

public class DetailEventActivity extends BaseActivity<ActivityDetailEventBinding> implements OnMapReadyCallback {

    private static final float DEFAULT_ZOOM = 12f;
    private String TAG = this.getClass().getSimpleName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private EditEventViewModel viewModel;
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
    private boolean isOwner;

    @Override
    protected int layoutRes() {
        return R.layout.activity_detail_event;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Event event = (Event) getIntent().getSerializableExtra("event");
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(EditEventViewModel.class);
        dataBinding.setViewModel(viewModel);

        mapToViewModel(event);
        viewModel.getUpdateSuccess().observe(this, this::onEditSuccess);
        String currentUser = AuthInterceptor.getInstance().getUsername();

        if (viewModel.eventDetails.getOrganizer().getUsername().equals(currentUser))
            this.isOwner = true;
        Toolbar toolbar = findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbar);
        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextDescription = findViewById(R.id.editTextDescription);
        Button btnChooseImg = findViewById(R.id.imageBtn);

        btnChooseImg.setEnabled(isOwner);
        editTextDescription.setEnabled(isOwner);
        editTextName.setEnabled(isOwner);
        pickStartTime = findViewById(R.id.editStartTime);
        pickStartTime.setEnabled(isOwner);
        int hour = viewModel.eventDetails.getStartTime().getHour();
        int minute = viewModel.eventDetails.getStartTime().getMinute();
        pickStartTime.setText(String.format("%d:%d", hour, minute));
        pickStartTime.setOnClickListener(v -> {
            timePickerDialog = new TimePickerDialog(DetailEventActivity.this, (view, hour1, minute1) -> {
                LocalTime time = LocalTime.of(hour1, minute1);
                viewModel.eventDetails.setStartTime(time);
                pickStartTime.setText(hour1 + ":" + minute1);
            }, hour, minute, true);
            timePickerDialog.show();
        });

        pickStartDate = findViewById(R.id.editStartDate);
        pickStartDate.setEnabled(isOwner);
        int day = viewModel.eventDetails.getStartDate().getDayOfMonth();
        int month = viewModel.eventDetails.getStartDate().getMonthValue();
        int year = viewModel.eventDetails.getStartDate().getYear();
        pickStartDate.setText(String.format("%d.%d.%d", day, month, year));
        pickStartDate.setOnClickListener(v -> {
            datePickerDialog = new DatePickerDialog(DetailEventActivity.this, (view, y, m, d) -> {
                m++;
                LocalDate date = LocalDate.of(y, m, d);
                viewModel.eventDetails.setStartDate(date);
                pickStartDate.setText(String.format("%d.%d.%d", d, m, y));
            }, year, month, day);
            datePickerDialog.show();
        });
        pickEndTime = findViewById(R.id.editEndTime);
        pickEndTime.setEnabled(isOwner);
        int hour_end = viewModel.eventDetails.getEndTime().getHour();
        int minute_end = viewModel.eventDetails.getEndTime().getMinute();
        pickEndTime.setText(String.format("%d:%d", hour_end, minute_end));
        pickEndTime.setOnClickListener(v -> {
            timePickerDialog = new TimePickerDialog(DetailEventActivity.this, (view, hour12, minute12) -> {
                LocalTime time = LocalTime.of(hour12, minute12);
                viewModel.eventDetails.setEndTime(time);
                pickEndTime.setText(String.format("%d:%d", hour12, minute12));
            }, hour_end, minute_end, true);
            timePickerDialog.show();
        });
        pickEndDate = findViewById(R.id.editEndDate);
        pickEndDate.setEnabled(isOwner);
        int day_end = viewModel.eventDetails.getEndDate().getDayOfMonth();
        int month_end = viewModel.eventDetails.getEndDate().getMonthValue();
        int year_end = viewModel.eventDetails.getEndDate().getYear();
        pickEndDate.setText(String.format("%d.%d.%d", day_end, month_end, year_end));
        pickEndDate.setOnClickListener(v -> {
            datePickerDialog = new DatePickerDialog(DetailEventActivity.this, (view, y, m, d) -> {
                m++;
                LocalDate date = LocalDate.of(y, m, d);
                viewModel.eventDetails.setEndDate(date);
                pickEndDate.setText(String.format("%d.%d.%d", d, m, y));
            }, year_end, month_end, day_end);
            datePickerDialog.show();
        });

        Spinner categorySpinner = findViewById(R.id.eventCategorySpinner);
        categorySpinner.setEnabled(isOwner);
        String[] categories = getResources().getStringArray(R.array.eventCategories);
        for (int i = 0; i < categories.length; i++)
            if (categories[i] == viewModel.eventDetails.getCategory())
                categorySpinner.setSelection(i);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.eventDetails.setCategory(categories[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        EditText editText = findViewById(R.id.placePicker);
        editText.setEnabled(isOwner);
        editText.setText(String.valueOf(viewModel.eventDetails.getPlace()));
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
        mapFragment.getView().setEnabled(isOwner);
    }

    private void mapToViewModel(Event event) {
        viewModel.eventDetails.setName(event.getName());
        viewModel.eventDetails.setEventImage(event.getEventImage());
        viewModel.eventDetails.setLatitude(event.getLatitude());
        viewModel.eventDetails.setLongitude(event.getLongitude());
        viewModel.eventDetails.setPlace(event.getPlace());
        viewModel.eventDetails.setPrice(event.getPrice());
        viewModel.eventDetails.setPrivateEvent(event.isPrivateEvent());
        viewModel.eventDetails.setOrganizer(event.getOrganizer());
        viewModel.eventDetails.setCategory(event.getCategories().get(0).getCategory());
        viewModel.eventDetails.setDescription(event.getDescription());
        LocalDateTime startDateTime = LocalDateTime.ofInstant(event.getStartTime(), ZoneOffset.UTC);
        viewModel.eventDetails.setStartDate(startDateTime.toLocalDate());
        viewModel.eventDetails.setStartTime(startDateTime.toLocalTime());
        LocalDateTime endDateTime = LocalDateTime.ofInstant(event.getEndTime(), ZoneOffset.UTC);
        viewModel.eventDetails.setEndDate(endDateTime.toLocalDate());
        viewModel.eventDetails.setEndTime(endDateTime.toLocalTime());
        viewModel.eventDetails.setOrganizer(event.getOrganizer());
    }

    private void parseToInt(EditText editText) {
        int placeInt = 0;
        try {
            placeInt = Integer.parseInt(editText.getText().toString());
        } catch (NumberFormatException ex) {
            Toast.makeText(this, "please enter a number", Toast.LENGTH_SHORT).show();
        }
        viewModel.eventDetails.setPlace(placeInt);
    }

    private void onEditSuccess(boolean isSuccess) {
        if (!isSuccess) {
            Toast.makeText(this, "Could not edit  event", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, EventListActivity.class);
            startActivity(intent);
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
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(viewModel.eventDetails.getLatitude(),
                                viewModel.eventDetails.getLongitude()), DEFAULT_ZOOM));

                marker = mMap.addMarker(new MarkerOptions().position(new LatLng(viewModel.eventDetails.getLatitude(), viewModel.eventDetails.getLongitude())));
                mMap.setOnMapClickListener(latLng -> {
                    if (isOwner) {
                        Toast.makeText(DetailEventActivity.this, String.format("you tapped on lat:%.2f long:%.2f", latLng.latitude, latLng.longitude), Toast.LENGTH_SHORT).show();
                        viewModel.eventDetails.setLatitude(latLng.latitude);
                        viewModel.eventDetails.setLongitude(latLng.longitude);
                        if (marker != null) {
                            marker.remove();
                        }
                        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title("your picked location"));
                    }
                });
            }
        } catch (
                SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public boolean isOwner() {
        return isOwner;
    }
}
