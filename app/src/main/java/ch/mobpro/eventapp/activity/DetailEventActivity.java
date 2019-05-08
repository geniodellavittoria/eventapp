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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import ch.mobpro.eventapp.R;
import ch.mobpro.eventapp.base.BaseActivity;
import ch.mobpro.eventapp.databinding.ActivityDetailEventBinding;
import ch.mobpro.eventapp.dto.EventDetailForm;
import ch.mobpro.eventapp.model.Event;
import ch.mobpro.eventapp.model.EventCategory;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        viewModel.event = event;

        updateToolbarName(event.getName());

        LocalDateTime startDateTime = LocalDateTime.ofInstant(event.getStartTime(), ZoneOffset.UTC);
        viewModel.setStartDate(startDateTime.toLocalDate());
        viewModel.setStartTime(startDateTime.toLocalTime());
        LocalDateTime endDateTime = LocalDateTime.ofInstant(event.getEndTime(), ZoneOffset.UTC);
        viewModel.setEndDate(endDateTime.toLocalDate());
        viewModel.setEndTime(endDateTime.toLocalTime());
        viewModel.getUpdateSuccess().observe(this, this::onEditSuccess);
        viewModel.getUpdatedEventName().observe(this, this::updateToolbarName);

        String currentUser = AuthInterceptor.getInstance().getUsername();

        if (viewModel.event.getUsername() != null && viewModel.event.getUsername().equals(currentUser))
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
        int hour = viewModel.getStartTime().getHour();
        int minute = viewModel.getStartTime().getMinute();
        pickStartTime.setText(String.format("%d:%d", hour, minute));
        pickStartTime.setOnClickListener(v -> {
            timePickerDialog = new TimePickerDialog(DetailEventActivity.this, (view, hour1, minute1) -> {
                LocalTime time = LocalTime.of(hour1, minute1);
                viewModel.setStartTime(time);
                pickStartTime.setText(hour1 + ":" + minute1);
            }, hour, minute, true);
            timePickerDialog.show();
        });

        pickStartDate = findViewById(R.id.editStartDate);
        pickStartDate.setEnabled(isOwner);
        int day = viewModel.getStartDate().getDayOfMonth();
        int month = viewModel.getStartDate().getMonthValue();
        int year = viewModel.getStartDate().getYear();
        pickStartDate.setText(String.format("%d.%d.%d", day, month, year));
        pickStartDate.setOnClickListener(v -> {
            datePickerDialog = new DatePickerDialog(DetailEventActivity.this, (view, y, m, d) -> {
                m++;
                LocalDate date = LocalDate.of(y, m, d);
                viewModel.setStartDate(date);
                pickStartDate.setText(String.format("%d.%d.%d", d, m, y));
            }, year, month, day);
            datePickerDialog.show();
        });
        pickEndTime = findViewById(R.id.editEndTime);
        pickEndTime.setEnabled(isOwner);
        int hour_end = viewModel.getEndTime().getHour();
        int minute_end = viewModel.getEndTime().getMinute();
        pickEndTime.setText(String.format("%d:%d", hour_end, minute_end));
        pickEndTime.setOnClickListener(v -> {
            timePickerDialog = new TimePickerDialog(DetailEventActivity.this, (view, hour12, minute12) -> {
                LocalTime time = LocalTime.of(hour12, minute12);
                viewModel.setEndTime(time);
                pickEndTime.setText(String.format("%d:%d", hour12, minute12));
            }, hour_end, minute_end, true);
            timePickerDialog.show();
        });
        pickEndDate = findViewById(R.id.editEndDate);
        pickEndDate.setEnabled(isOwner);
        int day_end = viewModel.getEndDate().getDayOfMonth();
        int month_end = viewModel.getEndDate().getMonthValue();
        int year_end = viewModel.getEndDate().getYear();
        pickEndDate.setText(String.format("%d.%d.%d", day_end, month_end, year_end));
        pickEndDate.setOnClickListener(v -> {
            datePickerDialog = new DatePickerDialog(DetailEventActivity.this, (view, y, m, d) -> {
                m++;
                LocalDate date = LocalDate.of(y, m, d);
                viewModel.setEndDate(date);
                pickEndDate.setText(String.format("%d.%d.%d", d, m, y));
            }, year_end, month_end, day_end);
            datePickerDialog.show();
        });

        Spinner categorySpinner = findViewById(R.id.eventCategorySpinner);
        categorySpinner.setEnabled(isOwner);
        String[] categories = getResources().getStringArray(R.array.eventCategories);
        for (int i = 0; i < categories.length; i++)
            if (categories[i] == viewModel.event.getCategories().get(0).getCategory())
                categorySpinner.setSelection(i);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<EventCategory> selectedCategories = new ArrayList<>();
                selectedCategories.add(new EventCategory(categories[position]));
                viewModel.event.setCategories(selectedCategories);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        EditText editText = findViewById(R.id.placePicker);
        editText.setEnabled(isOwner);
        editText.setText(String.valueOf(viewModel.event.getPlace()));
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

    private void updateToolbarName(String name) {
        Toolbar toolbar = findViewById(R.id.toolbarDetail);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        MenuItem deleteItem = menu.findItem(R.id.action_delete);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            Log.d(TAG, "onOptionsItemSelected: ");
            //TODO delete
        }
        return super.onOptionsItemSelected(item);
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
            updateLocationUI();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
                    mLastKnownLocation = (Location) task.getResult();
                    marker = mMap.addMarker(new MarkerOptions().position(new LatLng(viewModel.event.getLatitude(), viewModel.event.getLongitude())));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(mLastKnownLocation.getLatitude(),
                                    mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                    mMap.setOnMapClickListener(latLng -> {
                        if (isOwner) {
                            Toast.makeText(DetailEventActivity.this, String.format("you tapped on lat:%.2f long:%.2f", latLng.latitude, latLng.longitude), Toast.LENGTH_SHORT).show();
                            viewModel.event.setLatitude(latLng.latitude);
                            viewModel.event.setLongitude(latLng.longitude);
                            if (marker != null) {
                                marker.remove();
                            }
                            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title("your picked location"));
                        }
                    });
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
